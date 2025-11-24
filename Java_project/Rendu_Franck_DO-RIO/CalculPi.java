import java.util.concurrent.ThreadLocalRandom; // Import d'un générateur de RNG pour chaque threads 
import java.util.Arrays;

public final class CalculPi {

    // on crée un outil permettant de facilement générer des points dans un carrée
    static final class Sampler {
        static double nextDouble(double a, double b, ThreadLocalRandom rnd) {
            return a + (b - a) * rnd.nextDouble(); // Transformation affine d’un sur les intervalle souhaités
        }
    }

    // Variante avec partage du même quart de cercle et somation des hits
    static final class WorkerSum extends Thread {
        private final long samples; // Nombre d’échantillons à tirer pour ce thread
        private long hits; // Nombre de points tombés dans le quart de disque

        WorkerSum(long samples) {
            this.samples = samples; // Mémorise l'échantillon
        }

        public long getHits() { // get pour récupérer les hits
            return hits;
        }

        public long getSamples() { // On prend les N
            return samples;
        }

        @Override
        public void run() { // Corps du thread
            ThreadLocalRandom rnd = ThreadLocalRandom.current(); // On utilise l'import pour faire de la RNG par thread
            long h = 0;
            for (long i = 0; i < samples; i++) { // On passe l'échantillon
                double x = rnd.nextDouble();
                double y = rnd.nextDouble(); // On crée le point a étudier
                if (x * x + y * y <= 1.0) // Test l'appartenance au quart de disque
                    h++; // On ajoute dans les hits
            }
            hits = h; // ON stock tout les Hits dans le grand Hit
        }
    }
    // je ne commenterai pas les ligne de codes similaires à la partie d'avant

    // Variante avec moyennage sur le quart
    static final class WorkerAvg extends Thread {
        private final long samples; // Nombre d’échantillons pour ce thread
        private double estimateQuarter; // S/4 estimée par ce thread (proportion dans le quart)

        WorkerAvg(long samples) {
            this.samples = samples;
        }

        public double getEstimateQuarter() {
            return estimateQuarter;
        }

        @Override
        public void run() { // de la même manière on regénére des points aléatoirement
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            long h = 0;
            for (long i = 0; i < samples; i++) {
                double x = rnd.nextDouble();
                double y = rnd.nextDouble();
                if (x * x + y * y <= 1.0)
                    h++;
            }
            // Proportion dans le quart de disque (dans un carré aire=1) = aire du quart S/4
            estimateQuarter = (double) h / (double) samples; // On fait donc la moyenne mais on estime ici PI/4
        }
    }

    // Dernivière variante avec un thread par quart de cercle
    static final class WorkerQuarter extends Thread { // Thread dédié à un quart donné
        private final long samples; // Nombre d’échantillons pour ce quart
        private final int quadrant; // Numéro du quart
        private double quarterArea; // Aire estimée de ce quart S_q

        WorkerQuarter(long samples, int quadrant) {
            this.samples = samples;
            this.quadrant = quadrant; // Quart de disque ciblé
        }

        public double getQuarterArea() {
            return quarterArea;
        }

        @Override
        public void run() {
            ThreadLocalRandom rnd = ThreadLocalRandom.current();

            // ON défini les borne de chaque quart en fonction du quadrant
            double ax = 0, bx = 0, ay = 0, by = 0; // Intervalles [ax,bx] et [ay,by] selon le quart
            switch (quadrant) {
                case 1:
                    ax = 0.0;
                    bx = 1.0;
                    ay = 0.0;
                    by = 1.0;
                    break;
                case 2:
                    ax = -1.0;
                    bx = 0.0;
                    ay = 0.0;
                    by = 1.0;
                    break;
                case 3:
                    ax = -1.0;
                    bx = 0.0;
                    ay = -1.0;
                    by = 0.0;
                    break;
                case 4:
                    ax = 0.0;
                    bx = 1.0;
                    ay = -1.0;
                    by = 0.0;
                    break;
            }

            long h = 0; // Compteur de hits pour ce quart
            for (long i = 0; i < samples; i++) {
                double x = Sampler.nextDouble(ax, bx, rnd);
                double y = Sampler.nextDouble(ay, by, rnd);
                if (x * x + y * y <= 1.0)
                    h++;
            }
            // Dans un carré de surface 1, la proportion de hits est directement l’aire S_q
            quarterArea = (double) h / (double) samples; // ≈ aire du quart (π/4)
        }
    }

