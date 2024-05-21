package Java.Utilities.DataAccessObjects;

import Java.ReferenceClasses.GameSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The GameSessionDAO class provides methods for interacting with the game sessions stored in the database.
 * It includes methods for saving, loading, and updating game sessions, as well as checking token uniqueness and retrieving all game sessions.
 */
public class GameSessionDAO {

    public GameSessionDAO(){

    }


    public GameSessionDAO(Connection connection) {
    }

    /**
     * Saves a game session to the database or updates it if the session already exists.
     * @param session
     * @throws SQLException
     */
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

    /**
     * Loads a game session from the database based on the provided game token.
     * @param gameToken
     * @return
     * @throws SQLException
     */
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

    /**
     * Checks if a given game token is unique in the database.
     * @param token
     * @return
     * @throws SQLException
     */
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

    /**
     * Retrieves a list of all game sessions stored in the database.
     *
     * @return
     * @throws SQLException
     */
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

    /**
     * Updates the winning rounds setting for all waiting lobbies in the database.
     * @param winningRounds
     * @return
     * @throws SQLException
     */
    public int updateWinningRoundsForWaitingLobbies(int winningRounds) throws SQLException {
        return updateAllWaitingGameSessions("winning_rounds", winningRounds);
    }

    /**
     * Updates the lobby waiting time setting for all waiting lobbies in the database.
     *
     * @param lobbyWaitingTime
     * @return
     * @throws SQLException
     */
    public int updateLobbyWaitingTimeForWaitingLobbies(int lobbyWaitingTime) throws SQLException {
        return updateAllWaitingGameSessions("lobby_waiting_time", lobbyWaitingTime);
    }

    /**
     * Updates the duration per round setting for all waiting lobbies in the database.
     * @param durationPerRound
     * @return
     * @throws SQLException
     */
    public int updateDurationPerRoundForWaitingLobbies(int durationPerRound) throws SQLException {
        return updateAllWaitingGameSessions("duration_per_round", durationPerRound);
    }

    /**
     * Updates the delay per round setting for all waiting lobbies in the database.
     * @param delayPerRound
     * @return
     * @throws SQLException
     */
    public int updateDelayPerRoundForWaitingLobbies(int delayPerRound) throws SQLException {
        return updateAllWaitingGameSessions("delay_per_round", delayPerRound);
    }

    /**
     * Updates a specific setting for all waiting lobbies in the database.
     *
     * @param settingName
     * @param settingValue
     * @return
     * @throws SQLException
     */
    private int updateAllWaitingGameSessions(String settingName, int settingValue) throws SQLException {
        String sql = "UPDATE game_sessions SET " + settingName + " = ? WHERE status = 'WAITING'";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, settingValue);
            return statement.executeUpdate();
        }
    }
}

