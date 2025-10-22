package view;

import DAO.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VueConnexion extends JPanel {
    public VueConnexion(FenetrePrincipale parent) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Formulaire de connexion
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(350, 250));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel titleLabel = new JLabel("🔐 Connexion à l'espace client");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(59, 89, 152));

        JLabel emailLabel = new JLabel("Email :");
        JTextField emailField = new JTextField(15);

        JLabel mdpLabel = new JLabel("Mot de passe :");
        JPasswordField mdpField = new JPasswordField(15);

        JButton connexionButton = new JButton("Connexion");
        JButton inscriptionBtn = new JButton("Créer un compte");

        connexionButton.setBackground(new Color(59, 89, 152));
        connexionButton.setForeground(Color.WHITE);
        connexionButton.setFocusPainted(false);

        inscriptionBtn.setBackground(Color.LIGHT_GRAY);
        inscriptionBtn.setFocusPainted(false);

        // Placement des éléments dans le formulaire
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(mdpLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(mdpField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(connexionButton, gbc);

        gbc.gridy++;
        formPanel.add(inscriptionBtn, gbc);

        add(formPanel);

        // Action lors du clic sur "Connexion"
        connexionButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText().trim();
            String mdp = new String(mdpField.getPassword()).trim();

            ClientDAO dao = new ClientDAO();
            List<Client> clients = dao.listerClients();

            for (Client c : clients) {
                if (c.getEmail().equalsIgnoreCase(email) && c.getMotDePasse().equals(mdp)) {
                    parent.setClientConnecte(c);

                    // Si administrateur, ouvrir menu admin
                    if (c.getTypeClient().equalsIgnoreCase("admin")) {
                        JOptionPane.showMessageDialog(this, "Bienvenue administrateur");
                        SwingUtilities.getWindowAncestor(this).dispose();
                        new Menuadminvue().setVisible(true);
                    } else {
                        parent.afficherPage(FenetrePrincipale.PAGE_CATALOGUE);
                    }
                    return;
                }
            }

            // Si connexion échouée
            JOptionPane.showMessageDialog(this, "Identifiants incorrects.");
        });

        // Action lors du clic sur "Créer un compte"
        inscriptionBtn.addActionListener(e -> parent.afficherPage(FenetrePrincipale.PAGE_INSCRIPTION));
    }
}
