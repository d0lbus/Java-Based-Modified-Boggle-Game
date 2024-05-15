package TestingGrounds.ReferenceClasses;

import TestingGrounds.Utilities.DataAccessObjects.DBConnection;
import TestingGrounds.Utilities.DataAccessObjects.GameSessionDAO;
import TestingGrounds.Utilities.DataAccessObjects.GameSettingsDAO;
import TestingGrounds.Utilities.DataAccessObjects.UserDAO;

import java.sql.SQLException;
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
    private int currentPlayerCount;
    private int WINNING_ROUNDS;
    private int LOBBY_WAITING_TIME;
    private int DURATION_PER_ROUNDS;
    private int DELAY_PER_ROUNDS;


    public enum GameStatus {
        WAITING, ACTIVE, COMPLETED, CANCELLED
    }


    public GameSession(String gameToken) throws SQLException {
        this.gameToken = gameToken;
        GameSessionDAO dao = new GameSessionDAO(DBConnection.getConnection());
        try {
            GameSession loadedSession = dao.loadGameSession(gameToken);
            if (loadedSession != null) {
                // If a session with the specified token exists, load its settings.
                this.WINNING_ROUNDS = loadedSession.getWINNING_ROUNDS();
                this.LOBBY_WAITING_TIME = loadedSession.getLOBBY_WAITING_TIME();
                this.DURATION_PER_ROUNDS = loadedSession.getDURATION_PER_ROUNDS();
                this.DELAY_PER_ROUNDS = loadedSession.getDELAY_PER_ROUNDS();
                this.status = loadedSession.getStatus();
                this.players = (ConcurrentHashMap<String, Integer>) loadedSession.getPlayers();
                this.currentPlayerCount = loadedSession.getCurrentPlayerCount();
                this.playerScores = (ConcurrentHashMap<String, Integer>) loadedSession.getPlayerScores();
                this.playerRoundsWon = loadedSession.getPlayerRoundsWon();
                this.readyPlayers = loadedSession.getReadyPlayers();
                this.randomLetters = loadedSession.getRandomLetters();
                this.guessedWords = loadedSession.getGuessedWords();
                this.timerRunning.set(loadedSession.getTimerRunning().get());
                this.hostPosition.set(loadedSession.getHostPosition().get());
            } else {
                this.WINNING_ROUNDS = 3;
                this.LOBBY_WAITING_TIME = 10;
                this.DURATION_PER_ROUNDS = 30;
                this.DELAY_PER_ROUNDS = 5;
                this.status = GameStatus.WAITING;
                this.players = new ConcurrentHashMap<>();
                this.currentPlayerCount = 0;
                this.playerScores = new ConcurrentHashMap<>();
                this.playerRoundsWon = new ConcurrentHashMap<>();
                this.readyPlayers = new ConcurrentHashMap<>();
                this.randomLetters = new char[20];
                this.guessedWords = new ArrayList<>();
                this.timerRunning = new AtomicBoolean(false);
                this.hostPosition = new AtomicInteger(0);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load game session from database: " + e.getMessage());
            e.printStackTrace();
        }
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
        playerScores.put(sessionToken, 0);
        currentPlayerCount = players.size();
        if (players.size() == 1) {
            hostPosition.set(position);
        }
    }

    public void removePlayer(String sessionToken) {
        players.remove(sessionToken);
        readyPlayers.remove(sessionToken);
        playerRoundsWon.remove(sessionToken);
        currentPlayerCount = players.size();
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

    public int getWINNING_ROUNDS() {
        return WINNING_ROUNDS;
    }

    public void setWINNING_ROUNDS(int WINNING_ROUNDS) {
        this.WINNING_ROUNDS = WINNING_ROUNDS;
    }

    public int getLOBBY_WAITING_TIME() {
        return LOBBY_WAITING_TIME;
    }

    public void setLOBBY_WAITING_TIME(int LOBBY_WAITING_TIME) {
        this.LOBBY_WAITING_TIME = LOBBY_WAITING_TIME;
    }

    public int getDURATION_PER_ROUNDS() {
        return DURATION_PER_ROUNDS;
    }

    public void setDURATION_PER_ROUNDS(int DURATION_PER_ROUNDS) {
        this.DURATION_PER_ROUNDS = DURATION_PER_ROUNDS;
    }

    public int getDELAY_PER_ROUNDS() {
        return DELAY_PER_ROUNDS;
    }

    public void setDELAY_PER_ROUNDS(int DELAY_PER_ROUNDS) {
        this.DELAY_PER_ROUNDS = DELAY_PER_ROUNDS;
    }

    public int getMAX_PLAYERS() {
        return MAX_PLAYERS;
    }

    public String getGameToken() {
        return gameToken;
    }

    public int getCurrentPlayerCount() {
        return currentPlayerCount;
    }

    public void setCurrentPlayerCount(int currentPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    public void setPlayers(ConcurrentHashMap<String, Integer> players) {
        this.players = players;
    }

    public void setPlayerScores(ConcurrentHashMap<String, Integer> playerScores) {
        this.playerScores = playerScores;
    }

    public ConcurrentHashMap<String, Integer> getPlayerRoundsWon() {
        return playerRoundsWon;
    }

    public void setPlayerRoundsWon(ConcurrentHashMap<String, Integer> playerRoundsWon) {
        this.playerRoundsWon = playerRoundsWon;
    }

    public ConcurrentHashMap<String, Boolean> getReadyPlayers() {
        return readyPlayers;
    }

    public void setReadyPlayers(ConcurrentHashMap<String, Boolean> readyPlayers) {
        this.readyPlayers = readyPlayers;
    }

    public AtomicBoolean getTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(AtomicBoolean timerRunning) {
        this.timerRunning = timerRunning;
    }

    public AtomicInteger getHostPosition() {
        return hostPosition;
    }

    public void setHostPosition(AtomicInteger hostPosition) {
        this.hostPosition = hostPosition;
    }

    public void addGuessedWord(String word) {
        if (!guessedWords.contains(word)) {
            guessedWords.add(word);
        }
    }

    public void updatePlayerScore(String sessionToken, int points) {
        playerScores.put(sessionToken, playerScores.getOrDefault(sessionToken, 0) + points);
    }

    public void incrementRoundWinCount(String sessionToken) {
        playerRoundsWon.put(sessionToken, playerRoundsWon.getOrDefault(sessionToken, 0) + 1);
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

    public String determineRoundWinner() {
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        if (sortedScores.size() < 2 || sortedScores.get(0).getValue() != sortedScores.get(1).getValue()) {
            return sortedScores.get(0).getKey();
        }
        return null; // No clear winner
    }


    public String determineOverallWinner() {
        WINNING_ROUNDS = getWINNING_ROUNDS();
        for (Map.Entry<String, Integer> entry : playerRoundsWon.entrySet()) {
            if (entry.getValue() >= WINNING_ROUNDS) {
                return entry.getKey();
            }
        }
        return null;
    }


    public void resetScoresForNextRound() {
        playerScores.replaceAll((key, value) -> 0);
        guessedWords.clear();
    }



}
