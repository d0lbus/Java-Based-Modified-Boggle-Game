package Java.Utilities.DataAccessObjects;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DBConnection class provides methods for establishing a connection to the database.
 * It uses JDBC to connect to a MySQL database hosted locally.
 */

public class DBConnection {
    private static Connection con;

    /**
     * Establishes a connection to the MySQL database.
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/boggled", "root", "password");
        } catch (Exception e) {
            System.out.println("Database connection failed.");
            throw e;
        }
        return con;
    }
}