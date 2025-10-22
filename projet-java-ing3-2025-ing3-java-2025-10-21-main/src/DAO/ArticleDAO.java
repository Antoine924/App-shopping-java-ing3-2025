package DAO;

import model.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les opérations sur la table "article" dans la base de données.
 * Fournit des méthodes pour ajouter, modifier, supprimer et lister les articles.
 */
public class ArticleDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Ajoute un nouvel article dans la base de données.
     *
     * @param article L'article à ajouter
     */
    public void ajouterArticle(Article article) {
        String sql = "INSERT INTO article(nom, marque, prix_unitaire, prix_gros, seuil_gros, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getMarque());
            stmt.setDouble(3, article.getPrixUnitaire());
            stmt.setDouble(4, article.getPrixGros());
            stmt.setInt(5, article.getSeuilGros());
            stmt.setString(6, article.getImagePath());

            stmt.executeUpdate();
            System.out.println("✅ Article ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    /**
     * Supprime un article de la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant de l'article à supprimer
     */
    public void supprimerArticle(int id) {
        String sql = "DELETE FROM article WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Article supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun article trouvé avec l’ID donné.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    /**
     * Modifie un article existant dans la base de données.
     *
     * @param article L'article contenant les nouvelles informations
     */
    public void modifierArticle(Article article) {
        String sql = "UPDATE article SET nom = ?, marque = ?, prix_unitaire = ?, prix_gros = ?, seuil_gros = ?, image_path = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getMarque());
            stmt.setDouble(3, article.getPrixUnitaire());
            stmt.setDouble(4, article.getPrixGros());
            stmt.setInt(5, article.getSeuilGros());
            stmt.setString(6, article.getImagePath());
            stmt.setInt(7, article.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Article modifié avec succès !");
            } else {
                System.out.println("⚠️ Aucun article trouvé avec l’ID donné.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Récupère tous les articles présents dans la base de données.
     *
     * @return Une liste d'objets {@link Article}
     */
    public List<Article> listerArticles() {
        List<Article> liste = new ArrayList<>();
        String sql = "SELECT * FROM article";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Article a = new Article(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("marque"),
                        rs.getDouble("prix_unitaire"),
                        rs.getDouble("prix_gros"),
                        rs.getInt("seuil_gros"),
                        rs.getString("image_path")
                );
                liste.add(a);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return liste;
    }
}
