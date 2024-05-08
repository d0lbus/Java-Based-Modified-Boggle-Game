package TestingGrounds.Utilities.DataAccessObjects;

import TestingGrounds.ReferenceClasses.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        System.out.println("Executing query with username: " + username); // Debugging log

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User found with username: " + username); // Debugging log
                    return new User(
                            rs.getString("playerId"),
                            rs.getString("username"),
                            rs.getString("sessionToken"),
                            rs.getBoolean("inGame"),
                            rs.getInt("score"),
                            rs.getString("currentGameToken")
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
    public boolean validatePassword(User user, String password) throws SQLException {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return password.equals(rs.getString("password"));
                }
            }
        }
        return false;
    }

    public void updateSessionToken(User user, String sessionToken) throws SQLException {
        String sql = "UPDATE users SET sessionToken = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        }
    }

    public static void createUser(User user, String firstName, String lastName, String password) throws SQLException {
        String sqlGetLastPlayerId = "SELECT playerId FROM users ORDER BY playerId DESC LIMIT 1";
        String sqlInsertUser = "INSERT INTO users (playerId, username, firstName, lastName, password, sessionToken, inGame, score, currentGameToken) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psGetLastPlayerId = conn.prepareStatement(sqlGetLastPlayerId);
             ResultSet rs = psGetLastPlayerId.executeQuery()) {

            int lastPlayerId = 0;
            if (rs.next()) {
                lastPlayerId = rs.getInt("playerId");
            }

            // Increment playerId
            int newPlayerId = lastPlayerId + 1;

            try (PreparedStatement psInsertUser = conn.prepareStatement(sqlInsertUser)) {
                psInsertUser.setInt(1, newPlayerId);
                psInsertUser.setString(2, user.getUsername());
                psInsertUser.setString(3, firstName); // Set first name
                psInsertUser.setString(4, lastName);  // Set last name
                psInsertUser.setString(5, password);
                psInsertUser.setString(6, user.getSessionToken());
                psInsertUser.setBoolean(7, user.isInGame());
                psInsertUser.setInt(8, user.getScore());
                psInsertUser.setString(9, user.getCurrentGameToken());

                psInsertUser.executeUpdate();
            }
        }
    }
    public static boolean doesUsernameExist(String username) throws SQLException {
        String sqlCheckUsername = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psCheckUsername = conn.prepareStatement(sqlCheckUsername)) {
            psCheckUsername.setString(1, username);
            try (ResultSet rs = psCheckUsername.executeQuery()) {
                return rs.next(); // If rs.next() returns true, the username exists
            }
        }
    }

    public static void clearSessionToken(String sessionToken) throws SQLException {
        String sql = "UPDATE users SET sessionToken = NULL WHERE sessionToken = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            ps.executeUpdate();
        }
    }
}