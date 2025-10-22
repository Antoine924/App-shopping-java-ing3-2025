package DAO;

import model.Commande;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les opérations sur la table "commande" dans la base de données.
 * Permet d'ajouter, supprimer et lister les commandes.
 */
public class CommandeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Ajoute une nouvelle commande dans la base de données.
     * Récupère et met à jour l'identifiant généré par la base dans l'objet Commande.
     *
     * @param commande La commande à ajouter
     */
    public void ajouterCommande(Commande commande) {
        String sql = "INSERT INTO commande(id_client, date_commande) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, commande.getIdClient());
            stmt.setTimestamp(2, Timestamp.valueOf(commande.getDateCommande()));

            stmt.executeUpdate();

            // Récupération automatique de l'ID généré par MySQL
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                commande.setId(idGenere);
                System.out.println("✅ Commande ajoutée avec l'id : " + idGenere);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    /**
     * Supprime une commande de la base de données en fonction de son identifiant.
     *
     * @param idCommande L'identifiant de la commande à supprimer
     */
    public void supprimerCommande(int idCommande) {
        String sql = "DELETE FROM commande WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCommande);
            stmt.executeUpdate();
            System.out.println("Commande supprimée !");
        } catch (SQLException e) {
            System.out.println("Erreur suppression commande : " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les commandes présentes dans la base de données.
     *
     * @return Une liste d'objets {@link Commande}
     */
    public List<Commande> listerCommandes() {
        List<Commande> liste = new ArrayList<>();
        String sql = "SELECT * FROM commande";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int idClient = rs.getInt("id_client");
                LocalDateTime dateCommande = rs.getTimestamp("date_commande").toLocalDateTime();

                Commande commande = new Commande(id, idClient, dateCommande);
                liste.add(commande);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des commandes : " + e.getMessage());
        }

        return liste;
    }
}
