import java.util.concurrent.locks.ReentrantLock; // verrou pour protéger l’état partagé
import java.util.concurrent.locks.LockSupport; // sommeil précis en nanosecondes
import java.util.concurrent.TimeUnit; // conversion ms → ns

/**
 * Rendu D — Régulation de température (mini système temps réel). // on décrit
 * le but du programme
 * Entités : T (température), P (pression), S (écran), Contrôleur, Chauffage, //
 * on liste les tâches/acteurs
 * Pompe. // pompe = actionneur pression
 * Mémoire partagée protégée par verrou pour MemT/MemP ; booléens go* sans //
 * on protège T/P avec lock, les booléens sont volatiles
 * mutex (contrôleur écrit, actionneurs lisent). // modèle du diapo : écriture
 * simple des commandes
 * Périodicité : "delay until next" via horloge monotone + parkNanos. // on fait
 * un tick périodique déterministe
 */
public final class RegulationTempPression {

    // état partagé : T/P sous verrou, ordres en volatile (lecture sans verrou)
    static final class EtatPartage {
        final ReentrantLock verrou = new ReentrantLock(); // protège memT et memP
        double memT = 20.0; // température simulée
        double memP = 1.0; // pression simulée
        volatile boolean ordChauffage = false; // ordre chauffage
        volatile boolean ordPompe = false; // ordre pompe
    }

    // configuration : seuils et périodes des tâches
    static final class Configuration {
        final double seuilT, seuilP; // seuils de décision
        final long periodeTms, periodePms; // périodes capteurs
        final long periodeActuMs, periodeCtrlMs, periodeEcranMs; // périodes actionneurs/contrôleur/écran

        Configuration(double seuilT, double seuilP,
                long periodeTms, long periodePms,
                long periodeActuMs, long periodeCtrlMs, long periodeEcranMs) {
            this.seuilT = seuilT;
            this.seuilP = seuilP;
            this.periodeTms = periodeTms;
            this.periodePms = periodePms;
            this.periodeActuMs = periodeActuMs;
            this.periodeCtrlMs = periodeCtrlMs;
            this.periodeEcranMs = periodeEcranMs;
        }
    }

    // capteurs simulés + petites dynamiques (chauffage ↓↑ T, pompe ↓ P)
    static double lireThermocoupleBrut() {
        return Math.random();
    } // bruit

    static double lirePressionBrute() {
        return Math.random();
    } // bruit

    static double convertirADTemp(double brut, double tPrec, boolean chauffageOn) {
        double delta = (chauffageOn ? +0.2 : -0.05) + (brut - 0.5) * 0.1; // dérive + bruit
        return borner(tPrec + delta, 0.0, 120.0); // borne T
    }

    static double convertirADPression(double brut, double pPrec, boolean pompeOn) {
        double delta = (pompeOn ? -0.15 : +0.05) + (brut - 0.5) * 0.05; // dérive + bruit
        return borner(pPrec + delta, 0.5, 5.0); // borne P
    }

