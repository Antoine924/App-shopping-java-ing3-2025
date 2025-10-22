package DAO;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO pour générer des statistiques sur les ventes à partir des tables
 * commande, article, client et ligne_commande.
 */
public class StatscommandeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Calcule le montant total des ventes réalisées.
     *
     * return Le total des ventes en euros
     */
    public double calculerVentesTotales() {
        String sql = "SELECT SUM(a.prix_unitaire * lc.quantite) AS total FROM ligne_commande lc " +
                "JOIN article a ON lc.id_article = a.id";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getDouble("total");

        } catch (SQLException e) {
            System.out.println("Erreur total ventes : " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Récupère les quantités totales vendues pour chaque article.
     *
     * return Une map où la clé est le nom de l'article et la valeur est la quantité totale vendue
     */
    public Map<String, Integer> getQuantitesParArticle() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT a.nom, SUM(lc.quantite) AS total FROM ligne_commande lc " +
                "JOIN article a ON lc.id_article = a.id GROUP BY a.nom ORDER BY total DESC";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stats.put(rs.getString("nom"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            System.out.println("Erreur stats article : " + e.getMessage());
        }
        return stats;
    }

    /**
     * Récupère le total des ventes réalisées par chaque client.
     *
     * return Une map où la clé est le nom du client et la valeur est le montant total acheté en euros
     */
    public Map<String, Double> getVentesParClient() {
        Map<String, Double> stats = new HashMap<>();
        String sql = "SELECT c.nom, SUM(a.prix_unitaire * lc.quantite) AS total " +
                "FROM client c " +
                "JOIN commande co ON c.id = co.id_client " +
                "JOIN ligne_commande lc ON co.id = lc.id_commande " +
                "JOIN article a ON lc.id_article = a.id " +
                "GROUP BY c.nom ORDER BY total DESC";

        try (Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stats.put(rs.getString("nom"), rs.getDouble("total"));
            }

        } catch (SQLException e) {
            System.out.println("Erreur stats client : " + e.getMessage());
        }
        return stats;
    }
}
