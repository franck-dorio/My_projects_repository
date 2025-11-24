import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;

/** Mon petit “jeu de l’araignée” (morpion mobile) */
public class Jeu extends JPanel {

    // pour revenir au menu via le CardLayout parent
    private final JPanel cards;

    // couleurs des pions (changeables depuis Paramètres)
    private Color colorP1 = new Color(0, 120, 255); // rond J1
    private Color colorP2 = new Color(220, 60, 60); // croix J2

    // boutons du haut
    private JButton btnBack, btnUndo, btnNew;

    // affichage infos / scores
    private PillLabel scoreJ1 = new PillLabel("Victoire J1: 0");
    private PillLabel scoreJ2 = new PillLabel("Victoire J2: 0");
    private JLabel lblTurn = new JLabel("Au tour du joueur 1", SwingConstants.CENTER);
    private JLabel lblNum = new JLabel("Tour n°: 0", SwingConstants.CENTER);

    // grille 3x3 (carrée)
    private final CellButton[] cells = new CellButton[9];
    private JPanel boardGrid;
    private JPanel boardWrapper;

    // état de la partie
    private int joueur = 1; // 1=rond, 2=croix
    private int numTour = 0; // on compte que les vrais coups
    private int vic1 = 0, vic2 = 0;

    // sélection (phase déplacements)
    private int clickState = 2; // 2: choisir pion ; 1: choisir destination
    private CellButton selected = null;

    // historique des coups (pour Undo)
    private static final int PLACE = 0, MOVE = 1;

    private static class Move {
        int type, player, from, to; // simple et efficace

        Move(int type, int player, int from, int to) {
            this.type = type;
            this.player = player;
            this.from = from;
            this.to = to;
        }
    }

    private final ArrayDeque<Move> history = new ArrayDeque<>(); // j’empile ici

    public Jeu(JPanel cards) {
        this.cards = cards;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // barre du haut (menu / undo / nouveau)
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(true);
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(8, 8, 0, 8));

