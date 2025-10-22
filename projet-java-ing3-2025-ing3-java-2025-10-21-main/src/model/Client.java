package model;

/**
 * Représente un client de l'application.
 * Chaque client a un identifiant, un nom, un email, un mot de passe et un type (client ou admin).
 */
public class Client {

    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String typeClient;

    // === Constructeurs ===

    /**
     * Constructeur complet utilisé lorsqu'on récupère un client depuis la base de données.
     */
    public Client(int id, String nom, String email, String motDePasse, String typeClient) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.typeClient = typeClient;
    }

    /**
     * Constructeur utilisé lors de l'inscription d'un nouveau client (sans ID, généré par la base).
     */
    public Client(String nom, String email, String motDePasse, String typeClient) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.typeClient = typeClient;
    }

    /**
     * Constructeur simplifié utilisé pour la gestion rapide de clients,
     * notamment en affichage administrateur sans forcer la saisie du mot de passe.
     */
    public Client(String nom, String email) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = "";
        this.typeClient = "standard";
    }

    // === Getters ===

    public int getId() { return id; }

    public String getNom() { return nom; }

    public String getEmail() { return email; }

    public String getMotDePasse() { return motDePasse; }

    public String getTypeClient() { return typeClient; }

    // === Setters ===

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }

    /**
     * Retourne une description textuelle du client, utile pour l'affichage et le debug.
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", typeClient='" + typeClient + '\'' +
                '}';
    }
}
