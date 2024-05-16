package TestingGrounds.Utilities.DataAccessObjects;

import TestingGrounds.ReferenceClasses.GameSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameSessionDAO {

    public GameSessionDAO(){

    }


    public GameSessionDAO(Connection connection) {
    }

    public void saveGameSession(GameSession session) throws SQLException {
        String sql = "INSERT INTO game_sessions (game_token, winning_rounds, lobby_waiting_time, duration_per_round, delay_per_round, status, player_count) VALUES (?, ?, ?, ?, ?, ?, ?)"+
                "ON DUPLICATE KEY UPDATE " +
                "winning_rounds = VALUES(winning_rounds), " +
                "lobby_waiting_time = VALUES(lobby_waiting_time), " +
                "duration_per_round = VALUES(duration_per_round), " +
                "delay_per_round = VALUES(delay_per_round), " +
                "status = VALUES(status), " +
                "player_count = VALUES(player_count)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, session.getSessionId());
            stmt.setInt(2, session.getWINNING_ROUNDS());
            stmt.setInt(3, session.getLOBBY_WAITING_TIME());
            stmt.setInt(4, session.getDURATION_PER_ROUNDS());
            stmt.setInt(5, session.getDELAY_PER_ROUNDS());
            stmt.setString(6, session.getStatus().name());
            stmt.setInt(7, session.getCurrentPlayerCount());
            stmt.executeUpdate();
        }
    }

    public GameSession loadGameSession(String gameToken) throws SQLException {
        String sql = "SELECT * FROM game_sessions WHERE game_token = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, gameToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                GameSession session = new GameSession(rs.getString("game_token"));
                session.setWINNING_ROUNDS(rs.getInt("winning_rounds"));
                session.setLOBBY_WAITING_TIME(rs.getInt("lobby_waiting_time"));
                session.setDURATION_PER_ROUNDS(rs.getInt("duration_per_round"));
                session.setDELAY_PER_ROUNDS(rs.getInt("delay_per_round"));
                session.setStatus(GameSession.GameStatus.valueOf(rs.getString("status")));
                session.setCurrentPlayerCount(rs.getInt("player_count"));
                return session;
            }
        }
        return null;
    }

    public boolean isTokenUnique(String token) throws SQLException {
        String sql = "SELECT COUNT(*) FROM game_sessions WHERE game_token = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    public List<GameSession> getAllGameSessions() throws SQLException {
        List<GameSession> sessions = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT game_token, max_players, winning_rounds, lobby_waiting_time, duration_per_round, delay_per_round, status, player_count FROM game_sessions";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sessions.add(new GameSession(
                        rs.getString("game_token"),
                        rs.getInt("max_players"),
                        rs.getInt("winning_rounds"),
                        rs.getInt("lobby_waiting_time"),
                        rs.getInt("duration_per_round"),
                        rs.getInt("delay_per_round"),
                        rs.getString("status"),
                        (Integer) rs.getObject("player_count") // Handles potential NULL values
                ));
            }
        }
        return sessions;
    }
}