        btnBack = makeTopButton("◀ Menu");
        btnBack.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, "MENU")); // retour menu (si
                                                                                              // autorisé)

        btnUndo = makeTopButton("⟲ Undo");
        btnUndo.addActionListener(e -> undo());

        btnNew = makeTopButton("⟲ Nouveau");
        btnNew.addActionListener(e -> newGame());

        topBar.add(btnBack);
        topBar.add(btnUndo);
        topBar.add(btnNew);
        add(topBar, BorderLayout.NORTH);

        // infos + scores (en bas)
        JPanel infoBar = new JPanel(new BorderLayout());
        infoBar.setOpaque(false);
        infoBar.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel centerInfos = new JPanel(new GridLayout(2, 1));
        centerInfos.setOpaque(false);
        lblTurn.setFont(lblTurn.getFont().deriveFont(Font.BOLD, 16f));
        centerInfos.add(lblTurn);
        centerInfos.add(lblNum);

        infoBar.add(scoreJ1, BorderLayout.WEST);
        infoBar.add(centerInfos, BorderLayout.CENTER);
        infoBar.add(scoreJ2, BorderLayout.EAST);
        add(infoBar, BorderLayout.SOUTH);

        // plateau carré centré
        boardWrapper = new JPanel(new GridBagLayout());
        boardWrapper.setBackground(Color.WHITE);
        boardWrapper.setBorder(new EmptyBorder(6, 6, 6, 6));

        boardGrid = new JPanel(new GridLayout(3, 3, 0, 0)) {
            @Override
            public Dimension getPreferredSize() {
                int s = Math.min(boardWrapper.getWidth() - 12, boardWrapper.getHeight() - 12);
                if (s <= 0)
                    s = 360; // taille par défaut
                return new Dimension(s, s);
            }
        };
        boardGrid.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        boardWrapper.add(boardGrid, gbc);
        add(boardWrapper, BorderLayout.CENTER);

        // je crée les 9 cases + écouteurs
        ActionListener listener = new CellClick();
        for (int i = 0; i < 9; i++) {
            cells[i] = new CellButton(i);
            cells[i].addActionListener(listener);
            boardGrid.add(cells[i]);
        }

        updateControlsState(); // on démarre avec Menu ok, Undo off
    }

    // petit helper pour faire des boutons bleus
    private JButton makeTopButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setBackground(new Color(30, 144, 255));
        b.setForeground(Color.WHITE);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // on change les couleurs à la volée depuis les paramètres
    public void setPlayerColors(Color p1, Color p2) {
        if (p1 != null)
            colorP1 = p1;
        if (p2 != null)
            colorP2 = p2;
        for (CellButton c : cells)
            c.repaint();
    }

    // --- logique de base ---
    private boolean inPlacementPhase() {
        return numTour < 6;
    } // au début on pose 3+3

    private void nextPlayer() {
        joueur = (joueur % 2) + 1;
        lblTurn.setText("Au tour du joueur " + joueur);
    }

    private void updateTurnLabel() {
        lblNum.setText("Tour n°: " + numTour);
    }

    // j'active/désactive Menu/Undo selon l'état
    private void updateControlsState() {
        boolean empty = isBoardEmpty();
        boolean hasHistory = !history.isEmpty();

        btnBack.setEnabled(empty); // menu seulement quand plateau vide
        btnBack.setToolTipText(empty ? "Retour au menu" : "Désactivé pendant la partie");

        btnUndo.setEnabled(hasHistory); // undo dispo si on a au moins 1 coup
        btnUndo.setToolTipText(hasHistory ? "Annuler le dernier coup" : "Aucun coup à annuler");
    }

    private boolean isBoardEmpty() {
        for (CellButton c : cells)
            if (c.getEtat() != 0)
                return false;
        return true;
    }

    private void newGame() {
        history.clear();
        for (CellButton c : cells) {
            c.setEtat(0);
            c.resetBorder(); // je remets la grille propre
        }
        joueur = 1;
        numTour = 0;
        clickState = 2;
        selected = null;
        lblTurn.setText("Au tour du joueur 1");
        lblNum.setText("Tour n°: 0");
        updateControlsState();
        repaint();
    }

    private void win(int who) {
        JOptionPane.showMessageDialog(this, (who == 1 ? "Joueur 1" : "Joueur 2") + " a gagné !");
        if (who == 1) {
            vic1++;
            scoreJ1.setText("Victoire J1: " + vic1);
        } else {
            vic2++;
            scoreJ2.setText("Victoire J2: " + vic2);
        }
        newGame(); // je relance direct
    }

    private int winner() {
        // les 8 combinaisons classiques
        int[][] L = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // lignes
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // colonnes
                { 0, 4, 8 }, { 2, 4, 6 } // diagonales
        };
        for (int[] t : L) {
            int e = cells[t[0]].getEtat();
            if (e != 0 && e == cells[t[1]].getEtat() && e == cells[t[2]].getEtat())
                return e;
        }
        return 0;
    }

    private boolean neighbors(CellButton a, CellButton b) {
        // voisinage 8 directions (simple)
        int ia = indexOf(a), ib = indexOf(b);
        int ra = ia / 3, ca = ia % 3;
        int rb = ib / 3, cb = ib % 3;
        int dr = Math.abs(ra - rb), dc = Math.abs(ca - cb);
        return ia != ib && dr <= 1 && dc <= 1;
    }

    private int indexOf(CellButton btn) {
        for (int i = 0; i < 9; i++)
            if (cells[i] == btn)
                return i;
        return -1;
    }

    // --- UNDO (j’annule le dernier coup) ---
    private void undo() {
        if (history.isEmpty())
            return;

        if (selected != null) { // si j’étais en train de déplacer
            selected.resetBorder();
            selected = null;
            clickState = 2;
        }

        Move m = history.pop();
        if (m.type == PLACE) {
            cells[m.to].setEtat(0);
            cells[m.to].resetBorder();
        } else {
            cells[m.from].setEtat(m.player);
            cells[m.from].resetBorder();
            cells[m.to].setEtat(0);
            cells[m.to].resetBorder();
        }

        numTour = Math.max(0, numTour - 1);
        joueur = m.player; // on rend la main à celui qui a joué
        lblTurn.setText("Au tour du joueur " + joueur);
        updateTurnLabel();
        updateControlsState();
    }

    // === clics sur les cases ===
    private class CellClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CellButton b = (CellButton) e.getSource();

            // Phase 1 : pose
            if (inPlacementPhase()) {
                if (b.getEtat() == 0) {
                    b.setEtat(joueur);
                    history.push(new Move(PLACE, joueur, -1, indexOf(b)));
                    numTour++;
                    updateTurnLabel();
                    updateControlsState(); // menu off, undo on
                    int w = winner();
                    if (w != 0) {
                        win(w);
                        return;
                    }
                    nextPlayer();
                }
                return;
            }

            // Phase 2 : déplacement
            if (clickState == 2) {
                if (b.getEtat() == joueur) {
                    selected = b;
                    b.applySelectedBorder(); // petit rouge pour montrer la sélection
                    clickState = 1;
                }
            } else { // on cherche une destination
                if (b == selected) {
                    b.resetBorder();
                    selected = null;
                    clickState = 2;
                } else if (b.getEtat() == 0 && neighbors(selected, b)) {
                    int from = indexOf(selected), to = indexOf(b);
                    selected.resetBorder();
                    selected.setEtat(0);
                    b.setEtat(joueur);
                    selected = null;
                    clickState = 2;

                    history.push(new Move(MOVE, joueur, from, to));
                    numTour++;
                    updateTurnLabel();
                    int w = winner();
                    if (w != 0) {
                        win(w);
                        return;
                    }
                    nextPlayer();
                    updateControlsState();
                }
            }
        }
    }

    // === Case de la grille (dessin + bordures qui tiennent) ===
    private class CellButton extends JButton {
        private int etat = 0; // 0 vide, 1 rond, 2 croix
        private final int index;
        private final Border baseBorder;

        CellButton(int index) {
            this.index = index;
            setFocusPainted(false);
            setContentAreaFilled(true);
            setBackground(Color.WHITE);
            setOpaque(true);

            // j'utilise des MatteBorder pour garder un quadrillage propre
            this.baseBorder = makeGridBorder(index);
            setBorder(baseBorder);
        }

        int getEtat() {
            return etat;
        }

        void setEtat(int e) {
            etat = e;
            repaint();
        }

        void applySelectedBorder() {
            Border sel = new LineBorder(Color.RED, 2, true);
            setBorder(new CompoundBorder(sel, baseBorder)); // rouge par-dessus la grille
        }

        void resetBorder() {
            setBorder(baseBorder);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (etat == 0)
                return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int size = Math.min(w, h);
            int pad = Math.max(6, size / 10);
            float stroke = Math.max(3f, size / 12f);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (etat == 1) { // rond
                g2.setColor(colorP1);
                int d = size - 2 * pad;
                int x = (w - d) / 2, y = (h - d) / 2;
                g2.drawOval(x, y, d, d);
            } else { // croix
                g2.setColor(colorP2);
                int x1 = pad, y1 = pad, x2 = w - pad, y2 = h - pad;
                g2.drawLine(x1, y1, x2, y2);
                g2.drawLine(x1, y2, x2, y1);
            }
            g2.dispose();
        }

        private Border makeGridBorder(int idx) {
            int row = idx / 3, col = idx % 3;
            int thickOuter = 4; // bords ext
            int thinInner = 2; // lignes internes
            int top = (row == 0) ? thickOuter : thinInner;
            int left = (col == 0) ? thickOuter : thinInner;
            int bottom = (row == 2) ? thickOuter : thinInner;
            int right = (col == 2) ? thickOuter : thinInner;
            Color grid = new Color(185, 190, 205);
            return new MatteBorder(top, left, bottom, right, grid);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(120, 120);
        }
    }

    // pastille bleue pour les scores (juste style)
    private static class PillLabel extends JLabel {
        PillLabel(String text) {
            super(text);
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 14f));
            setBorder(new EmptyBorder(8, 14, 8, 14));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 120, 255));
            int arc = 24;
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.height = Math.max(d.height, 32);
            return d;
        }
    }
}
