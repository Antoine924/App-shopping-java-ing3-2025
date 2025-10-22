package view;

import DAO.CommandeDAO;
import DAO.LigneCommandeDAO;
import DAO.ArticleDAO;
import model.Client;
import model.Commande;
import model.LigneCommande;
import model.Article;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class VueHistorique extends JPanel {
    private JPanel contenuHistorique;
    private FenetrePrincipale fenetrePrincipale;

    public VueHistorique(FenetrePrincipale parent) {
        this.fenetrePrincipale = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Titre de la page
        JLabel titre = new JLabel("Historique de vos commandes", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titre, BorderLayout.NORTH);

        // Zone centrale : liste des commandes
        contenuHistorique = new JPanel();
        contenuHistorique.setLayout(new BoxLayout(contenuHistorique, BoxLayout.Y_AXIS));
        contenuHistorique.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(contenuHistorique);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Boutons en bas : retour catalogue et déconnexion
        JButton retourBtn = new JButton(" Retour au catalogue");
        retourBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        retourBtn.setPreferredSize(new Dimension(180, 30));
        retourBtn.addActionListener(e -> fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_CATALOGUE));

        JButton deconnexionBtn = new JButton("Déconnexion");
        deconnexionBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        deconnexionBtn.setPreferredSize(new Dimension(160, 30));
        deconnexionBtn.setBackground(Color.LIGHT_GRAY);
        deconnexionBtn.setFocusPainted(false);
        deconnexionBtn.addActionListener(e -> {
            fenetrePrincipale.setClientConnecte(null);
            fenetrePrincipale.getPanier().vider();
            fenetrePrincipale.afficherPage(FenetrePrincipale.PAGE_CONNEXION);
        });

        JPanel bas = new JPanel();
        bas.setBackground(new Color(245, 245, 245));
        bas.add(retourBtn);
        bas.add(deconnexionBtn);
        add(bas, BorderLayout.SOUTH);
    }

    // Mise à jour de l'affichage de l'historique
    public void mettreAJourHistorique(FenetrePrincipale parent) {
        contenuHistorique.removeAll();

        // DAO pour accéder aux données commandes/articles
        CommandeDAO commandeDAO = new CommandeDAO();
        LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
        ArticleDAO articleDAO = new ArticleDAO();

        List<Commande> commandes = commandeDAO.listerCommandes();
        List<Article> tousLesArticles = articleDAO.listerArticles();

        // Création d'une map pour accéder rapidement aux articles
        Map<Integer, Article> articleMap = new HashMap<>();
        for (Article a : tousLesArticles) {
            articleMap.put(a.getId(), a);
        }

        Client client = parent.getClientConnecte();

        // On affiche les commandes les plus récentes en premier
        Collections.reverse(commandes);

        for (Commande c : commandes) {
            if (c.getIdClient() == client.getId()) {
                List<LigneCommande> lignes = ligneDAO.listerLignesParCommande(c.getId());
                if (lignes.isEmpty()) continue;

                // Calcul du total de la commande
                double totalCommande = 0;
                for (LigneCommande ligne : lignes) {
                    Article article = articleMap.get(ligne.getIdArticle());
                    if (article != null) {
                        totalCommande += ligne.getQuantite() * article.getPrixUnitaire();
                    }
                }

                // Affichage d'un résumé de la commande
                JLabel commandeLabel = new JLabel("<html><b>Commande #" + c.getId() + "</b> - " + c.getDateCommande()
                        + "<br>Total : " + String.format("%.2f", totalCommande) + " €</html>");
                commandeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                commandeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
                contenuHistorique.add(commandeLabel);

                // Affichage des articles de la commande
                for (LigneCommande ligne : lignes) {
                    Article article = articleMap.get(ligne.getIdArticle());
                    if (article == null) continue;

                    // Chargement de l'image de l'article
                    ImageIcon icon;
                    String imagePath = article.getImagePath();

                    if (imagePath == null || imagePath.trim().isEmpty()) {
                        icon = new ImageIcon();
                    } else {
                        String cleanedPath = imagePath.replaceAll("^/+", "").replaceAll("\"", "").trim();
                        File file = new File("Images", new File(cleanedPath).getName());

                        if (file.exists()) {
                            icon = new ImageIcon(file.getAbsolutePath());
                        } else {
                            icon = new ImageIcon();
                        }
                    }

                    Image img = icon.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(img));
                    imageLabel.setAlignmentY(Component.TOP_ALIGNMENT);

                    JLabel texte = new JLabel("<html><b>" + article.getNom() + "</b><br>"
                            + ligne.getQuantite() + " x " + article.getPrixUnitaire() + " €</html>");
                    texte.setFont(new Font("Arial", Font.PLAIN, 14));
                    texte.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

                    // Mise en page de chaque ligne article
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
                    contenu.add(texte);

                    lignePanel.add(contenu, BorderLayout.CENTER);

                    contenuHistorique.add(Box.createVerticalStrut(8));
                    contenuHistorique.add(lignePanel);
                }

                contenuHistorique.add(Box.createVerticalStrut(10));
            }
        }

        // Si aucun historique trouvé
        if (contenuHistorique.getComponentCount() == 0) {
            JLabel vide = new JLabel("Aucune commande à afficher.");
            vide.setFont(new Font("Arial", Font.ITALIC, 14));
            vide.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            contenuHistorique.add(vide);
        }

        contenuHistorique.revalidate();
        contenuHistorique.repaint();
    }
}