    // MAIN
    public static void main(String[] args) throws InterruptedException { // Point d’entrée
        if (args.length < 2) { // Vérifie les arguments d'entrées
            System.err.println("il y a un problème d'argument");
            // je prend les valeur par défaut
            System.err.println("(info) je pars sur: sum N=2000000 T=4");
            args = new String[] { "sum", "2000000", "4" };
            // ne pas faire System.exit(1);
        }

        String mode = args[0].toLowerCase(); // On passe le mot en minuscule
        long N = Long.parseLong(args[1]); // le nombre d'echantillions que l'utilisateur souhaite
        long tStart = System.nanoTime(); // Timestamp de début (nano) pour mesurer le temps

        switch (mode) { // On fait cas par cas selon le mode
            case "sum": {
                int T = Integer.parseInt(args[2]); // Nombre de threads
                long per = N / T, rem = N % T; // On mais donc le nombre de d'échantillions par threads et on ajoute ce
                                               // qui reste
                WorkerSum[] ws = new WorkerSum[T]; // on crée le worker
                for (int i = 0; i < T; i++) { // lancement des threads
                    long work = per + (i < rem ? 1 : 0); // sur le premier thread on ajoute le surplus
                    ws[i] = new WorkerSum(work);
                    ws[i].start(); // Démarre le thread
                }
                long hits = 0, total = 0; // On compte
                for (WorkerSum w : ws) { // ON Parcours tout les threads
                    w.join(); // Attend la fin du thread
                    hits += w.getHits(); // On Ajoute
                    total += w.getSamples(); // Ajoute son nombre d’échantillons
                }
                double pi = 4.0 * ((double) hits / (double) total); // Estimation π
                long tEnd = System.nanoTime(); // fin du temps
                report("sum", pi, total, T, tStart, tEnd); // Affiche résultat/erreur/temps
                break;
            }

            case "avg": {
                int T = Integer.parseInt(args[2]); // Nb de threads (idem que sum)
                long per = N / T; // Ici on impose la même charge pour chacun (pas de reste ici)
                if (per == 0) // Si on a plus de threads que de points, ça n’a pas de sens
                    throw new IllegalArgumentException("N doit être ≥ T pour avg"); // on prévient l’utilisateur

                long N_effective = per * T; // On “ramène” N au multiple de T pour faire une vraie moyenne simple
                if (N_effective != N) { // Si on a dû corriger N, on le dit
                    System.out.printf("(avg) Attention: N effectif ramené à %d pour une moyenne non pondérée.%n",
                            N_effective);
                }

                WorkerAvg[] ws = new WorkerAvg[T]; // Tableau de travailleurs (même logique de création que sum)
                for (int i = 0; i < T; i++) { // On démarre chaque thread avec exactement 'per' tirages
                    ws[i] = new WorkerAvg(per);
                    ws[i].start();
                }

                double sumQuarter = 0.0; // Ici, on ne somme pas des “hits” mais des “estimations de quart”
                for (WorkerAvg w : ws) { // Récupération (idem que sum pour le join)
                    w.join();
                    sumQuarter += w.getEstimateQuarter(); // On ajoute l’estimation S/4 de chaque thread
                }

                double quarterMean = sumQuarter / T; // Moyenne des S/4 (c’est le but de cette variante)
                double pi = 4.0 * quarterMean; // On repasse de S/4 à π en ×4
                long tEnd = System.nanoTime(); // fin du temps (idem)
                report("avg", pi, N_effective, T, tStart, tEnd); // On affiche (idem)
                break; // Fin du case
            }

            case "quarters": { // Variante 3 : un thread par quart, somme des aires
                int T = 4; // Ici c’est fixé : 4 quadrants → 4 threads
                long per = N / T, rem = N % T; // Même principe de répartition que sum (on découpe et on met le reste)
                WorkerQuarter[] ws = new WorkerQuarter[T]; // Les 4 workers, chacun pour un quart différent
                for (int q = 1; q <= 4; q++) { // q = 1..4 pour cibler Q1,Q2,Q3,Q4
                    long work = per + ((q - 1) < rem ? 1 : 0); // Même logique “on ajoute le surplus” mais par quart
                    ws[q - 1] = new WorkerQuarter(work, q); // On assigne le quadrant
                    ws[q - 1].start(); // Démarrage
                }

                double sumAreas = 0.0; // Ici on additionne directement les “aires de quart” (pas les hits)
                for (WorkerQuarter w : ws) { // Récupération classique
                    w.join();
                    sumAreas += w.getQuarterArea(); // S_1 + S_2 + S_3 + S_4 ≈ π
                }

                double pi = sumAreas; // Par définition de cette variante, la somme ≈ π
                long tEnd = System.nanoTime(); // fin du temps
                report("quarters", pi, N, T, tStart, tEnd); // Affiche le rapport
                break;
            }

            default: // Mode inconnu
                throw new IllegalArgumentException("mode inconnu: " + mode); // Signale l’erreur d’argument
        }
    }

    static void report(String mode, double pi, long N, int T, long tStart, long tEnd) { // Rapport d'affichage
        double errEst = 1.0 / Math.sqrt(N);
        System.out.printf("[%s] π ≈ %.9f  (N=%d, T=%d)%n", mode, pi, N, T); // Affiche π, N, T
        System.out.printf("Erreur (ordre de grandeur) ≈ ±%.6f%n", errEst); // Affiche l’erreur estimée
        System.out.printf("Temps: %.3f s%n", (tEnd - tStart) / 1e9); // Affiche le temps écoulé en secondes
    }
}

// Exécuter ces lignes de codes
// javac CalculPi.java il faut ajouter ceci devant pour que ca marche
// java -cp out CalculPi sum 2000000 4
// java -cp out CalculPi avg 2000000 4
// java -cp out CalculPi quarters 2000000