package view;

import DAO.ClientDAO;
import DAO.CommandeDAO;
import DAO.LigneCommandeDAO;
import model.Client;
import model.Commande;
import model.LigneCommande;
import controller.ClientController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

// Classe qui gère l'interface de consultation des commandes passées
public class Gestioncommandevue extends JFrame {
    // Déclaration des composants de l'interface et des modèles de données
    private JTable tableCommandes, tableLignes;
    private DefaultTableModel modeleCommandes, modeleLignes;
    private CommandeDAO commandeDAO;
    private LigneCommandeDAO ligneCommandeDAO;
    private ClientDAO clientDAO;

    public Gestioncommandevue() {
        super("Historique des commandes"); // Titre de la fenêtre

        // Initialisation des DAO pour accéder à la base de données
        commandeDAO = new CommandeDAO();
        ligneCommandeDAO = new LigneCommandeDAO();
        clientDAO = new ClientDAO();

        // Configuration générale de la fenêtre
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Quitter l'application quand on ferme
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

        // Création des modèles de table (colonnes)
        modeleCommandes = new DefaultTableModel(new String[]{"ID", "Client", "Date"}, 0);
        modeleLignes = new DefaultTableModel(new String[]{"ID ligne", "ID Article", "Quantité"}, 0);

        // Création des tables elles-mêmes
        tableCommandes = new JTable(modeleCommandes);
        tableLignes = new JTable(modeleLignes);

        // Lorsque l'utilisateur clique sur une commande, on affiche ses détails
        tableCommandes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Pour éviter de traiter deux fois l'événement
                int ligne = tableCommandes.getSelectedRow(); // Récupère la ligne sélectionnée
                if (ligne != -1) { // Vérifie qu'une ligne est bien sélectionnée
                    int idCommande = (int) modeleCommandes.getValueAt(ligne, 0); // Récupère l'ID de la commande
                    chargerLignesCommande(idCommande); // Charge les articles liés à cette commande
                }
            }
        });

        // Création des panneaux haut et bas pour l'affichage
        JPanel panneauHaut = new JPanel(new BorderLayout());
        panneauHaut.setBorder(BorderFactory.createTitledBorder("Commandes")); // Bordure avec titre
        panneauHaut.add(new JScrollPane(tableCommandes), BorderLayout.CENTER); // Scroll automatique si besoin

        JPanel panneauBas = new JPanel(new BorderLayout());
        panneauBas.setBorder(BorderFactory.createTitledBorder("Détails de la commande"));
        panneauBas.add(new JScrollPane(tableLignes), BorderLayout.CENTER);

        // Boutons d'action
        JButton boutonRafraichir = new JButton("Rafraîchir"); // recharge la liste
        JButton boutonRetour = new JButton("↩ Retour au menu"); // retourne au menu principal
        JButton boutonExporter = new JButton("Exporter facture (.txt)"); // génère un fichier facture

        // Action : Rafraîchir la liste des commandes
        boutonRafraichir.addActionListener(e -> chargerCommandes());

        // Action : Retourner au menu administrateur
        boutonRetour.addActionListener(e -> {
            dispose(); // Ferme cette fenêtre
            new Menuadminvue().setVisible(true); // Réouvre le menu
        });

        // Action : Exporter une facture pour la commande sélectionnée
        boutonExporter.addActionListener(e -> exporterFacture());

        // Panneau pour les boutons en bas de la fenêtre
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.add(boutonRafraichir);
        panneauBoutons.add(boutonExporter);
        panneauBoutons.add(boutonRetour);

        // Assemblage général de la fenêtre
        getContentPane().add(panneauHaut, BorderLayout.NORTH); // Liste commandes en haut
        getContentPane().add(panneauBas, BorderLayout.CENTER); // Détails de la commande au centre
        getContentPane().add(panneauBoutons, BorderLayout.SOUTH); // Boutons en bas

        chargerCommandes(); // Chargement initial des commandes à l'ouverture
    }

    // Méthode pour charger toutes les commandes de la base dans la table
    private void chargerCommandes() {
        modeleCommandes.setRowCount(0); // Vide la table avant de re-remplir

        List<Commande> commandes = commandeDAO.listerCommandes(); // Récupère toutes les commandes

        for (Commande c : commandes) {
            // Pour chaque commande, récupérer son client associé
            Client client = clientDAO.listerClients().stream()
                    .filter(cl -> cl.getId() == c.getIdClient()) // On cherche par ID
                    .findFirst()
                    .orElse(new Client("Inconnu", "inconnu@mail.com")); // Si le client n'existe pas en base

            // Ajoute une ligne dans la table Commandes
            modeleCommandes.addRow(new Object[]{
                    c.getId(), // ID de la commande
                    client.getNom() + " (" + client.getEmail() + ")", // Affiche "Nom (email)"
                    c.getDateCommande().toString() // Date de la commande
            });
        }

        modeleLignes.setRowCount(0); // Vide les lignes de détails (car on recharge tout)
    }

    // Méthode pour charger les lignes d'une commande précise
    private void chargerLignesCommande(int idCommande) {
        modeleLignes.setRowCount(0); // Vide la table des lignes

        List<LigneCommande> lignes = ligneCommandeDAO.listerLignesParCommande(idCommande); // Récupère les articles achetés

        for (LigneCommande l : lignes) {
            modeleLignes.addRow(new Object[]{
                    l.getId(), l.getIdArticle(), l.getQuantite() // ID de la ligne, ID de l'article, quantité achetée
            });
        }
    }

    // Méthode pour exporter la facture d'une commande vers un fichier texte
    private void exporterFacture() {
        int ligne = tableCommandes.getSelectedRow(); // Récupère la ligne sélectionnée
        if (ligne == -1) { // Si aucune ligne sélectionnée
            JOptionPane.showMessageDialog(this, "❗ Sélectionne une commande.");
            return;
        }

        // Récupère les informations nécessaires à l'export
        int idCommande = (int) modeleCommandes.getValueAt(ligne, 0);
        String clientInfo = (String) modeleCommandes.getValueAt(ligne, 1);
        String date = (String) modeleCommandes.getValueAt(ligne, 2);

        List<LigneCommande> lignes = ligneCommandeDAO.listerLignesParCommande(idCommande); // Articles achetés

        try (PrintWriter writer = new PrintWriter(new FileWriter("facture_commande_" + idCommande + ".txt"))) {
            // Ecriture du fichier facture
            writer.println("📄 FACTURE COMMANDE #" + idCommande);
            writer.println("Client : " + clientInfo);
            writer.println("Date   : " + date);
            writer.println("\nArticles :");
            for (LigneCommande ligneCmd : lignes) {
                writer.println("- Article ID " + ligneCmd.getIdArticle() + " x" + ligneCmd.getQuantite());
            }
            writer.println("\nMerci pour votre achat !");
            // Message succès
            JOptionPane.showMessageDialog(this, "✅ Facture exportée !");
        } catch (Exception e) {
            // Message erreur si échec
            JOptionPane.showMessageDialog(this, "❌ Erreur lors de l'export.");
        }
    }

    // Point d'entrée pour tester la fenêtre seule
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gestioncommandevue().setVisible(true));
    }
}
