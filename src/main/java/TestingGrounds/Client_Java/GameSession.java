package TestingGrounds.Client_Java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class GameSession {
    private String sessionId;
    private List<User> players;
    private GameStatus status;

    public enum GameStatus {
        WAITING, ACTIVE, COMPLETED
    }

    public GameSession() {
        this.sessionId = UUID.randomUUID().toString(); // Unique identifier for the session
        this.players = new ArrayList<>();
        this.status = GameStatus.WAITING; // Initial status
    }

    public void addPlayer(User user) {
        if (!players.contains(user) && status == GameStatus.WAITING) {
            players.add(user);
            if (players.size() >= 2) { // Example: minimum two players to start
                startGame();
            }
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        if (players.size() < 2 && status == GameStatus.ACTIVE) {
            endGame(); // Not enough players to continue
        }
    }

    private void startGame() {
        status = GameStatus.ACTIVE;
        // Initialize game settings and notify players
    }

    private void endGame() {
        status = GameStatus.COMPLETED;
        // Clean up session, notify players, possibly store results
    }

    public String getSessionId() {
        return sessionId;
    }

    public GameStatus getStatus() {
        return status;
    }

    // Additional methods as needed for game logic
}

