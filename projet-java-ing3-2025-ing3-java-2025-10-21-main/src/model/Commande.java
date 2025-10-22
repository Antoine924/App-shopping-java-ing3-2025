package model;

import java.time.LocalDateTime;
import java.util.List;
import model.LigneCommande;

/**
 * Représente une commande effectuée par un client.
 * Une commande contient une date, un client associé et éventuellement des lignes de commande (articles achetés).
 */
public class Commande {

    private int id;
    private int idClient;
    private LocalDateTime dateCommande;
    private List<LigneCommande> lignes;

    // === Constructeurs ===

    /**
     * Constructeur complet utilisé lorsqu'on récupère une commande existante depuis la base de données.
     */
    public Commande(int id, int idClient, LocalDateTime dateCommande) {
        this.id = id;
        this.idClient = idClient;
        this.dateCommande = dateCommande;
    }

    /**
     * Constructeur utilisé lors de la création d'une nouvelle commande simple sans lignes détaillées.
     * La date est automatiquement fixée à la date et l'heure actuelle.
     */
    public Commande(int idClient) {
        this.idClient = idClient;
        this.dateCommande = LocalDateTime.now();
    }

    /**
     * Constructeur utilisé pour créer une commande avec directement une liste de lignes de commande.
     * Pratique lorsque l'on valide un panier contenant plusieurs articles.
     */
    public Commande(int idClient, List<LigneCommande> lignes) {
        this.idClient = idClient;
        this.lignes = lignes;
        this.dateCommande = LocalDateTime.now();
    }

    // === Getters ===

    public int getId() { return id; }

    public int getIdClient() { return idClient; }

    public LocalDateTime getDateCommande() { return dateCommande; }

    public List<LigneCommande> getLignes() { return lignes; }

    // === Setters ===

    public void setId(int id) { this.id = id; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }

    /**
     * Retourne une description textuelle de la commande.
     */
    @Override // On utilise @Override car on modifie la version de base de la méthode toString
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", dateCommande=" + dateCommande +
                '}';
    }
}
