package view;

import DAO.ArticleDAO;
import model.Article;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class VueCatalogue extends JPanel {
    private FenetrePrincipale fenetrePrincipale;
    private JPanel panelArticles; // Panneau pour afficher tous les articles

    // Classe utilitaire pour générer des images par défaut
    public static class ImageUtils {
        public static ImageIcon creerImageParDefaut(int largeur, int hauteur, String texte) {
            BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, largeur, hauteur);

            g.setColor(Color.RED);
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g.getFontMetrics();
            int x = (largeur - fm.stringWidth(texte)) / 2;
            int y = (hauteur - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(texte, x, y);

            g.dispose();
            return new ImageIcon(image);
        }
    }

    public VueCatalogue(FenetrePrincipale fenetrePrincipale) {
        this.fenetrePrincipale = fenetrePrincipale;
        this.setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Bandeau du haut (titre + bouton historique)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));

        JLabel titre = new JLabel(" Catalogue produits", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        titre.setForeground(new Color(59, 89, 152));
        topPanel.add(titre, BorderLayout.CENTER);

        JButton historiqueBtn = new JButton(" Récapitulatif des commandes");
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

        // Panneau central pour les cartes des articles
        panelArticles = new JPanel(new GridLayout(0, 2, 15, 15));
        panelArticles.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panelArticles.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(panelArticles);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Bas de page : boutons voir panier et déconnexion
        JButton panierBtn = new JButton("Voir le panier");
        panierBtn.setBackground(new Color(59, 89, 152));
        panierBtn.setForeground(Color.WHITE);
        panierBtn.setFocusPainted(false);
        panierBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        panierBtn.setPreferredSize(new Dimension(160, 35));
        panierBtn.addActionListener(e -> fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_PANIER));

        JButton deconnexionBtn = new JButton("Déconnexion");
        deconnexionBtn.setBackground(Color.LIGHT_GRAY);
        deconnexionBtn.setForeground(Color.BLACK);
        deconnexionBtn.setFocusPainted(false);
        deconnexionBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        deconnexionBtn.setPreferredSize(new Dimension(160, 35));
        deconnexionBtn.addActionListener(e -> {
            fenetrePrincipale.setClientConnecte(null);
            fenetrePrincipale.getPanier().vider();
            fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_CONNEXION);
        });

        JPanel bas = new JPanel();
        bas.setBackground(new Color(245, 245, 245));
        bas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bas.add(panierBtn);
        bas.add(deconnexionBtn);

        add(bas, BorderLayout.SOUTH);

        // Chargement initial du catalogue
        rafraichirCatalogue();
    }

    // Méthode pour recharger les articles dans le catalogue
    public void rafraichirCatalogue() {
        panelArticles.removeAll();

        List<Article> articles = new ArticleDAO().listerArticles();

        for (Article article : articles) {
            JPanel carte = new JPanel(new BorderLayout());
            carte.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            carte.setBackground(Color.WHITE);
            carte.setPreferredSize(new Dimension(200, 180));

            // Chargement de l'image de l'article
            String imagePath = article.getImagePath();
            ImageIcon icon;

            if (imagePath == null || imagePath.trim().isEmpty()) {
                icon = ImageUtils.creerImageParDefaut(120, 90, "Image manquante");
            } else {
                String cleanedPath = imagePath.replaceAll("^/+", "").replaceAll("\"", "").trim();
                File file = new File("Images", new File(cleanedPath).getName());

                if (file.exists()) {
                    icon = new ImageIcon(file.getAbsolutePath());
                } else {
                    icon = ImageUtils.creerImageParDefaut(120, 90, "Introuvable");
                }
            }

            Image scaledImg = icon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(120, 90));

            // Nom et prix de l'article
            JLabel nom = new JLabel(article.getNom(), SwingConstants.CENTER);
            nom.setFont(new Font("SansSerif", Font.BOLD, 14));
            nom.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

            JLabel prix = new JLabel(article.getPrixUnitaire() + " €", SwingConstants.CENTER);
            prix.setFont(new Font("SansSerif", Font.PLAIN, 13));

            // Panel pour ajuster la quantité
            JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            quantitePanel.setBackground(Color.WHITE);

            JButton moinsBtn = new JButton("-");
            moinsBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
            moinsBtn.setPreferredSize(new Dimension(45, 28));

            JButton plusBtn = new JButton("+");
            plusBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
            plusBtn.setPreferredSize(new Dimension(45, 28));

            JLabel quantiteLabel = new JLabel("1");
            quantiteLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

            final int[] quantite = {1};
            moinsBtn.addActionListener(e -> {
                if (quantite[0] > 1) {
                    quantite[0]--;
                    quantiteLabel.setText(String.valueOf(quantite[0]));
                }
            });
            plusBtn.addActionListener(e -> {
                quantite[0]++;
                quantiteLabel.setText(String.valueOf(quantite[0]));
            });

            quantitePanel.add(moinsBtn);
            quantitePanel.add(quantiteLabel);
            quantitePanel.add(plusBtn);

            // Bouton ajouter au panier
            JButton ajouterBtn = new JButton("Ajouter au panier");
            ajouterBtn.setBackground(new Color(59, 89, 152));
            ajouterBtn.setForeground(Color.WHITE);
            ajouterBtn.setFocusPainted(false);
            ajouterBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            ajouterBtn.addActionListener(e -> {
                fenetrePrincipale.getPanier().ajouterArticle(article, quantite[0]);
                JOptionPane.showMessageDialog(this, "Article ajouté au panier (" + quantite[0] + "x).");
            });

            // Mise en page des infos de l'article
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.add(nom);
            infoPanel.add(prix);
            infoPanel.add(quantitePanel);
            infoPanel.add(ajouterBtn);

            carte.add(imageLabel, BorderLayout.NORTH);
            carte.add(infoPanel, BorderLayout.CENTER);

            panelArticles.add(carte);
        }

        panelArticles.revalidate();
        panelArticles.repaint();
    }
}
