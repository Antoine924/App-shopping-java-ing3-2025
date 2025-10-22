package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    // ✅ Méthode manquante à ajouter
    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }

    // Test manuel (facultatif)
    public static void main(String[] args) {
        try (Connection connexion = getConnexion()) {
            System.out.println("✅ Connexion OK !");
        } catch (SQLException e) {
            System.out.println("❌ Connexion échouée : " + e.getMessage());
        }
    }
}
