package view;

import DAO.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Vue pour gérer les clients : ajout, modification, suppression
public class Gestionclientvue extends JFrame {
    private JTable tableClients; // Tableau affichant les clients
    private DefaultTableModel modeleTable; // Modèle pour remplir le tableau
    private JTextField champNom, champEmail, champMotDePasse; // Champs de saisie
    private JComboBox<String> comboTypeClient; // Choix du type de client (standard/admin)
    private ClientDAO clientDAO; // Accès aux données client en base

    public Gestionclientvue() {
        super("Gestion des clients");

        clientDAO = new ClientDAO(); // Instancie le DAO

        setSize(800, 600); // Taille de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre

        // Initialisation du modèle du tableau avec les colonnes
        modeleTable = new DefaultTableModel(new String[]{"ID", "Nom", "Email", "Type"}, 0);
        tableClients = new JTable(modeleTable);
        JScrollPane scrollPane = new JScrollPane(tableClients); // Permet un scroll si beaucoup de clients

        // Lorsqu'on clique sur un client dans la table, remplissage des champs du formulaire
        tableClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int ligne = tableClients.getSelectedRow(); // Ligne cliquée
                champNom.setText(modeleTable.getValueAt(ligne, 1).toString());
                champEmail.setText(modeleTable.getValueAt(ligne, 2).toString());
                comboTypeClient.setSelectedItem(modeleTable.getValueAt(ligne, 3).toString());
            }
        });

        // Champs de saisie
        champNom = new JTextField();
        champEmail = new JTextField();
        champMotDePasse = new JTextField();
        comboTypeClient = new JComboBox<>(new String[]{"standard", "admin"}); // 2 types possibles

        // Formulaire de saisie
        JPanel panneauFormulaire = new JPanel(new GridLayout(4, 2)); // 4 lignes x 2 colonnes
        panneauFormulaire.add(new JLabel("Nom"));
        panneauFormulaire.add(champNom);
        panneauFormulaire.add(new JLabel("Email"));
        panneauFormulaire.add(champEmail);
        panneauFormulaire.add(new JLabel("Mot de passe"));
        panneauFormulaire.add(champMotDePasse);
        panneauFormulaire.add(new JLabel("Type de client"));
        panneauFormulaire.add(comboTypeClient);

        // Boutons d'actions
        JPanel panneauBoutons = new JPanel();
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");
        JButton boutonRafraichir = new JButton("Rafraîchir");
        JButton boutonRetour = new JButton("↩ Retour au menu");

        // Ajout des boutons au panneau
        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonModifier);
        panneauBoutons.add(boutonSupprimer);
        panneauBoutons.add(boutonRafraichir);
        panneauBoutons.add(boutonRetour);

        // Action bouton Ajouter
        boutonAjouter.addActionListener(e -> {
            if (champsValides()) { // Vérifie que tout est rempli
                try {
                    Client client = new Client(
                            champNom.getText(),
                            champEmail.getText(),
                            champMotDePasse.getText(),
                            comboTypeClient.getSelectedItem().toString()
                    );
                    clientDAO.ajouterClient(client); // Ajoute en base
                    chargerClients(); // Recharge la liste
                    viderChamps(); // Vide le formulaire
                    JOptionPane.showMessageDialog(this, "✅ Client ajouté !");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Erreur lors de l'ajout.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "❗ Remplis tous les champs.");
            }
        });

        // Action bouton Modifier
        boutonModifier.addActionListener(e -> {
            int ligne = tableClients.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ Sélectionne un client à modifier.");
                return;
            }

            if (champsValides()) {
                try {
                    int id = (int) modeleTable.getValueAt(ligne, 0); // Récupère l'ID du client sélectionné
                    Client client = new Client(
                            champNom.getText(),
                            champEmail.getText(),
                            champMotDePasse.getText(),
                            comboTypeClient.getSelectedItem().toString()
                    );
                    client.setId(id); // Important : l'ID reste le même
                    clientDAO.modifierClient(client); // Met à jour en base
                    chargerClients(); // Recharge la liste
                    viderChamps(); // Vide le formulaire
                    JOptionPane.showMessageDialog(this, "✏️ Client modifié !");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Erreur lors de la modification.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "❗ Remplis tous les champs.");
            }
        });

        // Action bouton Supprimer
        boutonSupprimer.addActionListener(e -> {
            int ligne = tableClients.getSelectedRow();
            if (ligne == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ Sélectionne un client à supprimer.");
                return;
            }
            int id = (int) modeleTable.getValueAt(ligne, 0);
            clientDAO.supprimerClient(id); // Suppression en base
            chargerClients(); // Recharge la liste
            viderChamps(); // Vide le formulaire
            JOptionPane.showMessageDialog(this, "🗑️ Client supprimé !");
        });

        // Action bouton Rafraîchir
        boutonRafraichir.addActionListener(e -> {
            chargerClients(); // Recharge la liste
            viderChamps(); // Vide le formulaire
        });

        // Action bouton Retour
        boutonRetour.addActionListener(e -> {
            dispose(); // Ferme cette fenêtre
            new Menuadminvue().setVisible(true); // Ouvre le menu admin
        });

        // Organisation de la fenêtre
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.NORTH); // Tableau en haut
        getContentPane().add(panneauFormulaire, BorderLayout.CENTER); // Formulaire au centre
        getContentPane().add(panneauBoutons, BorderLayout.SOUTH); // Boutons en bas

        chargerClients(); // Charger la liste de clients dès l'ouverture
    }

    // Vérifie que tous les champs obligatoires sont remplis
    private boolean champsValides() {
        return !champNom.getText().isEmpty() &&
                !champEmail.getText().isEmpty() &&
                !champMotDePasse.getText().isEmpty();
    }

    // Recharge la liste des clients depuis la base de données
    private void chargerClients() {
        modeleTable.setRowCount(0); // Vide la table avant de la remplir
        List<Client> clients = clientDAO.listerClients(); // Récupère tous les clients
        for (Client c : clients) {
            modeleTable.addRow(new Object[]{c.getId(), c.getNom(), c.getEmail(), c.getTypeClient()});
        }
    }

    // Vide tous les champs du formulaire
    private void viderChamps() {
        champNom.setText("");
        champEmail.setText("");
        champMotDePasse.setText("");
        comboTypeClient.setSelectedIndex(0); // Remet le type client sur "standard"
    }

    // Point d'entrée pour tester cette vue seule
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gestionclientvue().setVisible(true));
    }
}
