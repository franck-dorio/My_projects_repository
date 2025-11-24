import java.util.Arrays; // on importe pour afficher facilement les tableaux (Arrays.toString)
import java.util.concurrent.TimeUnit; // on importe pour faire des pauses lisibles (sleep en secondes)
import java.util.concurrent.locks.Condition; // on importe la variable de condition (await/signal)
import java.util.concurrent.locks.ReentrantLock; // on importe le verrou réentrant pour l’exclusion mutuelle

/**
 * Gestionnaire de Billes — ATTENTE PASSIVE (variables de condition). // on
 * explique le principe général
 * - demander(k): bloque tant que disponibles < k (while + await()). // on
 * attend sans consommer CPU tant qu’on ne peut pas prendre
 * - rendre(k): crédite et réveille (signalAll()). // on redonne et on réveille
 * ceux qui attendent
 * - Contrôleur: vérifie en continu 0 ≤ disponibles ≤ max. // on surveille
 * l’invariant de la ressource
 */
public final class GestionnaireBillesPassive {

    // On crée le stock de bille
    static final class Billes {
        private final ReentrantLock lock = new ReentrantLock(); // on crée un verrou pour protéger l’accès
        private final Condition enough = lock.newCondition(); // on crée la condition “assez de billes”
        private final int max; // on mémorise le stock max autorisé
        private int disponibles; // on garde le stock courant (protégé par lock)

        Billes(int max) { // on construit le lot de bille
            this.max = max; // on stocke le max
            this.disponibles = max; // on démarre plein
        }

        void demander(int k) throws InterruptedException { // on demande k billes
            lock.lock(); // on entre dans le vérou
            try {
                // IMPORTANT: while (et pas if) pour re-tester après les réveils
                while (disponibles < k) { // on n’a pas assez de billes donc attend
                    enough.await(); // On attend
                }
                // SC: réserver k billes on peut débiter
                disponibles -= k; // on retire k au stock
            } finally { // se fait quoi qu'il arrive
                lock.unlock(); // on libère le verrou
            }
        }

        // Rendre k billes : crédite puis réveille les demandeurs
        void rendre(int k) { // on rend k billes
            lock.lock(); // on protège la mise à jour
            try {
                disponibles += k; // on crédite le stock
                // signale tout le monde
                enough.signalAll(); // on réveille tous ceux qui attendaient “assez de billes”
            } finally {
                lock.unlock(); // on sort de la section critique
            }
        }

        // Lecture protégée pour le contrôleur on expose une lecture sûre
        // pourl’affichage/contrôle
        int lireDisponiblesSousLock() { // on lit “disponibles” sous lock
            lock.lock(); // on prend le verrou
            try {
                return disponibles; // on renvoie la valeur courante
            } finally {
                lock.unlock(); // on relâche le verrou après lecture
            }
        }

        int max() { // on expose le max
            return max; // on retourne la capacité max
        }
    }

    // les threads du travailleur
    static final class Travailleur extends Thread {
        private final String nom; // on garde un nom lisible pour les logs
        private final int k; // nombre de billes nécessaires
        private final int m; // nombre d’itérations
        private final Billes billes; // référence vers la ressource partagée