    static double borner(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    // base des tâches périodiques : “delay until next” (évite dérive et busy-wait)
    static abstract class TachePeriodique extends Thread {
        private final long periodeNanos; // période ciblée en ns
        protected volatile boolean actif = true; // arrêt propre

        TachePeriodique(String nom, long periodeMillis) {
            super(nom);
            this.periodeNanos = TimeUnit.MILLISECONDS.toNanos(periodeMillis); // ms→ns
            setDaemon(true); // les threads n’empêchent pas l’arrêt du process
        }

        public void arreter() {
            actif = false;
            interrupt();
        } // coupe la boucle et réveille si endormi

        @Override
        public final void run() {
            long prochain = System.nanoTime(); // horloge monotone pour la précision
            try {
                while (actif) {
                    tick(); // travail périodique
                    prochain += periodeNanos; // on vise l’instant suivant
                    long attente = prochain - System.nanoTime();
                    if (attente > 0)
                        LockSupport.parkNanos(attente); // sommeil précis
                }
            } catch (Exception e) {
                System.err.println("[" + getName() + "] erreur: " + e);
            }
        }

        protected abstract void tick() throws Exception; // implémentation par sous-classes
    }

    // capteur température : met à jour memT sous verrou (cohérence inter-threads)
    static final class TacheTemperature extends TachePeriodique {
        private final EtatPartage etat;

        TacheTemperature(EtatPartage etat, long periodeMs) {
            super("T-Temperature", periodeMs);
            this.etat = etat;
        }

        @Override
        protected void tick() {
            double brut = lireThermocoupleBrut();
            etat.verrou.lock(); // début section critique
            try {
                etat.memT = convertirADTemp(brut, etat.memT, etat.ordChauffage);
            } finally {
                etat.verrou.unlock();
            } // fin section critique
        }
    }

    // capteur pression : met à jour memP sous verrou
    static final class TachePression extends TachePeriodique {
        private final EtatPartage etat;

        TachePression(EtatPartage etat, long periodeMs) {
            super("P-Pression", periodeMs);
            this.etat = etat;
        }

        @Override
        protected void tick() {
            double brut = lirePressionBrute();
            etat.verrou.lock();
            try {
                etat.memP = convertirADPression(brut, etat.memP, etat.ordPompe);
            } finally {
                etat.verrou.unlock();
            }
        }
    }

    // écran : lit T/P atomiquement (sous verrou) puis affiche
    static final class TacheEcran extends TachePeriodique {
        private final EtatPartage etat;

        TacheEcran(EtatPartage etat, long periodeMs) {
            super("S-Ecran", periodeMs);
            this.etat = etat;
        }

        @Override
        protected void tick() {
            double t, p;
            etat.verrou.lock();
            try {
                t = etat.memT;
                p = etat.memP;
            } // lecture cohérente
            finally {
                etat.verrou.unlock();
            }
            System.out.printf("[ECRAN] T=%.1f°C  |  P=%.2f bar%n", t, p);
        }
    }

    // actionneurs : lisent des booléens volatiles (pas de verrou, visibilité
    // garantie)
    static final class TacheChauffage extends TachePeriodique {
        private final EtatPartage etat;
        private boolean etatLocal = false; // détection de changement

        TacheChauffage(EtatPartage etat, long periodeMs) {
            super("Act-Chauffage", periodeMs);
            this.etat = etat;
        }

        @Override
        protected void tick() {
            boolean voulu = etat.ordChauffage;
            if (voulu != etatLocal) {
                etatLocal = voulu;
                System.out.println("[Chauffage] " + (etatLocal ? "ON" : "OFF"));
            }
        }
    }

    static final class TachePompe extends TachePeriodique {
        private final EtatPartage etat;
        private boolean etatLocal = false;

        TachePompe(EtatPartage etat, long periodeMs) {
            super("Act-Pompe", periodeMs);
            this.etat = etat;
        }

        @Override
        protected void tick() {
            boolean voulu = etat.ordPompe;
            if (voulu != etatLocal) {
                etatLocal = voulu;
                System.out.println("[Pompe] " + (etatLocal ? "ON" : "OFF"));
            }
        }
    }

    // contrôleur : lit T/P sous verrou, calcule les ordres, écrit en volatile (pas
    // besoin de verrou)
    static final class TacheControleur extends TachePeriodique {
        private final EtatPartage etat;
        private final Configuration cfg;
        private final TacheTemperature tT;
        private final TachePression tP;
        private final TacheChauffage tCh;
        private final TachePompe tPo;
        private final TacheEcran tE;

        TacheControleur(EtatPartage etat, Configuration cfg) {
            super("CTRL", cfg.periodeCtrlMs);
            this.etat = etat;
            this.cfg = cfg;
            this.tT = new TacheTemperature(etat, cfg.periodeTms);
            this.tP = new TachePression(etat, cfg.periodePms);
            this.tCh = new TacheChauffage(etat, cfg.periodeActuMs);
            this.tPo = new TachePompe(etat, cfg.periodeActuMs);
            this.tE = new TacheEcran(etat, cfg.periodeEcranMs);
        }

        void demarrerTout() {
            start();
            tT.start();
            tP.start();
            tCh.start();
            tPo.start();
            tE.start();
        }

        void arreterTout() {
            arreter();
            tT.arreter();
            tP.arreter();
            tCh.arreter();
            tPo.arreter();
            tE.arreter();
        }

        @Override
        protected void tick() {
            double T, P;
            etat.verrou.lock();
            try {
                T = etat.memT;
                P = etat.memP;
            } // lecture atomique des mesures
            finally {
                etat.verrou.unlock();
            }

            // décision selon le diapo (simple, sans hystérésis)
            boolean ordCh, ordPo;
            if (T > cfg.seuilT) { // T trop haute
                ordCh = false; // couper chauffage
                ordPo = (P > cfg.seuilP); // pompe si P trop haute
            } else if (T < cfg.seuilT) { // T trop basse
                ordCh = true; // allumer chauffage
                ordPo = true; // allumer pompe (selon l’énoncé)
            } else { // T égale au seuil
                ordCh = false;
                ordPo = (P > cfg.seuilP);
            }

            etat.ordChauffage = ordCh; // écriture volatile → visible immédiatement
            etat.ordPompe = ordPo;
            System.out.printf("[CTRL] T=%.1f/%.1f  P=%.2f/%.2f  -> ch=%s  po=%s%n",
                    T, cfg.seuilT, P, cfg.seuilP, ordCh, ordPo);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration cfg = new Configuration(60.0, 3.0, 200L, 250L, 300L, 500L, 500L);
        EtatPartage etat = new EtatPartage();
        TacheControleur ctrl = new TacheControleur(etat, cfg);

        System.out.println("=== Régulation T/P — démarrage ===");
        ctrl.demarrerTout();

        Thread.sleep(8000); // durée de démo
        System.out.println("=== Arrêt des tâches ===");
        ctrl.arreterTout();

        Thread.sleep(200); // petite marge de sortie
        System.out.println("=== Terminé proprement ===");
    }
}

// le code pour tester
// javac RegulationTempPression.java
// java RegulationTempPression
