package Java.Utilities.DataAccessObjects;

import Java.ReferenceClasses.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    public Admin getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ?";
        System.out.println("Executing query with username: " + username); // Debugging log

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User found with username: " + username); // Debugging log
                    return new Admin(
                            rs.getString("adminid"),
                            rs.getString("username"),
                            rs.getString("sessionToken")
                    );
                } else {
                    System.out.println("No user found with username: " + username); // Debugging log
                }
            }
        } catch (SQLException e) {
            System.err.println("Error querying user by username: " + e.getMessage());
            throw e;
        }
        return null; // Return null if no matching user is found
    }


    public boolean validatePassword(Admin admin, String password) throws SQLException {
        String sql = "SELECT password FROM admins WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return password.equals(rs.getString("password"));
                }
            }
        }
        return false;
    }

    public void updateSessionToken(Admin admin, String sessionToken) throws SQLException {
        String sql = "UPDATE admins SET sessionToken = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            ps.setString(2, admin.getUsername());
            ps.executeUpdate();
        }
    }

    public static void clearSessionToken(String sessionToken) throws SQLException {
        String sql = "UPDATE admins SET sessionToken = NULL WHERE sessionToken = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            ps.executeUpdate();
        }
    }

    public static String getSessionTokenByUsername(String username) throws SQLException {
        String sql = "SELECT sessionToken FROM admins WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("sessionToken");
                }
            }
        }
        return null;
    }
}