        Travailleur(String nom, int k, int m, Billes billes) { // on construit un travailleur avec ses paramètres
            super("Travailleur-" + nom); // on donne un nom au thread
            this.nom = nom;
            this.k = k;
            this.m = m;
            this.billes = billes;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= m; i++) {
                    log(nom, "demande " + k + " billes [iter " + i + "/" + m + "]"); // écrit la demande
                    billes.demander(k); // on tente d’acquérir k billes

                    log(nom, "obtient " + k + " billes, travail en cours…"); // on confirme l’obtention
                    // Pour rendre réaliste on simule un temps de travail

                    TimeUnit.SECONDS.sleep(k); // on dort k secondes

                    billes.rendre(k); // on rend k billes après travail
                    log(nom, "rend " + k + " billes"); // on trace la restitution
                }
                log(nom, "a terminé ses " + m + " itérations."); // on indique la fin pour ce thread
            } catch (InterruptedException e) { // si on a été interrompu pendant pendant le temps de travail
                Thread.currentThread().interrupt();
                log(nom, "interrompu."); // on log l’interruption
            }
        }
    }

    // thread du controleur
    static final class Controleur extends Thread { // un thread indépendant pour contrôler
        private final Billes billes; // on a besoin d’accéder à la ressource
        private volatile boolean running = true; // flag pour arrêter proprement

        Controleur(Billes billes) {
            super("Controleur");
            this.billes = billes; // on garde la référence à la ressource
        }

        public void arreter() { // on expose une méthode pour arrêter le contrôleur
            running = false;
            this.interrupt(); // on interrompt si jamais il dort
        }

        @Override
        public void run() { // boucle de contrôle
            try {
                while (running) { // on tourne tant qu’on n’a pas demandé l’arrêt
                    int d = billes.lireDisponiblesSousLock(); // on lit le stock
                    if (d < 0 || d > billes.max()) { // on vérifie l’invariant 0 ≤ d ≤ max
                        System.err.printf("[CTRL] VIOLATION: disponibles=%d (max=%d)%n", d, billes.max()); // on alerte
                    } else {
                        System.out.printf("[CTRL] OK: disponibles=%d / %d%n", d, billes.max()); // on affiche l’état
                    }
                    TimeUnit.SECONDS.sleep(1); // on attend 1 seconde entre deux checks (périodique)
                }
            } catch (InterruptedException ie) { // interruption
            }
            System.out.println("[CTRL] arrêté."); // on confirme l’arrêt du contrôleur
        }
    }

    private static synchronized void log(String who, String msg) { // synchronized pour éviter les logs entremêlés
        System.out.printf("[%s] %s%n", who, msg); // on formate proprement le message
    }

    // MAIN
    public static void main(String[] args) throws Exception {
        // Paramètres par défaut
        int maxBilles = 9; // on a 9 billes au total
        int[] demandes = { 4, 3, 5, 2 }; // besoin de chaque travailleur
        int m = 2; // chaque travailleur répète m fois la séquence demandé

        // l’usage
        if (args.length >= 2) { // si on a au moins max et m
            maxBilles = Integer.parseInt(args[0]); // on lit le max depuis la ligne de commande
            m = Integer.parseInt(args[1]);
            if (args.length > 2) { // si on a aussi les demandes des travailleurs
                demandes = new int[args.length - 2]; // on alloue le tableau pour k1..kN
                for (int i = 2; i < args.length; i++) // on parcourt les args restants
                    demandes[i - 2] = Integer.parseInt(args[i]); // on remplit les demandes
            }
        }

        Billes billes = new Billes(maxBilles); // on crée la ressource partagée avec le stock initial

        // Créer travailleurs
        Travailleur[] workers = new Travailleur[demandes.length]; // on crée le tableau des threads travailleurs
        for (int i = 0; i < demandes.length; i++) {
            workers[i] = new Travailleur("P" + (i + 1), demandes[i], m, billes); // on crée les travailleurs avec son k
        }

        // Créer + lancer contrôle
        Controleur ctrl = new Controleur(billes); // on instancie le contrôleur
        ctrl.start();

        // Lancer travailleurs
        Arrays.stream(workers).forEach(Thread::start);

        // Attendre la fin de tous les travailleurs
        for (Thread t : workers) // on parcourt les threads
            t.join(); // on bloque le main jusqu’à leur fin

        // Arrêter proprement le contrôleur

        ctrl.arreter(); // on passe running=false et on l’interrompt
        ctrl.join();

        System.out.println("Terminé");
    }
}

// code a tester
// java GestionnaireBillesPassive.java
// java GestionnaireBillesPassive.java 9 3 4 3 5 2