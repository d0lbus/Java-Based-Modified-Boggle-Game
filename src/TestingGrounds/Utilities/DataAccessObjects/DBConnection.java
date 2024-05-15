package TestingGrounds.Utilities.DataAccessObjects;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection con;

    public static Connection getConnection() throws SQLException {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/boggled", "root", "");
        } catch (Exception e) {
            System.out.println("Database connection failed.");
            throw e;
        }
        return con;
    }
}