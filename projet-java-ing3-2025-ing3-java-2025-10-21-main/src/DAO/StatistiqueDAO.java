package DAO;

import java.sql.*;

/**
 * DAO pour effectuer des statistiques sur les articles de la base de données.
 * Permet de compter les articles et de calculer leur prix moyen.
 */
public class StatistiqueDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Compte le nombre total d'articles présents dans la base de données.
     *
     * return Le nombre total d'articles
     */
    public int compterArticles() {
        String sql = "SELECT COUNT(*) FROM article";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1); // La première colonne contient le résultat du COUNT
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du comptage des articles : " + e.getMessage());
        }

        return 0; // En cas d'erreur, retourne 0
    }

    /**
     * Calcule le prix moyen des articles stockés dans la base de données.
     *
     * return Le prix moyen des articles
     */
    public double prixMoyenArticles() {
        String sql = "SELECT AVG(prix_unitaire) FROM article";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1); // La première colonne contient le résultat du AVG
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du calcul du prix moyen : " + e.getMessage());
        }

        return 0.0; // En cas d'erreur, retourne 0.0
    }

    // Possibilité d'ajouter d'autres statistiques si besoin
}
