import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

/** petit launcher : Menu / Jeu / Paramètres (CardLayout) */
public class Entrée {
    private static Jeu jeuRef; // je garde une ref vers le panneau du jeu pour lui pousser les couleurs

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Entrée::createAndShow); // juste pour lancer propre
    }

    private static void createAndShow() {
        JFrame f = new JFrame("Menu Principal");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // fenetre un peu large sinon les palettes se chevauchent
        f.setSize(1280, 800);
        f.setMinimumSize(new Dimension(1080, 720));
        f.setLocationRelativeTo(null);

        JPanel cards = new JPanel(new CardLayout());

        JPanel menu = buildMenu(cards);
        JPanel jeu = buildJeu(cards); // je mémorise le même panneau
        JPanel params = buildParams(cards); // pour changer les couleurs

        cards.add(menu, "MENU");
        cards.add(jeu, "JEU");
        cards.add(params, "PARAMS");

        f.setContentPane(cards);
        f.setVisible(true);
    }

    // === MENU : 2 gros boutons (jouer / paramètres)
    private static JPanel buildMenu(JPanel cards) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 0, 18, 0);
        gbc.gridx = 0;

        RoundedButton jouerBtn = new RoundedButton("JOUER",
                new Color(0, 120, 255), Color.WHITE, 30);
        jouerBtn.setPreferredSize(new Dimension(320, 90));
        jouerBtn.setFont(jouerBtn.getFont().deriveFont(Font.BOLD, 26f));
        jouerBtn.addActionListener(e -> show(cards, "JEU")); // hop on va sur le jeu

        RoundedButton paramBtn = new RoundedButton("PARAMÈTRES",
                new Color(0, 85, 200), Color.WHITE, 30);
        paramBtn.setPreferredSize(new Dimension(320, 90));
        paramBtn.setFont(paramBtn.getFont().deriveFont(Font.BOLD, 24f));
        paramBtn.addActionListener(e -> show(cards, "PARAMS"));

        gbc.gridy = 0;
        panel.add(jouerBtn, gbc);
        gbc.gridy = 1;
        panel.add(paramBtn, gbc);
        return panel;
    }

    // === JEU : je crée une seule instance (pratique pour lui pousser des options)
    private static JPanel buildJeu(JPanel cards) {
        if (jeuRef == null)
            jeuRef = new Jeu(cards);
        return jeuRef;
    }

    // === PARAMÈTRES : 2 palettes côte à côte + mini preview + appliquer
    private static JPanel buildParams(JPanel cards) {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // header basique
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titre = new JLabel("Paramètres");
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 28f));
        header.add(titre, BorderLayout.WEST);

        RoundedButton retour = new RoundedButton("◀ Retour Menu",
                new Color(30, 144, 255), Color.WHITE, 20);
        retour.addActionListener(e -> show(cards, "MENU"));
        header.add(retour, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        // 2 colonnes (Rond / Croix)
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        // --- Colonne gauche : ROND (J1)
        JPanel col1 = new JPanel(new BorderLayout(0, 8));
        col1.setOpaque(false);
        JLabel lbl1 = new JLabel("Couleur du ROND (Joueur 1)");
        lbl1.setFont(lbl1.getFont().deriveFont(Font.BOLD, 16f));

        JColorChooser chooser1 = new JColorChooser(new Color(0, 120, 255));
        chooser1.setPreviewPanel(new JPanel()); // je vire la grosse preview
        // je tente de garder que les “échantillons”, sinon je laisse le 1er panneau
        {
            AbstractColorChooserPanel[] panels = chooser1.getChooserPanels();
            AbstractColorChooserPanel keep = (panels.length > 0 ? panels[0] : null);
            for (AbstractColorChooserPanel p : panels) {
                String dn = String.valueOf(p.getDisplayName()).toLowerCase();
                String cn = p.getClass().getName().toLowerCase();
                if (dn.contains("swatch") || dn.contains("échant") || dn.contains("palette") || cn.contains("swatch")) {
                    keep = p;
                    break;
                }
            }
            if (keep != null)
                chooser1.setChooserPanels(new AbstractColorChooserPanel[] { keep });
        }
        chooser1.setPreferredSize(new Dimension(520, 340));
        chooser1.setMinimumSize(new Dimension(480, 300));
        col1.add(lbl1, BorderLayout.NORTH);
        col1.add(chooser1, BorderLayout.CENTER);

        // --- Colonne droite : CROIX (J2)
        JPanel col2 = new JPanel(new BorderLayout(0, 8));
        col2.setOpaque(false);
        JLabel lbl2 = new JLabel("Couleur de la CROIX (Joueur 2)");
        lbl2.setFont(lbl2.getFont().deriveFont(Font.BOLD, 16f));

        JColorChooser chooser2 = new JColorChooser(new Color(220, 60, 60));
        chooser2.setPreviewPanel(new JPanel()); // pareil
        {
            AbstractColorChooserPanel[] panels = chooser2.getChooserPanels();
            AbstractColorChooserPanel keep = (panels.length > 0 ? panels[0] : null);
            for (AbstractColorChooserPanel p : panels) {
                String dn = String.valueOf(p.getDisplayName()).toLowerCase();
                String cn = p.getClass().getName().toLowerCase();
                if (dn.contains("swatch") || dn.contains("échant") || dn.contains("palette") || cn.contains("swatch")) {
                    keep = p;
                    break;
                }
            }
            if (keep != null)
                chooser2.setChooserPanels(new AbstractColorChooserPanel[] { keep });
        }
        chooser2.setPreferredSize(new Dimension(520, 340));
        chooser2.setMinimumSize(new Dimension(480, 300));
        col2.add(lbl2, BorderLayout.NORTH);
        col2.add(chooser2, BorderLayout.CENTER);

        center.add(col1);
        center.add(col2);
        panel.add(center, BorderLayout.CENTER);

        // bas : mini preview + bouton appliquer
        JPanel south = new JPanel(new BorderLayout(12, 12));
        south.setOpaque(false);

        PreviewMini preview = new PreviewMini(); // petit rendu pour voir ce qu’on choisit
        preview.setPreferredSize(new Dimension(160, 80));
        preview.setBorder(new EmptyBorder(6, 6, 6, 6));

        // je sync la preview quand on bouge les curseurs
        preview.setColors(chooser1.getColor(), chooser2.getColor());
        chooser1.getSelectionModel()
                .addChangeListener(e -> preview.setColors(chooser1.getColor(), chooser2.getColor()));
        chooser2.getSelectionModel()
                .addChangeListener(e -> preview.setColors(chooser1.getColor(), chooser2.getColor()));

        south.add(preview, BorderLayout.CENTER);

        RoundedButton appliquer = new RoundedButton("Appliquer au jeu",
                new Color(0, 120, 255), Color.WHITE, 18);
        appliquer.addActionListener(e -> {
            if (jeuRef != null) {
                jeuRef.setPlayerColors(chooser1.getColor(), chooser2.getColor()); // on pousse au jeu
                JOptionPane.showMessageDialog(panel, "Couleurs appliquées au jeu !");
            }
        });
        JPanel applyWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        applyWrap.setOpaque(false);
        applyWrap.add(appliquer);
        south.add(applyWrap, BorderLayout.SOUTH);

        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    private static void show(JPanel cards, String name) {
        ((CardLayout) cards.getLayout()).show(cards, name); // switch d’écran
    }

    // bouton arrondi (juste style)
    static class RoundedButton extends JButton {
        private final Color bg, fg;
        private final int radius;

        RoundedButton(String text, Color background, Color foreground, int radius) {
            super(text);
            this.bg = background;
            this.fg = foreground;
            this.radius = radius;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(foreground);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(14, 26, 14, 26));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color base = getModel().isArmed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg;
            g2.setColor(base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(fg);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 3;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = Math.max(d.width, 200);
            d.height = Math.max(d.height, 60);
            return d;
        }
    }

    // mini aperçu (rond + croix) pour vérifier les couleurs vite fait
    static class PreviewMini extends JPanel {
        private Color p1 = new Color(0, 120, 255), p2 = new Color(220, 60, 60);

        PreviewMini() {
            setBackground(Color.WHITE);
        }

        void setColors(Color c1, Color c2) {
            if (c1 != null)
                p1 = c1;
            if (c2 != null)
                p2 = c2;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int size = Math.min(w, h) - 16; // petit
            size = Math.max(28, Math.min(size, 64));
            int pad = Math.max(3, size / 8);
            float stroke = Math.max(2f, size / 8f);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // rond (gauche)
            g2.setColor(p1);
            int d = size - 2 * pad;
            int cx1 = w / 3, cy = h / 2;
            g2.drawOval(cx1 - d / 2, cy - d / 2, d, d);

            // croix (droite)
            g2.setColor(p2);
            int cx2 = 2 * w / 3;
            int half = d / 2;
            g2.drawLine(cx2 - half, cy - half, cx2 + half, cy + half);
            g2.drawLine(cx2 - half, cy + half, cx2 + half, cy - half);

            g2.dispose();
        }
    }
}
