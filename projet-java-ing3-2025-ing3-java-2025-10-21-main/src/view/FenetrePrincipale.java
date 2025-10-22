package view;

import model.Client;
import model.Panier;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FenetrePrincipale extends JFrame {
    private CardLayout cardLayout;
    private JPanel panneauPrincipal;

    public static final String PAGE_CONNEXION = "connexion";
    public static final String PAGE_CATALOGUE = "catalogue";
    public static final String PAGE_PANIER = "panier";
    public static final String PAGE_HISTORIQUE = "historique";
    public static final String PAGE_INSCRIPTION = "inscription";

    private Client clientConnecte;
    private Panier panier;

    public FenetrePrincipale() {
        setTitle("Shopping App");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panneauPrincipal = new JPanel(cardLayout);
        panier = new Panier();

        // Ajout des différentes vues
        panneauPrincipal.add(new VueConnexion(this), PAGE_CONNEXION);
        panneauPrincipal.add(new VueCatalogue(this), PAGE_CATALOGUE);
        panneauPrincipal.add(new VuePanier(this), PAGE_PANIER);
        panneauPrincipal.add(new VueHistorique(this), PAGE_HISTORIQUE);
        panneauPrincipal.add(new VueInscription(this), PAGE_INSCRIPTION);

        setContentPane(panneauPrincipal);
        afficherPage(PAGE_CONNEXION);
    }

    public void afficherPage(String nomPage) {
        // Mise à jour des pages dynamiques avant affichage
        if (nomPage.equals(PAGE_PANIER)) {
            VuePanier vue = (VuePanier) panneauPrincipal.getComponent(2);
            vue.mettreAJourPanier(this);
        } else if (nomPage.equals(PAGE_HISTORIQUE)) {
            VueHistorique vue = (VueHistorique) panneauPrincipal.getComponent(3);
            vue.mettreAJourHistorique(this);
        } else if (nomPage.equals(PAGE_CATALOGUE)) {
            VueCatalogue vue = (VueCatalogue) panneauPrincipal.getComponent(1);
            vue.rafraichirCatalogue();
        }
        cardLayout.show(panneauPrincipal, nomPage);
    }

    public void setClientConnecte(Client client) {
        this.clientConnecte = client;
    }

    public Client getClientConnecte() {
        return clientConnecte;
    }

    public Panier getPanier() {
        return panier;
    }

    // Liste des commandes passées pendant la session
    private List<Integer> commandesSession = new ArrayList<>();

    public List<Integer> getCommandesSession() {
        return commandesSession;
    }

    public void ajouterCommandeSession(int idCommande) {
        commandesSession.add(idCommande);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetrePrincipale().setVisible(true));
    }
}
