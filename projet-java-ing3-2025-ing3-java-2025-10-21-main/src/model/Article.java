package model;

/**
 * Représente un article disponible dans le catalogue.
 * Contient les informations sur le prix, la remise et l'image associée.
 */
public class Article {

    private int id;
    private String nom;
    private String marque;
    private double prixUnitaire;
    private double prixGros;
    private int seuilGros;
    private String imagePath;
    private int quantite = 0;
    private String remise = "Aucune";

    // === Constructeurs ===

    /**
     * Constructeur principal complet avec tous les attributs de l'article,
     * utilisé lorsqu'on lit un article depuis la base de données,
     * car toutes les informations (ID + image) sont connues.
     */
    public Article(int id, String nom, String marque, double prixUnitaire, double prixGros, int seuilGros, String imagePath) {
        this.id = id;
        this.nom = nom;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixGros = prixGros;
        this.seuilGros = seuilGros;
        this.imagePath = imagePath;
    }

    /**
     * Constructeur secondaire sans image.
     * Utilisé pour des articles créés sans forcément avoir d'image (optionnelle)
     * ou pour une lecture depuis la base sans tenir compte du champ image.
     */
    public Article(int id, String nom, String marque, double prixUnitaire, double prixGros, int seuilGros) {
        this(id, nom, marque, prixUnitaire, prixGros, seuilGros, null);
    }

    /**
     * Constructeur utilisé pour créer un nouvel article dans l'application,
     * avant insertion en base de données.
     * L'ID est alors inconnu car il sera généré par MySQL.
     */
    public Article(String nom, String marque, double prixUnitaire, double prixGros, int seuilGros) {
        this.nom = nom;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixGros = prixGros;
        this.seuilGros = seuilGros;
    }

    // === Getters ===

    /** @return L'identifiant de l'article */
    public int getId() { return id; }

    /** @return Le nom de l'article */
    public String getNom() { return nom; }

    /** @return La marque de l'article */
    public String getMarque() { return marque; }

    /** @return Le prix unitaire */
    public double getPrixUnitaire() { return prixUnitaire; }

    /** @return Le prix de gros */
    public double getPrixGros() { return prixGros; }

    /** @return Le seuil pour bénéficier du prix de gros */
    public int getSeuilGros() { return seuilGros; }

    /** @return Le chemin de l'image associée */
    public String getImagePath() { return imagePath; }

    /** @return La quantité (utile pour le panier) */
    public int getQuantite() { return quantite; }

    /**
     * @return La description de la remise si applicable, sinon "Aucune"
     */
    public String getRemise() {
        if (seuilGros > 0 && prixGros > 0) {
            return seuilGros + " pour " + prixGros + " €";
        } else {
            return "Aucune";
        }
    }

    /** @return Le prix utilisé pour l'achat unitaire */
    public double getPrix() {
        return prixUnitaire;
    }

    // === Setters ===

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setPrixGros(double prixGros) { this.prixGros = prixGros; }
    public void setSeuilGros(int seuilGros) { this.seuilGros = seuilGros; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setRemise(String remise) { this.remise = remise; }

    // === equals & hashCode ===

    /**
     * Compare deux articles selon leur identifiant.
     *
     * @param o L'objet à comparer
     * @return true si même identifiant, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article other = (Article) o;
        return this.id == other.id;
    }

    /**
     * Génère un hashcode basé sur l'identifiant.
     *
     * return hashcode de l'article
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // === toString ===

    /**
     * Retourne une représentation textuelle de l'article.
     *
     * return Chaîne représentant l'article
     */
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
