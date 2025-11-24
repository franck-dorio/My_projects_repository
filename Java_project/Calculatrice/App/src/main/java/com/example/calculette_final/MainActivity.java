package com.example.calculette_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "calc_prefs";
    private static final String KEY_HISTORY = "history_json";
    private static final int MAX_HISTORY = 50;

    // Petite tolérance numérique pour les comparaisons flottantes
    private static final double EPS = 1e-12;

    private EditText saisie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saisie = findViewById(R.id.input);
        // On garde le clavier logiciel masqué (clavier custom avec boutons)
        saisie.setShowSoftInputOnFocus(false);

        // Bouton "Historique"
        definirClic(R.id.btn_history, v -> openHistory());

        // Chiffres
        definirTouche(R.id.btn_0, "0");
        definirTouche(R.id.btn_1, "1");
        definirTouche(R.id.btn_2, "2");
        definirTouche(R.id.btn_3, "3");
        definirTouche(R.id.btn_4, "4");
        definirTouche(R.id.btn_5, "5");
        definirTouche(R.id.btn_6, "6");
        definirTouche(R.id.btn_7, "7");
        definirTouche(R.id.btn_8, "8");
        definirTouche(R.id.btn_9, "9");

        // Opérateurs et ponctuation
        definirTouche(R.id.btn_plus, "+");
        definirTouche(R.id.btn_minus, "-");
        definirTouche(R.id.btn_mul, "×"); // on normalise plus tard vers '*'
        definirTouche(R.id.btn_div, "÷"); // on normalise plus tard vers '/'
        definirTouche(R.id.btn_dot, ".");
        definirTouche(R.id.btn_lpar, "(");
        definirTouche(R.id.btn_rpar, ")");

        // DEL : supprime un caractère à gauche du curseur
        definirClic(R.id.btn_del, v -> {
            int start = Math.max(saisie.getSelectionStart(), 0);
            int end   = Math.max(saisie.getSelectionEnd(), 0);
            if (start == end && start > 0) {
                saisie.getText().delete(start - 1, start);
            } else if (start != end) {
                saisie.getText().delete(Math.min(start, end), Math.max(start, end));
            }
        });

        // C : efface tout
        definirClic(R.id.btn_clear, v -> saisie.setText(""));

        // = : évalue l’expression
        definirClic(R.id.btn_eq, v -> {
            String expression = saisie.getText().toString();
            if (expression.trim().isEmpty()) return;
            try {
                String resultat = evaluer(expression);
                saisie.setText(resultat);
                saisie.setSelection(resultat.length());

                // Ajoute à l’historique
                saveHistoryEntry(expression + " = " + resultat);

            } catch (IllegalArgumentException ex) {
                Toast.makeText(this, R.string.err_syntax, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openHistory() {
        startActivity(new Intent(this, HistoryActivity.class));
    }


    // Saisie / utilitaires de clic


    private void insererTexte(String s) {
        int start = Math.max(saisie.getSelectionStart(), 0);
        int end   = Math.max(saisie.getSelectionEnd(), 0);
        saisie.getText().replace(Math.min(start, end), Math.max(start, end), s, 0, s.length());
    }

    private void definirTouche(int btnId, String s) {
        Button b = findViewById(btnId);
        if (b != null) b.setOnClickListener(v -> insererTexte(s));
    }

    private void definirToucheSiExiste(int btnId, String s) {
        View v = findViewById(btnId);
        if (v instanceof Button) v.setOnClickListener(x -> insererTexte(s));
    }

    private void definirClic(int id, View.OnClickListener l) {
        View v = findViewById(id);
        if (v != null) v.setOnClickListener(l);
    }


    // Historique (SharedPreferences + JSON)


    private void saveHistoryEntry(String entry) {
        ArrayList<String> history = loadHistory();
        history.add(entry);
        // On borne la taille pour ne pas gonfler les prefs
        if (history.size() > MAX_HISTORY) {
            history = new ArrayList<>(history.subList(history.size() - MAX_HISTORY, history.size()));
        }
        saveHistory(history);
    }

    private ArrayList<String> loadHistory() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        String json = sp.getString(KEY_HISTORY, "[]");
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) list.add(arr.getString(i));
        } catch (JSONException ignored) {}
        return list;
    }

    private void saveHistory(ArrayList<String> list) {
        JSONArray arr = new JSONArray();
        for (String s : list) arr.put(s);
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(KEY_HISTORY, arr.toString())
                .apply();
    }


    // ÉVALUATION DE L’EXPRESSION
    // normalisation → tokenisation → RPN → évaluation de la pile


    /**
     * Évalue une expression entrée par l’utilisateur.
     * Étapes :
     * 1) Normalise les symboles (×, ÷ → *, /)
     * 2) Tokenise en nombres/opérateurs/parenthèses (gère + / - unaire)
     * 3) Convertit en RPN (algorithme de Shunting-Yard)
     * 4) Évalue la RPN via une pile
     * 5) Formate le résultat (entier propre ou décimal sans zéros traînants)
     */
    private String evaluer(String raw) {
        // 1) normalisation des opérateurs vers les opérateurs ASCII
        String expression = raw.replace('×', '*').replace('÷', '/');

        // 2) tokenisation
        List<String> tokens = decouperEnJetons(expression);

        // 3) conversion en notation polonaise inversée (RPN)
        List<String> rpn = enRPN(tokens);

        // 4) évaluation
        double value = evaluerRPN(rpn);

        // 5) formatage
        String out = (Math.abs(value - Math.rint(value)) < EPS)
                ? Long.toString((long) Math.rint(value))
                : Double.toString(value);

        return supprimerZerosFinaux(out);
    }

    private String supprimerZerosFinaux(String s) {
        // Enlève les zéros inutiles à la fin des décimales, et le point si plus rien après
        while (s.contains(".") && (s.endsWith("0") || s.endsWith("."))) {
            s = s.substring(0, s.length() - 1);
        }
        return s.isEmpty() ? "0" : s;
    }

    /**
     * Découpe l’expression en éléments (nombres, opérateurs, parenthèses).
     * Gère les signes (+/-) en début d’expression ou après un opérateur / '('.
     */
    private List<String> decouperEnJetons(String expression) {
        List<String> out = new ArrayList<>();
        int n = expression.length();
        boolean expectNumber = true; // vrai si on attend un nombre ou un signe unaire
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) continue;

            if (Character.isDigit(c) || c == '.') {
                buf.setLength(0);
                buf.append(c);
                while (i + 1 < n) {
                    char d = expression.charAt(i + 1);
                    if (Character.isDigit(d) || d == '.') {
                        buf.append(d);
                        i++;
                    } else break;
                }
                out.add(buf.toString());
                expectNumber = false;
                continue;
            }

            if (c == '+' || c == '-') {
                if (expectNumber) {
                    // unaire → colle au nombre qui suit
                    buf.setLength(0);
                    buf.append(c);
                    int j = i + 1;
                    boolean hasDigit = false;
                    while (j < n) {
                        char d = expression.charAt(j);
                        if (Character.isWhitespace(d)) { j++; continue; }
                        if (Character.isDigit(d) || d == '.') {
                            hasDigit = true;
                            buf.append(d);
                            j++;
                            while (j < n) {
                                char e = expression.charAt(j);
                                if (Character.isDigit(e) || e == '.') {
                                    buf.append(e);
                                    j++;
                                } else break;
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                    if (hasDigit) {
                        out.add(buf.toString());
                        i = j - 1;
                        expectNumber = false;
                        continue;
                    }
                }
                out.add(String.valueOf(c));
                expectNumber = true;
                continue;
            }

            if (c == '*' || c == '/' || c == '(' || c == ')') {
                out.add(String.valueOf(c));
                expectNumber = (c != ')');
                continue;
            }

            throw new IllegalArgumentException("Caractère invalide: " + c);
        }
        // Fusion des parenthèses si nécessaire (déjà géré par les règles ci-dessus)
        return out;
    }

    private boolean estOperateur(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    private boolean estOp(String s) { // alias utilitaire
        return estOperateur(s);
    }

    private int priorite(String op) {
        switch (op) {
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    private List<String> enRPN(List<String> jetons) {
        List<String> out = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String t : jetons) {
            if (estOperateur(t)) {
                while (!stack.isEmpty() && estOperateur(stack.peek())
                        && priorite(stack.peek()) >= priorite(t)) {
                    out.add(stack.pop());
                }
                stack.push(t);
            } else if ("(".equals(t)) {
                stack.push(t);
            } else if (")".equals(t)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    out.add(stack.pop());
                }
                if (stack.isEmpty()) throw new IllegalArgumentException("Parenthèse manquante");
                stack.pop(); // enlève '('
            } else {
                out.add(t);
            }
        }

        while (!stack.isEmpty()) {
            String s = stack.pop();
            if ("(".equals(s) || ")".equals(s)) throw new IllegalArgumentException("Parenthèse manquante");
            out.add(s);
        }

        return out;
    }

    private double evaluerRPN(List<String> rpn) {
        Deque<Double> st = new ArrayDeque<>();
        for (String t : rpn) {
            if (estOperateur(t)) {
                if (st.size() < 2) throw new IllegalArgumentException("Expression invalide");
                double b = st.pop();
                double a = st.pop();
                switch (t) {
                    case "+": st.push(a + b); break;
                    case "-": st.push(a - b); break;
                    case "*": st.push(a * b); break;
                    case "/":
                        if (Math.abs(b) < EPS) throw new IllegalArgumentException("Division par zéro");
                        st.push(a / b);
                        break;
                }
            } else {
                st.push(Double.parseDouble(t));
            }
        }
        if (st.size() != 1) throw new IllegalArgumentException("Expression invalide");
        return st.pop();
    }
}
