package view;

import DAO.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueInscription extends JPanel {
    public VueInscription(FenetrePrincipale parent) {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(400, 300));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel titleLabel = new JLabel("📝 Création de compte");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(59, 89, 152));

        JLabel nomLabel = new JLabel("Nom :");
        JTextField nomField = new JTextField(15);

        JLabel emailLabel = new JLabel("Email :");
        JTextField emailField = new JTextField(15);

        JLabel mdpLabel = new JLabel("Mot de passe :");
        JPasswordField mdpField = new JPasswordField(15);

        JButton inscriptionButton = new JButton("S'inscrire");
        JButton retourButton = new JButton("Retour");

        inscriptionButton.setBackground(new Color(59, 89, 152));
        inscriptionButton.setForeground(Color.WHITE);
        inscriptionButton.setFocusPainted(false);

        retourButton.setBackground(Color.LIGHT_GRAY);
        retourButton.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridy++; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(nomLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(mdpLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(mdpField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(inscriptionButton, gbc);

        gbc.gridy++;
        formPanel.add(retourButton, gbc);

        add(formPanel);

        inscriptionButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = new String(mdpField.getPassword()).trim();

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            Client nouveau = new Client(nom, email, mdp, "standard");
            new ClientDAO().ajouterClient(nouveau);

            JOptionPane.showMessageDialog(this, "Compte créé avec succès !");
            parent.afficherPage(FenetrePrincipale.PAGE_CONNEXION);
        });

        retourButton.addActionListener(e -> parent.afficherPage(FenetrePrincipale.PAGE_CONNEXION));
    }
}
