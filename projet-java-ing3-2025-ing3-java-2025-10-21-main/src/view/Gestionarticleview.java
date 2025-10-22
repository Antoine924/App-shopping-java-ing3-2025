package view;

import controller.ArticleController;
import model.Article;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.io.File;
import java.nio.file.*;

// Classe de gestion des articles (ajout, modification, suppression)
public class Gestionarticleview extends JFrame {
    private JTable tableArticles; // Tableau pour afficher les articles
    private DefaultTableModel modeleTable; // Modèle de données pour remplir la table
    private JTextField champNom, champMarque, champPrixUnitaire, champPrixGros, champSeuilGros, champImagePath; // Champs de saisie
    private ArticleController articleController; // Contrôleur pour accéder aux méthodes métiers

    public Gestionarticleview() {
        super("Gestion des articles");

        articleController = new ArticleController(); // Instanciation du contrôleur

        // Configuration de base de la fenêtre
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation du tableau avec les colonnes
        modeleTable = new DefaultTableModel(new String[]{"ID", "Nom", "Marque", "Prix unit.", "Prix gros", "Seuil", "Image"}, 0);
        tableArticles = new JTable(modeleTable);
        JScrollPane scrollPane = new JScrollPane(tableArticles);

        // Lorsqu'on clique sur un article du tableau, remplir le formulaire
        tableArticles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int ligne = tableArticles.getSelectedRow();
                champNom.setText(modeleTable.getValueAt(ligne, 1).toString());
                champMarque.setText(modeleTable.getValueAt(ligne, 2).toString());
                champPrixUnitaire.setText(modeleTable.getValueAt(ligne, 3).toString());
                champPrixGros.setText(modeleTable.getValueAt(ligne, 4).toString());
                champSeuilGros.setText(modeleTable.getValueAt(ligne, 5).toString());
                champImagePath.setText(modeleTable.getValueAt(ligne, 6).toString());
            }
        });

        // Création des champs de saisie et bouton pour choisir une image
        JPanel panneauFormulaire = new JPanel(new GridLayout(3, 5));
        champNom = new JTextField();
        champMarque = new JTextField();
        champPrixUnitaire = new JTextField();
        champPrixGros = new JTextField();
        champSeuilGros = new JTextField();
        champImagePath = new JTextField();
        JButton boutonChoisirImage = new JButton("Choisir une image");

        // Action du bouton "Choisir une image"
        boutonChoisirImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(); // Ouvre un sélecteur de fichiers
            int retour = fileChooser.showOpenDialog(this);
            if (retour == JFileChooser.APPROVE_OPTION) { // Si un fichier est sélectionné
                File fichier = fileChooser.getSelectedFile();
                champImagePath.setText("/Images/" + fichier.getName());
                try {
                    // Copie du fichier sélectionné vers le dossier Images du projet
                    Path destination = Paths.get("Images", fichier.getName());
                    Files.copy(fichier.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur de copie : " + ex.getMessage());
                }
            }
        });

        // Placement des champs et boutons dans le panneau
        panneauFormulaire.add(new JLabel("Nom"));
        panneauFormulaire.add(new JLabel("Marque"));
        panneauFormulaire.add(new JLabel("Prix unitaire"));
        panneauFormulaire.add(new JLabel("Prix gros"));
        panneauFormulaire.add(new JLabel("Seuil gros"));
        panneauFormulaire.add(champNom);
        panneauFormulaire.add(champMarque);
        panneauFormulaire.add(champPrixUnitaire);
        panneauFormulaire.add(champPrixGros);
        panneauFormulaire.add(champSeuilGros);
        panneauFormulaire.add(new JLabel("Image"));
        panneauFormulaire.add(champImagePath);
        panneauFormulaire.add(boutonChoisirImage);

        // Création des boutons d'actions
        JPanel panneauBoutons = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");
        JButton boutonRafraichir = new JButton("Rafraîchir");
        JButton boutonRetour = new JButton("↩ Retour au menu");

        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonModifier);
        panneauBoutons.add(boutonSupprimer);
        panneauBoutons.add(boutonRafraichir);
        panneauBoutons.add(boutonRetour);

        // Action bouton Ajouter
        boutonAjouter.addActionListener(e -> {
            if (champsValides()) { // Vérifie que tout est rempli
                try {
                    String nom = champNom.getText();
                    String marque = champMarque.getText();
                    double prixUnitaire = Double.parseDouble(champPrixUnitaire.getText());
                    double prixGros = Double.parseDouble(champPrixGros.getText());
                    int seuil = Integer.parseInt(champSeuilGros.getText());
                    String imagePath = champImagePath.getText();

                    Article a = new Article(nom, marque, prixUnitaire, prixGros, seuil);
                    a.getClass().getMethod("setImagePath", String.class).invoke(a, imagePath); // Définit l'image

                    articleController.ajouterArticle(a); // Ajoute en base
                    chargerArticles(); // Recharge la liste
                    viderChamps(); // Vide le formulaire
                    JOptionPane.showMessageDialog(this, "✅ Article ajouté !");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Erreur : " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "❗ Veuillez remplir tous les champs.");
            }
        });

        // Action bouton Modifier
        boutonModifier.addActionListener(e -> {
            int ligne = tableArticles.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ Sélectionnez un article à modifier.");
                return;
            }

            if (champsValides()) {
                try {
                    int id = (int) modeleTable.getValueAt(ligne, 0);
                    String nom = champNom.getText();
                    String marque = champMarque.getText();
                    double prixUnitaire = Double.parseDouble(champPrixUnitaire.getText());
                    double prixGros = Double.parseDouble(champPrixGros.getText());
                    int seuil = Integer.parseInt(champSeuilGros.getText());
                    String imagePath = champImagePath.getText();

                    Article a = new Article(nom, marque, prixUnitaire, prixGros, seuil);
                    a.setId(id);
                    a.getClass().getMethod("setImagePath", String.class).invoke(a, imagePath);

                    articleController.modifierArticle(a); // Mise à jour en base
                    chargerArticles();
                    viderChamps();
                    JOptionPane.showMessageDialog(this, "Article modifié !");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            }
        });

        // Action bouton Supprimer
        boutonSupprimer.addActionListener(e -> {
            int ligne = tableArticles.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un article à supprimer.");
                return;
            }
            int id = (int) modeleTable.getValueAt(ligne, 0);
            articleController.supprimerArticle(id); // Supprime en base
            chargerArticles();
            viderChamps();
            JOptionPane.showMessageDialog(this, "Article supprimé !");
        });

        // Action bouton Rafraîchir
        boutonRafraichir.addActionListener(e -> {
            chargerArticles();
            viderChamps();
        });

        // Action bouton Retour
        boutonRetour.addActionListener(e -> {
            dispose();
            new Menuadminvue().setVisible(true);
        });

        // Placement des panneaux dans la fenêtre
        getContentPane().add(scrollPane, BorderLayout.NORTH); // Tableau en haut
        getContentPane().add(panneauFormulaire, BorderLayout.CENTER); // Formulaire au centre
        getContentPane().add(panneauBoutons, BorderLayout.SOUTH); // Boutons en bas

        chargerArticles(); // Chargement initial des articles
    }

    // Vérifie si tous les champs sont remplis
    private boolean champsValides() {
        return !champNom.getText().isEmpty() &&
                !champMarque.getText().isEmpty() &&
                !champPrixUnitaire.getText().isEmpty() &&
                !champPrixGros.getText().isEmpty() &&
                !champSeuilGros.getText().isEmpty() &&
                !champImagePath.getText().isEmpty();
    }

    // Charge les articles dans le tableau
    private void chargerArticles() {
        modeleTable.setRowCount(0); // Vide le tableau
        List<Article> articles = articleController.listerArticles(); // Récupère les articles
        for (Article a : articles) {
            String imagePath = "";
            try {
                imagePath = (String) a.getClass().getMethod("getImagePath").invoke(a);
            } catch (Exception ignored) {} // Si pas d'image, on ignore
            modeleTable.addRow(new Object[]{
                    a.getId(), a.getNom(), a.getMarque(),
                    a.getPrixUnitaire(), a.getPrixGros(), a.getSeuilGros(), imagePath
            });
        }
    }

    // Vide tous les champs de saisie
    private void viderChamps() {
        champNom.setText("");
        champMarque.setText("");
        champPrixUnitaire.setText("");
        champPrixGros.setText("");
        champSeuilGros.setText("");
        champImagePath.setText("");
    }

    // Lancement de la fenêtre seule
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gestionarticleview().setVisible(true));
    }
}
