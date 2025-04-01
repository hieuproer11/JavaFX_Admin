package fsiAdministration.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDDManager {

    private static final String URL = "jdbc:postgresql://localhost:5433/FSI_admin";
    private static final String USER = "postgres";
    private static final String PASSWORD = "080600";
    private static Connection connection = null;

    // Établir une connexion à la BDD
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion établie avec succès!");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            }
        }
        return connection;
    }

    // Fermer la connexion
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connexion fermée avec succès!");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}