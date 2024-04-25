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
    private HashMap<User,Integer> players;
    private GameStatus status;
    private int position;

    public enum GameStatus {
        WAITING, ACTIVE, COMPLETED
    }

    public GameSession() {
        this.sessionId = UUID.randomUUID().toString(); // Unique identifier for the session
        this.players = new HashMap<>();
        this.status = GameStatus.WAITING; // Initial status
        this.position = 1;
    }

    public void addPlayer(User user) {
        if (!players.containsKey(user) && status == GameStatus.WAITING) {
            position++;
            players.put(user,position);
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

