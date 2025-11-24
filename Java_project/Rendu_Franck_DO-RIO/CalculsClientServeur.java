import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rendu C : Client–Serveur de Calculs en Java concurrent.
 * On utilise un modèle producteur–consommateur avec des BlockingQueue pour les
 * demandes et les résultats.
 * Trois variantes sont proposées : V1 avec un seul demandeur, V2A avec une file
 * de résultats par demandeur,
 * et V2B avec une file de résultats unique et un filtrage par identifiant de
 * demandeur.
 * Les expressions sont validées avant évaluation et seules les opérations +, -,
 * *, / et les parenthèses sont autorisées.
 */
public class CalculsClientServeur {

    // ----- Messages échangés entre demandeurs et calculateurs -----
    static final class Job { // représente une demande de calcul
        final long jobId; // identifiant de la demande
        final int requesterId; // identifiant du demandeur (0 en V1, positif en V2)
        final String expr; // expression à évaluer, null signifie poison pill

        Job(long jobId, int requesterId, String expr) {
            this.jobId = jobId;
            this.requesterId = requesterId;
            this.expr = expr;
        }
    }

    static final class Result { // représente un résultat de calcul
        final long jobId;
        final int requesterId;
        final String expr;
        final Double value; // valeur calculée, null en cas d’erreur
        final String error; // message d’erreur, null si tout va bien

        Result(long jobId, int requesterId, String expr, Double value, String error) {
            this.jobId = jobId;
            this.requesterId = requesterId;
            this.expr = expr;
            this.value = value;
            this.error = error;
        }

        @Override
        public String toString() {
            return String.format("job=%d req=%d expr=\"%s\" -> %s",
                    jobId, requesterId, expr,
                    (error == null ? "val=" + value : "ERR=" + error));
        }
    }

    // ----- Évaluateur sûr : on découpe, on valide, on transforme en RPN, on évalue
    // -----
    static final class SafeEval {
        private static final Set<String> OPS = Set.of("+", "-", "*", "/"); // opérateurs autorisés

        private static int prec(String op) {
            return (op.equals("+") || op.equals("-")) ? 1 : 2;
        } // gestion des priorités

        private static boolean leftAssoc(String op) {
            return true;
        } // toutes les opérations sont associatives à gauche

        static Result evaluate(Job job) { // enchaînement complet avec gestion propre des erreurs
            String expr = job.expr;
            try {
                List<String> tokens = tokenize(expr); // on découpe l’expression en jetons
                validateTokens(tokens); // on vérifie la grammaire de base
                List<String> rpn = toRPN(tokens); // on applique l’algorithme de Shunting Yard
                double val = evalRPN(rpn); // on évalue la notation postfixée
                if (Double.isNaN(val) || Double.isInfinite(val)) // on rejette les valeurs non définies
                    return new Result(job.jobId, job.requesterId, expr, null, "Résultat non défini");
                return new Result(job.jobId, job.requesterId, expr, val, null);
            } catch (IllegalArgumentException ex) {
                return new Result(job.jobId, job.requesterId, expr, null, ex.getMessage()); // on remonte l’erreur
                                                                                            // lisible
            } catch (Exception ex) {
                return new Result(job.jobId, job.requesterId, expr, null, "Erreur inattendue"); // on garde un garde-fou
            }
        }

        static List<String> tokenize(String s) { // on isole nombres, opérateurs et parenthèses
            if (s == null)
                throw new IllegalArgumentException("Expression nulle");
            String src = s.replaceAll("\\s+", ""); // on retire les espaces
            if (src.isEmpty())
                throw new IllegalArgumentException("Expression vide");
            List<String> t = new ArrayList<>();
            int i = 0, n = src.length();
            while (i < n) {
                char c = src.charAt(i);
                if (Character.isDigit(c) || c == '.') { // on lit un nombre flottant
                    int j = i + 1;
                    while (j < n) {
                        char d = src.charAt(j);
                        if (Character.isDigit(d) || d == '.')
                            j++;
                        else
                            break;
                    }
                    String num = src.substring(i, j);
                    long dots = num.chars().filter(ch -> ch == '.').count();
                    if (dots > 1)
                        throw new IllegalArgumentException("Nombre mal formé: " + num);
                    t.add(num);
                    i = j;
                } else if ("+-*/()".indexOf(c) >= 0) { // on lit un symbole autorisé
                    t.add(String.valueOf(c));
                    i++;
                } else {
                    throw new IllegalArgumentException("Caractère invalide: '" + c + "'"); // on refuse les autres
                                                                                           // symboles
                }
            }
            return t;
        }

