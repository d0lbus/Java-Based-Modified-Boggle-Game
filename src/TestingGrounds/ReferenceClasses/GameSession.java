package TestingGrounds.ReferenceClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSession {
    private final int MAX_PLAYERS = 4;
    private int gameId;
    private String gameToken;
    private HashMap<String, Integer> players;
    private Map<String, Integer> playerScores;
    private GameStatus status;
    private int position;
    private char[] randomLetters;
    private ArrayList<String> guessedWords;

    public enum GameStatus {
        WAITING, ACTIVE, COMPLETED
    }

    public GameSession() {
        this.gameId = 0;
        this.gameToken = UUID.randomUUID().toString();
        this.players = new HashMap<>();
        this.playerScores = new HashMap<>();
        this.status = GameStatus.WAITING;
        this.position = 0;
        this.randomLetters = new char[20];
        this.guessedWords = new ArrayList<String>();
    }

    public void addPlayer(String sessionToken) {
        if (status != GameStatus.WAITING) {
            throw new IllegalStateException("Cannot add players unless the game is waiting");
        }
        if (players.containsKey(sessionToken)) {
            throw new IllegalArgumentException("Player already in the game");
        }
        if (players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("Game already has maximum allowed players");
        }
        players.put(sessionToken, ++position);
        if (players.size() == MAX_PLAYERS) {
            startGame();
        }
    }

    public void removePlayer(String sessionToken) {
        players.remove(sessionToken);
        if (players.size() < 2 && status == GameStatus.ACTIVE) {
            endGame();
        }
    }

    private void startGame() {
        status = GameStatus.ACTIVE;
    }

    private void endGame() {
        status = GameStatus.COMPLETED;
    }

    public String getSessionId() {
        return gameToken;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Map<String, Integer> getPlayers() {
        return new HashMap<>(players);
    }

    public boolean canJoin() {
        return this.status == GameStatus.WAITING && this.players.size() < MAX_PLAYERS;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public char[] getRandomLetters() {
        return randomLetters;
    }

    public void setRandomLetters(char[] randomLetters) {
        this.randomLetters = randomLetters;
    }

    public ArrayList<String> getGuessedWords() {
        return guessedWords;
    }

    public void setGuessedWords(ArrayList<String> guessedWords) {
        this.guessedWords = guessedWords;
    }

    public void addGuessedWord(String word) {
        if (!guessedWords.contains(word)) {
            guessedWords.add(word);
        }
    }

    public void updatePlayerScore(String sessionToken, int points) {
        playerScores.put(sessionToken, playerScores.getOrDefault(sessionToken, 0) + points);
    }

    public int getPlayerScore(String sessionToken) {
        return playerScores.getOrDefault(sessionToken, 0);
    }

    public Map<String, Integer> getPlayerScores() {
        return new HashMap<>(playerScores);
    }
}
