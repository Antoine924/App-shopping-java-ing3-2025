package model;


public class LigneCommande {
    private int id;
    private int idCommande;
    private int idArticle;
    private int quantite;

    public LigneCommande(int id, int idCommande, int idArticle, int quantite) {
        this.id = id;
        this.idCommande = idCommande;
        this.idArticle = idArticle;
        this.quantite = quantite;
    }

    public LigneCommande(int idCommande, int idArticle, int quantite) {
        this.idCommande = idCommande;
        this.idArticle = idArticle;
        this.quantite = quantite;
    }

    // Getters
    public int getId() { return id; }
    public int getIdCommande() { return idCommande; }
    public int getIdArticle() { return idArticle; }
    public int getQuantite() { return quantite; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    // Permet d'afficher la ligne de commande facilement
    @Override
    public String toString() {
        return "LigneCommande{" +
                "id=" + id +
                ", idCommande=" + idCommande +
                ", idArticle=" + idArticle +
                ", quantite=" + quantite +
                '}';
    }
}