        static void validateTokens(List<String> tk) { // on contrôle la structure minimale
            if (tk.isEmpty())
                throw new IllegalArgumentException("Expression vide");
            // un opérateur ne doit pas apparaître en première ou en dernière position
            if (OPS.contains(tk.get(0)) || OPS.contains(tk.get(tk.size() - 1)))
                throw new IllegalArgumentException("Opérateur en position invalide");
            int bal = 0; // on vérifie l’équilibrage des parenthèses et les enchaînements
            for (int i = 0; i < tk.size(); i++) {
                String cur = tk.get(i);
                if (cur.equals("(")) {
                    bal++;
                    continue;
                }
                if (cur.equals(")")) {
                    bal--;
                    if (bal < 0)
                        throw new IllegalArgumentException("Parenthèses non équilibrées");
                }
                if (OPS.contains(cur)) {
                    String prev = tk.get(i - 1), next = tk.get(i + 1);
                    if (OPS.contains(prev) || prev.equals("("))
                        throw new IllegalArgumentException("Deux opérateurs se suivent");
                    if (OPS.contains(next) || next.equals(")"))
                        throw new IllegalArgumentException("Opérateur mal positionné");
                }
            }
            if (bal != 0)
                throw new IllegalArgumentException("Parenthèses non équilibrées");
        }

        static List<String> toRPN(List<String> in) { // on produit une file postfixée à l’aide d’une pile d’opérateurs
            Deque<String> ops = new ArrayDeque<>();
            List<String> out = new ArrayList<>();
            for (String tok : in) {
                if (OPS.contains(tok)) {
                    while (!ops.isEmpty()) {
                        String top = ops.peek();
                        boolean plusFort = prec(top) > prec(tok);
                        boolean egalEtGauche = prec(top) == prec(tok) && leftAssoc(tok);
                        if (OPS.contains(top) && (plusFort || egalEtGauche))
                            out.add(ops.pop());
                        else
                            break;
                    }
                    ops.push(tok);
                } else if (tok.equals("(")) {
                    ops.push(tok);
                } else if (tok.equals(")")) {
                    while (!ops.isEmpty() && !ops.peek().equals("("))
                        out.add(ops.pop());
                    if (ops.isEmpty() || !ops.peek().equals("("))
                        throw new IllegalArgumentException("Parenthèses non équilibrées");
                    ops.pop(); // on enlève la parenthèse ouvrante
                } else {
                    out.add(tok); // on ajoute le nombre à la sortie
                }
            }
            while (!ops.isEmpty()) {
                String op = ops.pop();
                if (op.equals("(") || op.equals(")"))
                    throw new IllegalArgumentException("Parenthèses non équilibrées");
                out.add(op);
            }
            return out;
        }

        static double evalRPN(List<String> rpn) { // on évalue la postfixée avec une pile d’opérandes
            Deque<Double> st = new ArrayDeque<>();
            for (String tk : rpn) {
                if (OPS.contains(tk)) {
                    if (st.size() < 2)
                        throw new IllegalArgumentException("Expression invalide");
                    double b = st.pop(), a = st.pop();
                    switch (tk) {
                        case "+":
                            st.push(a + b);
                            break;
                        case "-":
                            st.push(a - b);
                            break;
                        case "*":
                            st.push(a * b);
                            break;
                        case "/":
                            if (b == 0.0)
                                throw new IllegalArgumentException("Division par zéro");
                            st.push(a / b);
                            break;
                    }
                } else {
                    st.push(Double.parseDouble(tk));
                }
            }
            if (st.size() != 1)
                throw new IllegalArgumentException("Expression invalide");
            return st.pop();
        }
    }

    // ----- Serveur de calcul : plusieurs threads prennent les jobs et déposent les
    // résultats -----
    static final class Server {
        private final BlockingQueue<Job> in; // file des demandes
        private final BlockingQueue<Result> out; // file des résultats
        private final List<Thread> workers = new ArrayList<>();

        Server(BlockingQueue<Job> in, BlockingQueue<Result> out) {
            this.in = in;
            this.out = out;
        }

        void start(int n) { // on lance n calculateurs
            for (int i = 0; i < n; i++) {
                Thread t = new Thread(() -> {
                    try {
                        while (true) {
                            Job j = in.take(); // prise bloquante
                            if (j.expr == null)
                                break; // poison pill signifie arrêt
                            Result r = SafeEval.evaluate(j); // on évalue l’expression
                            out.put(r); // on envoie le résultat
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // arrêt propre
                    }
                }, "calc-" + i);
                workers.add(t);
                t.start();
            }
        }

        void stopGracefully() throws InterruptedException { // on arrête en douceur avec des poison pills
            for (int i = 0; i < workers.size(); i++)
                in.put(new Job(-1, -1, null));
            for (Thread t : workers)
                t.join();
        }
    }

