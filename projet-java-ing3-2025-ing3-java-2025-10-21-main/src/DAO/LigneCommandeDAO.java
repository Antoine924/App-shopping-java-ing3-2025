package DAO;

import model.LigneCommande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les opérations sur la table "ligne_commande" dans la base de données.
 * Permet d'ajouter, supprimer et lister les lignes de commande associées aux commandes.
 */
public class LigneCommandeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Ajoute une nouvelle ligne de commande dans la base de données.
     *
     * le param ligne La ligne de commande à ajouter
     */
    public void ajouterLigneCommande(LigneCommande ligne) {
        String sql = "INSERT INTO ligne_commande(id_commande, id_article, quantite) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ligne.getIdCommande());
            stmt.setInt(2, ligne.getIdArticle());
            stmt.setInt(3, ligne.getQuantite());

            stmt.executeUpdate();
            System.out.println("✅ Ligne de commande ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌❌❌ Erreur lors de l'ajout de la ligne de commande : " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les lignes de commande associées à une commande spécifique.
     *
     *  idCommande L'identifiant de la commande
     * return Une liste d'objets {@link LigneCommande}
     */
    public List<LigneCommande> listerLignesParCommande(int idCommande) {
        List<LigneCommande> liste = new ArrayList<>();
        String sql = "SELECT * FROM ligne_commande WHERE id_commande = ?";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCommande);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LigneCommande ligne = new LigneCommande(
                        rs.getInt("id"),
                        rs.getInt("id_commande"),
                        rs.getInt("id_article"),
                        rs.getInt("quantite")
                );
                liste.add(ligne);
            }

        } catch (SQLException e) {
            System.out.println("❌❌❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Supprime une ligne de commande de la base de données par son identifiant.
     *
     *  idLigne L'identifiant de la ligne de commande à supprimer
     */
    public void supprimerLigneCommande(int idLigne) {
        String sql = "DELETE FROM ligne_commande WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLigne);
            stmt.executeUpdate();
            System.out.println("Ligne supprimée !");
        } catch (SQLException e) {
            System.out.println("Erreur suppression ligne : " + e.getMessage());
        }
    }
}
