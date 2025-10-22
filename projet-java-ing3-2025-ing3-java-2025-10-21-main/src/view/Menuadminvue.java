package view;

import javax.swing.*;
import java.awt.*;

// Vue principale du menu administrateur
public class Menuadminvue extends JFrame {

    public Menuadminvue() {
        super("Menu Administrateur"); // Titre de la fenêtre

        setSize(600, 450); // Taille de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Quitte l'application à la fermeture
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran
        getContentPane().setBackground(new Color(245, 248, 255)); // Couleur de fond
        setLayout(new BorderLayout()); // Utilisation d'un BorderLayout

        // Création du titre
        JLabel titre = new JLabel("Espace Administrateur", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titre, BorderLayout.NORTH); // Ajout du titre en haut

        // Panneau central pour les boutons
        JPanel centre = new JPanel();
        centre.setLayout(new GridBagLayout()); // Placement précis des boutons
        centre.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Espacement vertical uniforme

        // Ajout des différents boutons avec leurs actions
        gbc.gridy = 0;
        centre.add(creerBouton("Gestion des articles", () -> new Gestionarticleview().setVisible(true)), gbc);

        gbc.gridy++;
        centre.add(creerBouton("Gestion des clients", () -> new Gestionclientvue().setVisible(true)), gbc);

        gbc.gridy++;
        centre.add(creerBouton("Historique des commandes", () -> new Gestioncommandevue().setVisible(true)), gbc);

        gbc.gridy++;
        centre.add(creerBouton("Statistiques", () -> new Statsvue().setVisible(true)), gbc);

        gbc.gridy++;
        centre.add(creerBouton("Déconnexion", () -> {
            dispose(); // Ferme la fenêtre actuelle
            new FenetrePrincipale().setVisible(true); // Retour à la page principale
        }), gbc);

        add(centre, BorderLayout.CENTER); // Ajout du panneau central
    }

    // Méthode utilitaire pour créer un bouton avec du style et une action
    private JButton creerBouton(String texte, Runnable action) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        bouton.setFocusPainted(false);
        bouton.setBackground(new Color(230, 240, 255));
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setMaximumSize(new Dimension(250, 45));
        bouton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230), 1), // Bordure fine bleue claire
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Espacement interne
        ));
        bouton.addActionListener(e -> action.run()); // Action associée au clic
        return bouton;
    }

    // Méthode principale pour tester la fenêtre seule
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menuadminvue().setVisible(true));
    }
}