    // ----- Version 1 : un demandeur unique et plusieurs calculateurs -----
    static void runV1(int nCalcs) throws Exception {
        System.out.println("=== Version 1 : un demandeur et plusieurs calculateurs ===");
        BlockingQueue<Job> qIn = new LinkedBlockingQueue<>();
        BlockingQueue<Result> qOut = new LinkedBlockingQueue<>();
        Server server = new Server(qIn, qOut);
        server.start(nCalcs);

        List<String> expressions = List.of( // petit jeu de tests
                "2+3", "10/2+7*(3-1)", "2+3-", "(1+2)*(3+4)/5", "42/(6-6)", "3.5*2+1");

        AtomicLong seq = new AtomicLong(1);
        Thread requester = new Thread(() -> { // un seul producteur
            try {
                for (String e : expressions) {
                    long id = seq.getAndIncrement();
                    System.out.printf("[REQ] envoie job=%d expr=%s%n", id, e);
                    qIn.put(new Job(id, 0, e)); // identifiant de demandeur fixé à zéro
                }
            } catch (InterruptedException ignored) {
            }
        }, "demandeur-unique");
        requester.start();

        int need = expressions.size(); // on consomme autant de résultats qu’il y a d’expressions
        while (need > 0) {
            Result r = qOut.take();
            System.out.println("[RES] " + r);
            need--;
        }

        server.stopGracefully();
        requester.join();
        System.out.println("=== V1 terminé ===");
    }

    // ----- Version 2A : plusieurs demandeurs et une file de résultats par
    // demandeur -----
    static void runV2A(int mRequesters, int nCalcs) throws Exception {
        System.out.println("=== Version 2A : plusieurs demandeurs avec une file de résultats dédiée à chacun ===");

        BlockingQueue<Job> qIn = new LinkedBlockingQueue<>();
        Map<Integer, BlockingQueue<Result>> resultsByRequester = new HashMap<>(); // chaque demandeur a sa file
        for (int i = 1; i <= mRequesters; i++)
            resultsByRequester.put(i, new LinkedBlockingQueue<>());

        // on met un routeur qui reçoit une file commune et redistribue dans la bonne
        // file
        BlockingQueue<Result> qOutRouter = new LinkedBlockingQueue<>();
        Thread router = new Thread(() -> {
            try {
                while (true) {
                    Result r = qOutRouter.take();
                    boolean isPoison = (r.expr == null && r.error == null && r.value == null && r.jobId == -1);
                    if (isPoison)
                        break;
                    BlockingQueue<Result> q = resultsByRequester.get(r.requesterId);
                    if (q != null)
                        q.put(r);
                }
            } catch (InterruptedException ignored) {
            }
        }, "router");
        router.start();

        Server server = new Server(qIn, qOutRouter);
        server.start(nCalcs);

        AtomicLong seq = new AtomicLong(1);
        List<Thread> requesters = new ArrayList<>();
        List<List<String>> exprs = List.of(
                List.of("1+2", "(3+4)*5", "7/0", "2+3-"),
                List.of("10-3*2", "8/(2+2)", "3.14*2"),
                List.of("6*(7-1)", "(1+2)*(3+(4-2))", "14/7"));

        // chaque producteur envoie ses expressions avec son identifiant propre
        for (int rid = 1; rid <= mRequesters; rid++) {
            final int requesterId = rid;
            List<String> myList = exprs.get((rid - 1) % exprs.size());
            Thread t = new Thread(() -> {
                try {
                    for (String e : myList) {
                        long id = seq.getAndIncrement();
                        System.out.printf("[REQ%d] envoie job=%d expr=%s%n", requesterId, id, e);
                        qIn.put(new Job(id, requesterId, e));
                    }
                } catch (InterruptedException ignored) {
                }
            }, "demandeur-" + requesterId);
            requesters.add(t);
            t.start();
        }

        // chaque collecteur lit uniquement la file qui lui est dédiée
        List<Thread> collectors = new ArrayList<>();
        for (int rid = 1; rid <= mRequesters; rid++) {
            final int requesterId = rid;
            List<String> myList = exprs.get((rid - 1) % exprs.size());
            Thread t = new Thread(() -> {
                int need = myList.size();
                try {
                    while (need > 0) {
                        Result r = resultsByRequester.get(requesterId).take();
                        System.out.println("[RES" + requesterId + "] " + r);
                        need--;
                    }
                } catch (InterruptedException ignored) {
                }
            }, "collect-" + requesterId);
            collectors.add(t);
            t.start();
        }

        for (Thread t : requesters)
            t.join();
        for (Thread t : collectors)
            t.join();

        server.stopGracefully(); // on arrête les calculateurs
        qOutRouter.put(new Result(-1, -1, null, null, null)); // on arrête le routeur
        router.join();

        System.out.println("=== V2A terminé ===");
    }

