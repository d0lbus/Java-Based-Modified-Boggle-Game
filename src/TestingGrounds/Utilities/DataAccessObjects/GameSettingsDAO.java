package TestingGrounds.Utilities.DataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSettingsDAO {
    private static final String FETCH_ROUNDS_TO_WIN_QUERY = "SELECT rounds_to_win FROM game_settings LIMIT 1";
    private static final String FETCH_SECONDS_PER_ROUND_QUERY = "SELECT seconds_per_round FROM game_settings LIMIT 1";
    private static final String UPDATE_ROUNDS_TO_WIN_QUERY = "UPDATE game_settings SET rounds_to_win = ? WHERE id = 1";
    private static final String UPDATE_SECONDS_PER_ROUND_QUERY = "UPDATE game_settings SET seconds_per_round = ? WHERE id = 1";

    private static final String FETCH_SECONDS_PER_WAITING_QUERY = "SELECT seconds_per_waiting FROM game_settings LIMIT 1";

    private static final String UPDATE_SECONDS_PER_WAITING_QUERY = "UPDATE game_settings SET seconds_per_waiting = ? WHERE id = 1";

    public int fetchRoundsToWin() throws SQLException {
        int roundsToWin = 3; // default value
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FETCH_ROUNDS_TO_WIN_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                roundsToWin = resultSet.getInt("rounds_to_win");
            }
        }
        return roundsToWin;
    }

    public int fetchSecondsPerRound() throws SQLException {
        int secondsPerRound = 30; // default value
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FETCH_SECONDS_PER_ROUND_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                secondsPerRound = resultSet.getInt("seconds_per_round");
            }
        }
        return secondsPerRound;
    }

    public void updateRoundsToWin(int roundsToWin) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROUNDS_TO_WIN_QUERY)) {
            preparedStatement.setInt(1, roundsToWin);
            preparedStatement.executeUpdate();
        }
    }

    public void updateSecondsPerRound(int secondsPerRound) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SECONDS_PER_ROUND_QUERY)) {
            preparedStatement.setInt(1, secondsPerRound);
            preparedStatement.executeUpdate();
        }
    }

    public int fetchSecondsPerWaiting() throws SQLException {
        int secondsPerWaiting = 5; // default value
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FETCH_SECONDS_PER_WAITING_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                secondsPerWaiting = resultSet.getInt("seconds_per_waiting");
            }
        }
        return secondsPerWaiting;
    }

    public void updateSecondsPerWaiting(int secondsPerWaiting) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SECONDS_PER_WAITING_QUERY)) {
            preparedStatement.setInt(1, secondsPerWaiting);
            preparedStatement.executeUpdate();
        }
    }
}
