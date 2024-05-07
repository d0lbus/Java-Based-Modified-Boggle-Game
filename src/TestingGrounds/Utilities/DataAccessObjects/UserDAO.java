package TestingGrounds.Utilities.DataAccessObjects;

import TestingGrounds.ReferenceClasses.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("playerId"),
                            rs.getString("username"),
                            rs.getString("sessionToken"),
                            rs.getBoolean("inGame"),
                            rs.getInt("score"),
                            rs.getString("currentGameToken")
                    );
                }
            }
        }
        return null;
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
        String sql = "UPDATE users SET sessionToken = ? WHERE playerId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            ps.setString(2, user.getPlayerId());
            ps.executeUpdate();
        }
    }

    public void createUser(User user, String password) throws SQLException {
        String sql = "INSERT INTO users (playerId, username, password, sessionToken, inGame, score, currentGameId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPlayerId());
            ps.setString(2, user.getUsername());
            ps.setString(3, password);
            ps.setString(4, user.getSessionToken());
            ps.setBoolean(5, user.isInGame());
            ps.setInt(6, user.getScore());
            ps.setString(7, user.getCurrentGameToken());
            ps.executeUpdate();
        }
    }
}