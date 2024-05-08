package TestingGrounds.ReferenceClasses;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession {
    private final int MAX_PLAYERS = 4;
    private String gameToken;
    private ConcurrentHashMap<String, Integer> players;
    private ConcurrentHashMap<String, Integer> playerScores;
    private ConcurrentHashMap<String, Integer> playerRoundsWon;
    private ConcurrentHashMap<String, Boolean> readyPlayers;
    private GameStatus status;
    private char[] randomLetters;
    private ArrayList<String> guessedWords;
    private AtomicBoolean timerRunning = new AtomicBoolean(false);
    private AtomicInteger hostPosition = new AtomicInteger(0);
    private static final int WINNING_ROUNDS = 3;

    public enum GameStatus {
        WAITING, ACTIVE, COMPLETED
    }

    public GameSession() {
        this.gameToken = UUID.randomUUID().toString();
        this.players = new ConcurrentHashMap<>();
        this.playerScores = new ConcurrentHashMap<>();
        this.playerRoundsWon = new ConcurrentHashMap<>();
        this.readyPlayers = new ConcurrentHashMap<>();
        this.status = GameStatus.WAITING;
        this.randomLetters = new char[20];
        this.guessedWords = new ArrayList<>();
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
        int position = players.size() + 1;
        players.put(sessionToken, position);
        readyPlayers.put(sessionToken, false);
        playerRoundsWon.put(sessionToken, 0);
        if (players.size() == 1) {
            hostPosition.set(position);
        }
    }

    public void removePlayer(String sessionToken) {
        players.remove(sessionToken);
        readyPlayers.remove(sessionToken);
        playerRoundsWon.remove(sessionToken);
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

    public boolean isReady(String sessionToken) {
        return readyPlayers.getOrDefault(sessionToken, false);
    }

    public void markReady(String sessionToken) {
        readyPlayers.put(sessionToken, true);
    }

    public boolean allPlayersReady() {
        return readyPlayers.values().stream().allMatch(Boolean::booleanValue);
    }

    public boolean isTimerRunning() {
        return timerRunning.get();
    }

    public void setTimerRunning(boolean running) {
        timerRunning.set(running);
    }

    public boolean isHost(String sessionToken) {
        return players.get(sessionToken) == hostPosition.get();
    }

    // Determine the round winner
    public String determineRoundWinner() {
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        if (sortedScores.size() < 2 || sortedScores.get(0).getValue() != sortedScores.get(1).getValue()) {
            return sortedScores.get(0).getKey();
        }
        return null; // No clear winner
    }

    // Increment the round win count for a player
    public void incrementRoundWinCount(String sessionToken) {
        playerRoundsWon.put(sessionToken, playerRoundsWon.getOrDefault(sessionToken, 0) + 1);
    }

    // Check if any player has won the required number of rounds to win the game
    public String determineOverallWinner() {
        for (Map.Entry<String, Integer> entry : playerRoundsWon.entrySet()) {
            if (entry.getValue() >= WINNING_ROUNDS) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Reset scores for the next round
    public void resetScoresForNextRound() {
        playerScores.replaceAll((key, value) -> 0);
        guessedWords.clear();
    }
}
