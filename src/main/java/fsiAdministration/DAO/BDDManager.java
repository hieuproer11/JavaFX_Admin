package fsiAdministration.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDDManager {

    private static final String URL  = "jdbc:postgresql://localhost:5433/FSI_admin";
    private static final String USER = "postgres";
    private static final String PASS = "080600";

    /* Connexion unique partagée */
    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Connexion (re)établie !");
            }
        } catch (SQLException e) {
            System.err.println("Connexion impossible : " + e.getMessage());
            throw e;
        }
        return connection;
    }

    /*À appeler UNE SEULE FOIS à la sortie de l’appli */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur fermeture connexion : " + e.getMessage());
            }
            connection = null;
        }
    }
}
