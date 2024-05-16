package TestingGrounds.Utilities.DataAccessObjects;

import TestingGrounds.ReferenceClasses.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                            rs.getInt("overall_rounds_won"),
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

    public static void createUser(User user, String firstName, String lastName, String password, String sessionToken, Boolean inGame, Integer overall_rounds_won, Integer score, String currentGameToken) throws SQLException {
        String sqlGetLastPlayerId = "SELECT playerId FROM users ORDER BY playerId DESC LIMIT 1";
        String sqlInsertUser = "INSERT INTO users (playerId, username, firstName, lastName, password, sessionToken,overall_rounds_won, inGame, score, currentGameToken) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                psInsertUser.setString(6, sessionToken); // Set sessionToken
                if (overall_rounds_won != null) {
                    psInsertUser.setInt(7, overall_rounds_won); // Set overall rounds won
                } else {
                    psInsertUser.setNull(7, Types.INTEGER); // Set overall rounds won as null
                }
                if (inGame != null) {
                    psInsertUser.setBoolean(8, inGame); // Set inGame
                } else {
                    psInsertUser.setNull(8, Types.BOOLEAN); // Set inGame as null
                }
                if (score != null) {
                    psInsertUser.setInt(9, score); // Set score
                } else {
                    psInsertUser.setNull(9, Types.INTEGER); // Set score as null
                }
                psInsertUser.setString(10, currentGameToken); // Set currentGameToken

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

    public static String getSessionTokenByUsername(String username) throws SQLException {
        String sql = "SELECT sessionToken FROM users WHERE username = ?";
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

    public void updateOverallRoundsWon(String sessionToken, int roundsToAdd) {
        String sql = "UPDATE users SET overall_rounds_won = COALESCE(overall_rounds_won, 0) + ? WHERE sessionToken = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roundsToAdd);
            pstmt.setString(2, sessionToken);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("No rows affected, check if session token is correct: " + sessionToken);
            } else {
                System.out.println("Updated rounds won for session token: " + sessionToken);
            }
        } catch (SQLException e) {
            System.err.println("Failed to update rounds won for sessionToken: " + sessionToken + ", Error: " + e.getMessage());
        }
    }

    public List<User> getTopPlayersByRoundsWon() {
        List<User> topPlayers = new ArrayList<>();
        String sql = "SELECT username, overall_rounds_won FROM users ORDER BY overall_rounds_won DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setScore(rs.getInt("overall_rounds_won"));
                topPlayers.add(user);

                // Log each fetched user for debugging
                System.out.println("Fetched user: " + user.getUsername() + " with rounds won: " + user.getScore());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during fetching top players: " + e.getMessage());
        }
        return topPlayers;
    }

    public static void clearSessionTokens() throws SQLException {
        String sql = "UPDATE users SET sessionToken = NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Cleared session tokens for " + rowsUpdated + " users.");
        } catch (SQLException e) {
            System.err.println("Failed to clear session tokens: " + e.getMessage());
            throw e;
        }
    }
}