package view;

import DAO.CommandeDAO;
import DAO.LigneCommandeDAO;
import model.Article;
import model.Client;
import model.Commande;
import model.LigneCommande;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VuePanier extends JPanel {
    private JPanel panelArticles;
    private FenetrePrincipale fenetrePrincipale;
    private JLabel totalLabel;

    public VuePanier(FenetrePrincipale parent) {
        this.fenetrePrincipale = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Panel central pour afficher les articles du panier
        panelArticles = new JPanel();
        panelArticles.setLayout(new BoxLayout(panelArticles, BoxLayout.Y_AXIS));
        panelArticles.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(panelArticles);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Création des boutons
        JButton commanderBtn = new JButton("Valider la commande");
        JButton retourBtn = new JButton("⬅ Retour catalogue");
        JButton deconnexionBtn = new JButton("Déconnexion");

        commanderBtn.setFont(new Font("Arial", Font.BOLD, 13));
        retourBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        deconnexionBtn.setFont(new Font("Arial", Font.PLAIN, 12));

        commanderBtn.setBackground(new Color(59, 89, 152));
        commanderBtn.setForeground(Color.WHITE);
        retourBtn.setBackground(Color.LIGHT_GRAY);
        deconnexionBtn.setBackground(Color.LIGHT_GRAY);

        commanderBtn.setFocusPainted(false);
        retourBtn.setFocusPainted(false);
        deconnexionBtn.setFocusPainted(false);

        commanderBtn.setPreferredSize(new Dimension(180, 30));
        retourBtn.setPreferredSize(new Dimension(160, 30));
        deconnexionBtn.setPreferredSize(new Dimension(160, 30));

        // Action commander avec fenêtre de paiement
        commanderBtn.addActionListener(e -> {
            Client client = fenetrePrincipale.getClientConnecte();
            if (client == null || fenetrePrincipale.getPanier().getArticles().isEmpty()) return;

            JDialog paiementDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Paiement", true);
            paiementDialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel label1 = new JLabel("Numéro de carte :");
            JTextField champ1 = new JTextField(15);
            JLabel label2 = new JLabel("CVC :");
            JTextField champ2 = new JTextField(15);
            JButton validerPaiementBtn = new JButton("Valider Paiement");

            gbc.gridx = 0; gbc.gridy = 0; paiementDialog.add(label1, gbc);
            gbc.gridx = 1; paiementDialog.add(champ1, gbc);
            gbc.gridx = 0; gbc.gridy = 1; paiementDialog.add(label2, gbc);
            gbc.gridx = 1; paiementDialog.add(champ2, gbc);
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            paiementDialog.add(validerPaiementBtn, gbc);

            paiementDialog.setSize(350, 250);
            paiementDialog.setLocationRelativeTo(this);

            validerPaiementBtn.addActionListener(evt -> {
                if (champ1.getText().trim().isEmpty() || champ2.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(paiementDialog, "Veuillez remplir les deux champs !");
                    return;
                }

                paiementDialog.dispose();

                Commande commande = new Commande(client.getId());
                new CommandeDAO().ajouterCommande(commande);
                fenetrePrincipale.ajouterCommandeSession(commande.getId());

                for (Article a : fenetrePrincipale.getPanier().getArticles()) {
                    int qte = fenetrePrincipale.getPanier().getQuantite(a);
                    new LigneCommandeDAO().ajouterLigneCommande(new LigneCommande(commande.getId(), a.getId(), qte));
                }

                fenetrePrincipale.getPanier().vider();
                JOptionPane.showMessageDialog(this, "Commande validée !");
                mettreAJourPanier(fenetrePrincipale);
                fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_HISTORIQUE);
            });

            paiementDialog.setVisible(true);
        });

        retourBtn.addActionListener(e -> fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_CATALOGUE));
        deconnexionBtn.addActionListener(e -> {
            fenetrePrincipale.setClientConnecte(null);
            fenetrePrincipale.getPanier().vider();
            fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_CONNEXION);
        });

        totalLabel = new JLabel("💰 Total : 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bas.setBackground(new Color(245, 245, 245));
        bas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bas.add(totalLabel);
        bas.add(retourBtn);
        bas.add(commanderBtn);
        bas.add(deconnexionBtn);
        add(bas, BorderLayout.SOUTH);

        // Bandeau haut avec titre et bouton historique
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel titre = new JLabel("Votre panier", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 20));
        titre.setForeground(new Color(59, 89, 152));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        topPanel.add(titre, BorderLayout.CENTER);

        JButton historiqueBtn = new JButton("Récapitulatif des commandes");
        historiqueBtn.setBackground(new Color(100, 149, 237));
        historiqueBtn.setForeground(Color.WHITE);
        historiqueBtn.setFocusPainted(false);
        historiqueBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        historiqueBtn.setPreferredSize(new Dimension(220, 35));
        historiqueBtn.addActionListener(e -> fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_HISTORIQUE));

        JPanel boutonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutonPanel.setBackground(new Color(245, 245, 245));
        boutonPanel.add(historiqueBtn);
        topPanel.add(boutonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    public void mettreAJourPanier(FenetrePrincipale parent) {
        panelArticles.removeAll();
        List<Article> articles = new ArrayList<>(parent.getPanier().getArticles());
        double totalGlobal = 0.0;

        if (articles.isEmpty()) {
            JLabel vide = new JLabel("Votre panier est vide.");
            vide.setFont(new Font("Arial", Font.ITALIC, 14));
            vide.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            panelArticles.add(vide);
        } else {
            for (Article a : articles) {
                int qte = parent.getPanier().getQuantite(a);
                double prixUnitaire = a.getPrixUnitaire();
                double prixGros = a.getPrixGros();
                int seuil = a.getSeuilGros();
                double sousTotal;
                String prixTexte;

                if (seuil > 0 && qte == seuil) {
                    sousTotal = qte * prixGros;
                    prixTexte = "<html><b>" + a.getNom() + "</b><br>" +
                            qte + " x " + prixGros + " € (prix gros)<br>Total : " +
                            String.format("%.2f", sousTotal) + " €</html>";
                } else if (seuil > 0 && qte > seuil) {
                    int reste = qte - seuil;
                    sousTotal = seuil * prixGros + reste * prixUnitaire;
                    prixTexte = "<html><b>" + a.getNom() + "</b><br>" +
                            seuil + " x " + prixGros + " € (prix gros)<br>" +
                            reste + " x " + prixUnitaire + " € (prix unitaire)<br>" +
                            "Total : " + String.format("%.2f", sousTotal) + " €</html>";
                } else {
                    sousTotal = qte * prixUnitaire;
                    prixTexte = "<html><b>" + a.getNom() + "</b><br>" +
                            qte + " x " + prixUnitaire + " €<br>Total : " +
                            String.format("%.2f", sousTotal) + " €</html>";
                }

                totalGlobal += sousTotal;

                // Image
                ImageIcon icon;
                if (a.getImagePath() == null || a.getImagePath().isEmpty()) {
                    icon = new ImageIcon();
                } else {
                    String cleanedPath = a.getImagePath().replaceAll("^/+", "").replaceAll("\"", "").trim();
                    File file = new File("Images", new File(cleanedPath).getName());
                    icon = file.exists() ? new ImageIcon(file.getAbsolutePath()) : new ImageIcon();
                }

                Image img = icon.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                imageLabel.setAlignmentY(Component.TOP_ALIGNMENT);

                JLabel texte = new JLabel(prixTexte);
                texte.setFont(new Font("Arial", Font.PLAIN, 14));
                texte.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

                JPanel lignePanel = new JPanel(new BorderLayout());
                lignePanel.setBackground(Color.WHITE);
                lignePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(5, 10, 5, 10),
                        BorderFactory.createLineBorder(new Color(220, 220, 220))
                ));

                JPanel contenu = new JPanel();
                contenu.setLayout(new BoxLayout(contenu, BoxLayout.X_AXIS));
                contenu.setBackground(Color.WHITE);
                contenu.add(imageLabel);
                contenu.add(Box.createHorizontalStrut(10));
                contenu.add(texte);

                JButton supprimerBtn = new JButton("Supprimer");
                supprimerBtn.setFont(new Font("Arial", Font.PLAIN, 12));
                supprimerBtn.setBackground(new Color(255, 230, 230));
                supprimerBtn.setFocusPainted(false);

                supprimerBtn.addActionListener(e -> {
                    parent.getPanier().supprimerArticle(a);
                    mettreAJourPanier(parent);
                });

                JPanel panneauSupp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                panneauSupp.setBackground(Color.WHITE);
                panneauSupp.add(supprimerBtn);

                lignePanel.add(contenu, BorderLayout.CENTER);
                lignePanel.add(panneauSupp, BorderLayout.SOUTH);

                panelArticles.add(Box.createVerticalStrut(8));
                panelArticles.add(lignePanel);
            }
        }

        totalLabel.setText("💰 Total : " + String.format("%.2f", totalGlobal) + " €");
        panelArticles.revalidate();
        panelArticles.repaint();
    }
}