    // ----- Version 2B : plusieurs demandeurs et une file de résultats unique avec
    // filtrage -----
    static void runV2B(int mRequesters, int nCalcs) throws Exception {
        System.out.println("=== Version 2B : plusieurs demandeurs et une file de résultats unique avec filtrage ===");

        BlockingQueue<Job> qIn = new LinkedBlockingQueue<>();
        BlockingQueue<Result> qOut = new LinkedBlockingQueue<>();
        Server server = new Server(qIn, qOut);
        server.start(nCalcs);

        AtomicLong seq = new AtomicLong(1);
        List<Thread> requesters = new ArrayList<>();
        List<List<String>> exprs = List.of(
                List.of("1+2", "(3+4)*5", "7/0", "2+3-"),
                List.of("10-3*2", "8/(2+2)", "3.14*2"),
                List.of("6*(7-1)", "(1+2)*(3+(4-2))", "14/7"));

        // on crée plusieurs producteurs comme précédemment
        for (int rid = 1; rid <= mRequesters; rid++) {
            final int requesterId = rid;
            List<String> myList = exprs.get((rid - 1) % exprs.size());
            Thread t = new Thread(() -> {
                try {
                    for (String e : myList) {
                        long id = seq.getAndIncrement();
                        System.out.printf("[REQ%d] envoie job=%d expr=%s%n", requesterId, id, e);
                        qIn.put(new Job(id, requesterId, e));
                    }
                } catch (InterruptedException ignored) {
                }
            }, "demandeur-" + requesterId);
            requesters.add(t);
            t.start();
        }

        // chaque collecteur lit la file commune et remet ce qui ne lui appartient pas
        List<Thread> collectors = new ArrayList<>();
        for (int rid = 1; rid <= mRequesters; rid++) {
            final int requesterId = rid;
            List<String> myList = exprs.get((rid - 1) % exprs.size());
            Thread t = new Thread(() -> {
                int need = myList.size();
                try {
                    while (need > 0) {
                        Result r = qOut.take();
                        if (r.requesterId == requesterId) {
                            System.out.println("[RES" + requesterId + "] " + r);
                            need--;
                        } else {
                            qOut.put(r); // on remet le résultat pour le bon collecteur
                            Thread.yield(); // on laisse la main pour limiter la contention
                        }
                    }
                } catch (InterruptedException ignored) {
                }
            }, "collect-" + requesterId);
            collectors.add(t);
            t.start();
        }

        for (Thread t : requesters)
            t.join();
        for (Thread t : collectors)
            t.join();

        server.stopGracefully();
        System.out.println("=== V2B terminé ===");
    }

    // ----- Point d’entrée -----
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage:");
            System.err.println("  java CalculsClientServeur v1  <nCalcs>");
            System.err.println("  java CalculsClientServeur v2a <mReq> <nCalcs>");
            System.err.println("  java CalculsClientServeur v2b <mReq> <nCalcs>");
            System.exit(1);
        }
        String mode = args[0].toLowerCase(Locale.ROOT);
        switch (mode) {
            case "v1": {
                int nCalcs = (args.length >= 2) ? Integer.parseInt(args[1]) : 3;
                runV1(nCalcs);
                break;
            }
            case "v2a": {
                int mReq = (args.length >= 2) ? Integer.parseInt(args[1]) : 3;
                int nCalcs = (args.length >= 3) ? Integer.parseInt(args[2]) : 3;
                runV2A(mReq, nCalcs);
                break;
            }
            case "v2b": {
                int mReq = (args.length >= 2) ? Integer.parseInt(args[1]) : 3;
                int nCalcs = (args.length >= 3) ? Integer.parseInt(args[2]) : 3;
                runV2B(mReq, nCalcs);
                break;
            }
            default:
                throw new IllegalArgumentException("Mode inconnu: " + mode);
        }
    }
}

// code a éxécuter
// javac CalculsClientServeur.java
// java CalculsClientServeur v1 3
// java CalculsClientServeur v2a 3 3
// java CalculsClientServeur v2b 3 3
