package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.*;
import TestingGrounds.ReferenceClasses.Admin;
import TestingGrounds.ReferenceClasses.GameSession;
import TestingGrounds.ReferenceClasses.User;
import TestingGrounds.Utilities.DataAccessObjects.*;
import TestingGrounds.Utilities.TokenGenerator;
import TestingGrounds.Utilities.WordValidator;
import TestingGrounds.ReferenceClasses.User;

import org.omg.CORBA.*;
import org.omg.CORBA.Object;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GameServerImpl extends GameServerPOA implements Object {
    private Map<String, String> sessionTokens = new ConcurrentHashMap<>();
    private Map<String, GameSession> activeGameLobbies = new ConcurrentHashMap<>();
    private Map<String, CallbackInterface> sessionCallbacks = new ConcurrentHashMap<>();
    private Map<String, CallbackInterface> adminSessionCallbacks = new ConcurrentHashMap<>();
    private static final char[] CONSONANTS = "BCDFGHJKLMNPQRSTVWXYZ".toCharArray();
    private static final char[] VOWELS = "AEIOU".toCharArray();
    private static final int NUM_CONSONANTS = 13;
    private static final int NUM_VOWELS = 7;
    private UserDAO userDAO = new UserDAO();
    private AdminDAO adminDAO = new AdminDAO();
    private final Random random = new Random();
    private int durationPerRound;
    private int durationPerWaiting;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService roundScheduler = Executors.newScheduledThreadPool(1);
    private GameSessionDAO gameSessionDAO = new GameSessionDAO(DBConnection.getConnection());

    public GameServerImpl() throws SQLException {
    }


    @Override
    public boolean login(String username, String password, org.omg.CORBA.StringHolder sessionToken, CallbackInterface cbi)throws InvalidCredentials, AlreadyLoggedIn {
        try {
            User user = userDAO.getUserByUsername(username);

            if (user == null || !userDAO.validatePassword(user, password)) {
                throw new InvalidCredentials();
            }

            String storedToken = UserDAO.getSessionTokenByUsername(username);

            // Check if the session token is not null, meaning the user is already logged in
            if (storedToken != null) {
                System.err.println("User " + username + " is already logged in.");
                throw new AlreadyLoggedIn("User " + username + " is already logged in.");
            }


            String token = generateSecureToken();
            sessionToken.value = token;

            // Update user session in the database
            user.setSessionToken(token);
            userDAO.updateSessionToken(user, token);

            sessionTokens.put(token, username);
            sessionCallbacks.put(token, cbi);

            updateLeaderboards();
            System.out.println("Callback registered for " + username + " with token: " + token);
            return true;
        } catch (SQLException e) {
            System.err.println("SQL error during login: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean logout(String sessionToken) {
        try {
            // Always attempt to remove the session token from the sessionTokens map
            sessionTokens.remove(sessionToken);

            // Clear the session token in the database using the provided session token
            userDAO.clearSessionToken(sessionToken);

            return true; // Return true if the operation was successful
        } catch (SQLException ex) {
            // Log any database-related errors
            System.err.println("Error clearing session token: " + ex.getMessage());
            return false; // Return false if an exception occurred
        }
    }

    @Override
    public boolean adminLogin(String username, String password, org.omg.CORBA.StringHolder sessionToken, CallbackInterface cbi)throws InvalidCredentials, AlreadyLoggedIn {
        try {
            Admin admin = adminDAO.getUserByUsername(username);

            if (admin == null || !adminDAO.validatePassword(admin, password)) {
                throw new InvalidCredentials();
            }

            String storedToken = adminDAO.getSessionTokenByUsername(username);

            // Check if the session token is not null, meaning the user is already logged in
            if (storedToken != null) {
                System.err.println("User " + username + " is already logged in.");
                throw new AlreadyLoggedIn("User " + username + " is already logged in.");
            }


            String token = generateSecureToken();
            sessionToken.value = token;

            // Update user session in the database
            admin.setSessionToken(token);
            adminDAO.updateSessionToken(admin, token);

            sessionTokens.put(token, username);
            adminSessionCallbacks.put(token, cbi);

            updateLobbiesList();
            System.out.println("Callback registered for " + username + " with token: " + token);
            return true;
        } catch (SQLException e) {
            System.err.println("SQL error during login: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean adminLogout(String sessionToken) {
        try {
            // Always attempt to remove the session token from the sessionTokens map
            sessionTokens.remove(sessionToken);

            // Clear the session token in the database using the provided session token
            adminDAO.clearSessionToken(sessionToken);

            return true; // Return true if the operation was successful
        } catch (SQLException ex) {
            // Log any database-related errors
            System.err.println("Error clearing session token: " + ex.getMessage());
            return false; // Return false if an exception occurred
        }
    }

    /**
     *
     * PLAYER FUNCTIONALITIES
     *
     * */

    @Override
    public String hostGame(String sessionToken, CallbackInterface cbi) {
        GameSession newGameSession = new GameSession();
        
        if (cbi == null) {
            throw new RuntimeException("Callback interface cannot be null.");
        }

        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
        }
        try {

            newGameSession = new GameSession(generateGameToken());
            activeGameLobbies.put(newGameSession.getSessionId(), newGameSession);
            System.out.println("Game session created with ID: " + newGameSession.getSessionId());

            newGameSession.addPlayer(sessionToken);

            gameSessionDAO.saveGameSession(newGameSession);
            notifyPlayersAboutChanges(newGameSession);
        } catch (SQLException E){
            System.err.println(E);
        }

        return newGameSession.getSessionId();
    }
    @Override
    public String joinRandomGame(String sessionToken, CallbackInterface gci) throws NoWaitingGames {
        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
        }

        List<GameSession> availableSessions = activeGameLobbies.values().stream()
                .filter(GameSession::canJoin)
                .collect(Collectors.toList());

        if (availableSessions.isEmpty()) {
            System.out.println("No available game sessions to join");
            throw new NoWaitingGames();
        }

        int randomIndex = new Random().nextInt(availableSessions.size());
        GameSession selectedSession = availableSessions.get(randomIndex);
        selectedSession.addPlayer(sessionToken);
        System.out.println("Player added to game " + selectedSession.getSessionId());
        try {
            gameSessionDAO.saveGameSession(selectedSession);
        } catch (SQLException e){
            System.err.println(e);
        }

        notifyPlayersAboutChanges(selectedSession);
        return selectedSession.getSessionId();
    }
    @Override
    public String joinGame(String sessionToken, String gameId) throws InvalidGameCode {
        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
        }
        // Find the game session with the specified game ID
        Optional<GameSession> optionalSession = activeGameLobbies.values().stream()
                .filter(session -> session.getSessionId().equals(gameId))
                .filter(GameSession::canJoin)
                .findFirst();

        if (!optionalSession.isPresent()) {
            System.out.println("No available game session with the specified ID to join");
            throw new InvalidGameCode();
        }

        GameSession selectedSession = optionalSession.get();
        selectedSession.addPlayer(sessionToken);
        System.out.println("Player added to game " + selectedSession.getSessionId());
        try {
            gameSessionDAO.saveGameSession(selectedSession);
        } catch (SQLException e){
            System.err.println(e);
        }
        notifyPlayersAboutChanges(selectedSession);
        return selectedSession.getSessionId();
    }

    @Override
    public boolean startGame(String sessionToken, String gameToken) throws LobbyTimeExpired{
        GameSession session = activeGameLobbies.get(gameToken);
        if (session == null) {
            System.out.println("Invalid game session token.");
            return false;
        }


        if (session.isHost(sessionToken)) {
            // Check if there are fewer than two players
            if (session.getPlayers().size() < 2) {
                System.out.println("Cannot start the game with fewer than two players.");
                CallbackInterface callback = sessionCallbacks.get(sessionToken);
                if (callback != null) {
                    try {
                        callback.ReadyStateException();
                    } catch (Exception e) {
                        System.err.println("Error notifying host about insufficient players: " + sessionToken);
                        e.printStackTrace();
                    }
                }
                return false; // Exit early
            }

            // Start the timer if there are two or more players
            if (!session.isTimerRunning()) {
                session.markReady(sessionToken);
                System.out.println("Host started the timer, please click ready");
                updateReadyStatusForPlayers(session);

                startTimerForGui(session, session.getLOBBY_WAITING_TIME());
                session.setTimerRunning(true);

                scheduler.schedule(() -> {
                    if (!session.allPlayersReady()) {
                        System.out.println("Not all players are ready within the specified time.");
                        sendTimeoutExceptionToHost(sessionToken, session);
                    } else {
                        if (session.getStatus() != GameSession.GameStatus.ACTIVE) {
                            try {
                                startGameSession(session);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    session.setTimerRunning(false);
                }, session.getLOBBY_WAITING_TIME(), TimeUnit.SECONDS);
            } else {
                System.out.println("Timer is already running.");
            }
        } else {
            if (session.isTimerRunning()) {
                session.markReady(sessionToken);
                System.out.println("Player marked as ready: " + sessionToken);
                updateReadyStatusForPlayers(session);
                if (session.allPlayersReady() && session.getStatus() != GameSession.GameStatus.ACTIVE) {
                    try {
                        startGameSession(session);
                    } catch (SQLException e){
                        System.err.println(e);
                    }
                }
            } else {
                System.out.println("Timer is not running. Cannot mark player as ready.");
            }
        }
        try {
            gameSessionDAO.saveGameSession(session);
        } catch (SQLException e){
            System.err.println(e);
        }

        notifyPlayersAboutChanges(session);
        return true;
    }
    @Override
    public void submitWord(String sessionToken, String gameToken, String word) throws InvalidWord {
        GameSession session = activeGameLobbies.get(gameToken);
        String playerWhoGuessed = retrievePlayerFromSessionToken(sessionToken);
        if (session == null) {
            System.out.println("Invalid game session token.");
            return;
        }

        WordValidator wordValidator = new WordValidator("src/words.txt");

        // Convert random letters to lowercase
        char[] randomLetters = session.getRandomLetters();
        Map<Character, Integer> randomLetterMap = new HashMap<>();
        for (char c : randomLetters) {
            char lowerC = Character.toLowerCase(c);
            randomLetterMap.put(lowerC, randomLetterMap.getOrDefault(lowerC, 0) + 1);
        }

        // Convert submitted word to lowercase
        String lowerWord = word.toLowerCase();
        char[] charArray = lowerWord.toCharArray();
        Map<Character, Integer> wordMap = new HashMap<>();
        for (char c : charArray) {
            wordMap.put(c, wordMap.getOrDefault(c, 0) + 1);
        }

        boolean isValid = true;
        for (Map.Entry<Character, Integer> entry : wordMap.entrySet()) {
            char letter = entry.getKey();
            int count = entry.getValue();
            if (randomLetterMap.getOrDefault(letter, 0) < count) {
                isValid = false;
                break;
            }
        }

        if (isValid && wordValidator.isWordValid(lowerWord)) {
            if (session.getGuessedWords().contains(lowerWord)) {
                CallbackInterface callback = sessionCallbacks.get(sessionToken);
                if (callback != null) {
                    try {
                        callback.wordHasBeenGuessed(lowerWord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                session.addGuessedWord(lowerWord);
                session.updatePlayerScore(sessionToken, lowerWord.length());

                List<PlayerInfo> playerData = collectPlayerData(session);
                session.getPlayers().forEach((token, position) -> {
                    CallbackInterface callback = sessionCallbacks.get(token);
                    if (callback != null) {
                        try {
                            callback.broadcastGuessedWord(playerData.toArray(new PlayerInfo[0]), lowerWord, playerWhoGuessed);
                        } catch (Exception e) {
                            System.err.println("Error updating GUI for token: " + token);
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("No callback registered for token: " + token);
                    }
                });
                CallbackInterface callback = sessionCallbacks.get(sessionToken);
                if (callback != null) {
                    try {
                        callback.wordIsValid(lowerWord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Valid word: " + word);
            }
        } else {
            CallbackInterface callback = sessionCallbacks.get(sessionToken);
            if (callback != null) {
                try {
                    callback.wordIsInvalid(lowerWord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Invalid word: " + word);
            throw new InvalidWord("Invalid word: " + word);
        }

        try {
            gameSessionDAO.saveGameSession(session);
        } catch (SQLException e){
            System.err.println(e);
        };
    }

    @Override
    public void leaveGame(String sessionToken, String gameId) {
        // Check if the session token is valid
        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
            return;
        }

        // Retrieve the game session using the provided game ID
        GameSession gameSession = activeGameLobbies.get(gameId);

        // Check if the game session exists
        if (gameSession == null) {
            System.out.println("Invalid game session ID");
            return;
        }

        // Check if the player is in the game session
        if (!gameSession.getPlayers().containsKey(sessionToken)) {
            System.out.println("Player is not in the game session");
            return;
        }

        // If the player leaving is the host and there are no more players, cancel the game session
        if (gameSession.isHost(sessionToken) && gameSession.getPlayers().isEmpty()) {
            try {
                cancelGameSession(gameId);
            } catch (SQLException e){
                System.err.println(e);
            }
        }

        // Remove the player from the game session
        gameSession.removePlayer(sessionToken);

        try {
            String username = userDAO.getUserNameBySessionToken(sessionToken);
            gameSession.getPlayers().forEach((token, position) -> {
                CallbackInterface callback = sessionCallbacks.get(token);
                if (callback != null) {
                    try {
                        callback.broadcastDisconnection(username);
                    } catch (Exception e) {
                        System.err.println("Error updating GUI for token: " + token);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No callback registered for token: " + token);
                }
            });

        } catch (SQLException e){
            System.err.println(e);
        }

        notifyPlayersAboutChanges(gameSession);
        backToHomeScreen(sessionToken);
    }
    @Override
    public void leaveLobby(String sessionToken, String gameId) {
        GameSession gameSession = activeGameLobbies.get(gameId);

        if (gameSession == null) {
            System.out.println("Invalid game session ID");
            return;
        }

        // Remove the player from the game session
        gameSession.removePlayer(sessionToken);
        System.out.println("Player left the game session");

        // If the game session has no players left, it is cancelled
        if (gameSession.getCurrentPlayerCount() == 0) {
            activeGameLobbies.remove(gameId);
            gameSession.setStatus(GameSession.GameStatus.CANCELLED);
            System.out.println("Game session canceled as no players are left.");
            try {
                gameSessionDAO.saveGameSession(gameSession);
            } catch (SQLException e) {
                System.err.println("Error deleting game session: " + e.getMessage());
            }
        } else {
            // Save updates to the database
            try {
                gameSessionDAO.saveGameSession(gameSession);
            } catch (SQLException e) {
                System.err.println("Error saving game session: " + e.getMessage());
            }


            notifyPlayersAboutChanges(gameSession);
            backToHomeScreen(sessionToken);
        }
    }

    @Override
    public void backToHomeScreen(String sessionToken) {
        CallbackInterface callback = sessionCallbacks.get(sessionToken);
        if (callback != null) {
            SwingUtilities.invokeLater(callback::showHomeScreen);
        } else {
            // Handle the case where no callback is found for the session token
            System.err.println("No callback found for session token: " + sessionToken);
        }
    }

    private void cancelGameSession(String gameId) throws SQLException {
        // Retrieve the game session using the provided game ID
        GameSession gameSession = activeGameLobbies.get(gameId);

        // Check if the game session exists
        if (gameSession == null) {
            System.out.println("Invalid game session ID");
            return;
        }

        gameSession.setStatus(GameSession.GameStatus.CANCELLED);

        // Remove the game session from the active game lobbies map
        activeGameLobbies.remove(gameId);

        gameSessionDAO.saveGameSession(gameSession);
        notifyPlayersAboutChanges(gameSession);
    }

    /**
     *
     * HELPER METHODS FOR GAME SETUP
     *
     * */

    public void notifyPlayersAboutChanges(GameSession session) {
        List<PlayerInfo> playerData = collectPlayerData(session);

        System.out.println("Player data list:");
        for (PlayerInfo info : playerData) {
            System.out.println(info);  // This calls the toString() method of PlayerInfo
        }

        String gameToken = session.getGameToken();
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.UpdateLobGUI(playerData.toArray(new PlayerInfo[0]), gameToken);
                } catch (Exception e) {
                    System.err.println("Error updating GUI for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }

    public void startTimerForGui(GameSession session, int seconds){
        List<PlayerInfo> playerData = collectPlayerData(session);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.startLobbyTimer(seconds);
                } catch (Exception e) {
                    System.err.println("Error updating GUI for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }
    private List<PlayerInfo> collectPlayerData(GameSession session) {
        List<PlayerInfo> playerData = new ArrayList<>();
        session.getPlayers().forEach((token, position) -> {
            String username = retrievePlayerFromSessionToken(token);
            int score = session.getPlayerScore(token);
            int roundsWon = session.getPlayerRoundsWon().getOrDefault(token, 0); // Get rounds won from the map
            playerData.add(new PlayerInfo(token, username, position, score, roundsWon));
        });
        return playerData;
    }

    private void updateReadyStatusForPlayers(GameSession session) {
        List<PlayerInfo> playerData = collectPlayerData(session);
        boolean[] readyStatus = new boolean[playerData.size()];

        for (int i = 0; i < playerData.size(); i++) {
            PlayerInfo info = playerData.get(i);
            readyStatus[i] = session.isReady(info.sessionToken);
        }

        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.updatePlayerReadyStatus(playerData.toArray(new PlayerInfo[0]), readyStatus);
                } catch (Exception e) {
                    System.err.println("Error updating player ready status for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }
    private void startGameSession(GameSession session) throws SQLException {
        List<PlayerInfo> playerData = collectPlayerData(session);
        char[] charArrayList = generateRandomCharArray();
        session.setRandomLetters(charArrayList);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.startGameGUI(playerData.toArray(new PlayerInfo[0]), charArrayList);
                } catch (Exception e) {
                    System.err.println("Error updating GUI for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });

        session.setStatus(GameSession.GameStatus.ACTIVE);
        System.out.println("Game session started with ID: " + session.getSessionId());

        // Start the round timer
        gameSessionDAO.saveGameSession(session);
        startRoundTimer(session);
    }
    private void startRoundTimer(GameSession session) {
        notifyRoundTimerToPlayers(session, session.getDURATION_PER_ROUNDS());
        scheduler.schedule(() -> {
            try {
                completeRound(session);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, session.getDURATION_PER_ROUNDS(), TimeUnit.SECONDS);

    }
    private void notifyRoundTimerToPlayers(GameSession session, int durationSeconds) {
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.startRoundTimer(durationSeconds);
                } catch (Exception e) {
                    System.err.println("Error starting round timer for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }
    private void notifyRoundWinnerToPlayers(GameSession session, String winnerToken) {
        String winnerName = retrievePlayerFromSessionToken(winnerToken);
        List<PlayerInfo> playerData = collectPlayerData(session);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.displayRoundWinner(playerData.toArray(new PlayerInfo[0]), winnerName);
                } catch (Exception e) {
                    System.err.println("Error notifying round winner for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }
    private void notifyGameWinner(GameSession session, String winnerToken) {
        String winnerName = retrievePlayerFromSessionToken(winnerToken);
        List<PlayerInfo> playerData = collectPlayerData(session);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.displayOverallWinner(playerData.toArray(new PlayerInfo[0]), winnerName);
                } catch (Exception e) {
                    System.err.println("Error notifying overall winner for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }
    private void notifyTieToPlayers(GameSession session) {
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.displayTie();
                } catch (Exception e) {
                    System.err.println("Error notifying tie for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });
    }
    private void completeRound(GameSession session) throws SQLException {
        String roundWinner = session.determineRoundWinner();
        if (roundWinner != null) {
            System.out.println("Round winner is: " + retrievePlayerFromSessionToken(roundWinner));
            session.incrementRoundWinCount(roundWinner);
            session.resetScoresForNextRound();
            notifyRoundWinnerToPlayers(session, roundWinner);
            addRoundsToUserDB(roundWinner);
            updateLeaderboards();
        } else {
            System.out.println("No winner declared for the round due to a tie.");
            session.resetScoresForNextRound();
            notifyTieToPlayers(session);
        }

        String overallWinner = session.determineOverallWinner();
        if (overallWinner != null) {
            System.out.println("Overall game winner is: " + retrievePlayerFromSessionToken(overallWinner));
            notifyGameWinner(session, overallWinner);
            session.setStatus(GameSession.GameStatus.COMPLETED);
        } else {
            notifyPlayersAboutChanges(session);
            session.getPlayers().forEach((token, position) -> {
                CallbackInterface callback = sessionCallbacks.get(token);
                if (callback != null) {
                    try {
                        callback.startRoundDelayTimer(session.getDELAY_PER_ROUNDS());
                    } catch (Exception e) {
                        System.err.println("Error starting round delay timer for token: " + token);
                        e.printStackTrace();
                    }
                }
            });

            // Reset scores for the next round
            session.resetScoresForNextRound();
            notifyPlayersAboutChanges(session);
            gameSessionDAO.saveGameSession(session);

            // Schedule the next round after the delay
            scheduler.schedule(() -> {
                try {
                    startNextRound(session);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, session.getDELAY_PER_ROUNDS(), TimeUnit.SECONDS);
        }
    }
    private void updateLeaderboards() {
        List<User> topPlayers = userDAO.getTopPlayersByRoundsWon();

        Users[] topPlayersArray = convertListToUsersArray(topPlayers);

        for (Map.Entry<String, CallbackInterface> entry : sessionCallbacks.entrySet()) {
            CallbackInterface callback = entry.getValue();
            if (callback != null) {
                callback.updateLeaderBoardGUI(topPlayersArray);
            }
        }
    }


    private TestingGrounds.GameSystem.Users[] convertListToUsersArray(List<TestingGrounds.ReferenceClasses.User> topPlayers) {
        TestingGrounds.GameSystem.Users[] usersArray = new TestingGrounds.GameSystem.Users[topPlayers.size()];
        for (int i = 0; i < topPlayers.size(); i++) {
            TestingGrounds.ReferenceClasses.User refUser = topPlayers.get(i);
            TestingGrounds.GameSystem.Users user = new TestingGrounds.GameSystem.Users();
            user.playerId = refUser.getPlayerId() == null ? "defaultId" : refUser.getPlayerId();
            user.username = refUser.getUsername() == null ? "defaultUsername" : refUser.getUsername();
            user.sessionToken = refUser.getSessionToken() == null ? "defaultSessionToken" : refUser.getSessionToken();
            user.inGame = refUser.isInGame();
            user.roundsWon = refUser.getScore();
            user.currentGameToken = refUser.getCurrentGameToken() == null ? "defaultGameToken" : refUser.getCurrentGameToken();
            usersArray[i] = user;
        }
        return usersArray;
    }
    private void startNextRound(GameSession session) throws SQLException {
        char[] charArrayList = generateRandomCharArray();
        session.setRandomLetters(charArrayList);
        session.setStatus(GameSession.GameStatus.ACTIVE);

        List<PlayerInfo> playerData = collectPlayerData(session);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.startGameGUI(playerData.toArray(new PlayerInfo[0]), charArrayList);
                    callback.startRoundTimer(session.getDURATION_PER_ROUNDS());  // Reset the round timer GUI
                } catch (Exception e) {
                    System.err.println("Error updating GUI for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });

        System.out.println("Next round started for session ID: " + session.getSessionId());

        gameSessionDAO.saveGameSession(session);
        startRoundTimer(session);
    }
    private void sendTimeoutExceptionToHost(String sessionToken, GameSession session) {
        CallbackInterface callback = sessionCallbacks.get(sessionToken);
        if (callback != null) {
            try {
                System.out.print("Unable to start the game due to lack of players ready in time.");
            } catch (Exception e) {
                System.err.println("Error sending TimeoutException to host: " + sessionToken);
                e.printStackTrace();
            }
        }
    }
    private char[] generateRandomCharArray() {
        List<Character> consonantsList = new ArrayList<>();
        List<Character> vowelsList = new ArrayList<>();

        for (int i = 0; i < NUM_CONSONANTS; i++) {
            consonantsList.add(CONSONANTS[random.nextInt(CONSONANTS.length)]);
        }

        for (int i = 0; i < NUM_VOWELS; i++) {
            vowelsList.add(VOWELS[random.nextInt(VOWELS.length)]);
        }

        ArrayList<Character> charList = new ArrayList<>(consonantsList);
        charList.addAll(vowelsList);
        Collections.shuffle(charList, random);

        char[] charArray = new char[charList.size()];
        for (int i = 0; i < charList.size(); i++) {
            charArray[i] = charList.get(i);
        }

        return charArray;
    }
    private String generateSecureToken() {
        return UUID.randomUUID().toString();
    }
    private String generateGameToken() throws SQLException {
        String token;
        do {
            token = TokenGenerator.generateToken();
        } while (!gameSessionDAO.isTokenUnique(token));
        return token;
    }
    private boolean validateSessionToken(String sessionToken) {
        return sessionTokens.containsKey(sessionToken);
    }
    public String retrievePlayerFromSessionToken(String sessionToken) {
        return sessionTokens.get(sessionToken);
    }
    public void addRoundsToUserDB(String sessionToken) throws SQLException{
        if (sessionToken != null) {
            userDAO.updateOverallRoundsWon(sessionToken, 1);
        } else {
            System.err.println("No username found for session token: " + sessionToken);
        }
    }


    @Override
    public boolean ping() {
        return true;
    }

    public boolean isServerConnected() throws lossConnection {
        try {
            // Perform a dummy operation to check server connectivity
            ping(); // Assuming a ping method exists on the server
            return true; // Server is reachable
        } catch (Exception e) {
            // Catch any exception that might occur during the connectivity check
            // Then, throw the lossConnection exception with appropriate context
            throw new lossConnection();
        }
    }
    /**
     *
     * ADMIN RELATED METHODS
     *
     * */

    @Override
    public void updateSecondsPerWaiting(int newSeconds) {
        try {
            gameSessionDAO.updateLobbyWaitingTimeForWaitingLobbies(newSeconds);
            sessionCallbacks.forEach((token, callback) -> {
                if (callback != null) {
                    try {
                        callback.updateWaitingTimeLabel(newSeconds);
                    } catch (Exception e) {
                        System.err.println("Error updating waiting time label for token: " + token);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No callback registered for token: " + token);
                }
            });

            updateLobbiesList();
        } catch (SQLException e) {
            System.err.println("Error fetching seconds per round: " + e.getMessage());
            durationPerRound  = 10;
        }
    }
    @Override
    public void editRoundTime(int newSeconds) {
        try {
            gameSessionDAO.updateDurationPerRoundForWaitingLobbies(newSeconds);
            sessionCallbacks.forEach((token, callback) -> {
                if (callback != null) {
                    try {
                        callback.updateWaitingTimeLabel(newSeconds);
                    } catch (Exception e) {
                        System.err.println("Error updating Round time label for token: " + token);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No callback registered for token: " + token);
                }
            });

            updateLobbiesList();
        } catch (SQLException e) {
            System.err.println("Error fetching seconds per round: " + e.getMessage());
            durationPerRound  = 60;
        }
    }
    @Override
    public void editNumRounds(int newSeconds) {
        try {
            gameSessionDAO.updateWinningRoundsForWaitingLobbies(newSeconds);
            sessionCallbacks.forEach((token, callback) -> {
                if (callback != null) {
                    try {
                        callback.updateWaitingTimeLabel(newSeconds);
                    } catch (Exception e) {
                        System.err.println("Error updating waiting Number of rounds for token: " + token);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No callback registered for token: " + token);
                }
            });

            updateLobbiesList();
        } catch (SQLException e) {
            System.err.println("Error fetching seconds per round: " + e.getMessage());
            durationPerRound  = 60;
        }
    }

    private void updateAccountsList(){
        List<User> players = userDAO.getTopPlayersByRoundsWon();
        Users[] playersArray = convertListToUsersArray(players);

        for (Map.Entry<String, CallbackInterface> entry : adminSessionCallbacks.entrySet()) {
            CallbackInterface callback = entry.getValue();
            if (callback != null) {
                callback.updateAccountsList(playersArray);
            }
        }
    }

    private void updateLobbiesList() {
        try{
            List<GameSession> lobbies = gameSessionDAO.getAllGameSessions();
            Lobbies[] lobbiesArray = convertListToArray(lobbies);
            for (Map.Entry<String, CallbackInterface> entry : adminSessionCallbacks.entrySet()) {
                CallbackInterface callback = entry.getValue();
                if (callback != null) {
                    callback.updateGameSessions(lobbiesArray);
                }
            }
        } catch (SQLException e){
            System.err.println(e);
        }
    }

    private TestingGrounds.GameSystem.Lobbies[] convertListToArray(List<TestingGrounds.ReferenceClasses.GameSession> sessions) {
        // Create an array of Lobbies to hold the converted GameSession data
        TestingGrounds.GameSystem.Lobbies[] lobbiesArray = new TestingGrounds.GameSystem.Lobbies[sessions.size()];

        for (int i = 0; i < sessions.size(); i++) {
            TestingGrounds.ReferenceClasses.GameSession session = sessions.get(i);

            // Instantiate a new Lobbies object
            TestingGrounds.GameSystem.Lobbies lobby = new TestingGrounds.GameSystem.Lobbies();

            // Map the properties from GameSession to Lobbies
            lobby.gameToken = session.getGameToken() != null ? session.getGameToken() : "defaultGameToken";
            lobby.maxPlayers = session.getMAX_PLAYERS(); // Assuming getMAX_PLAYERS always returns a valid int
            lobby.winningRounds = session.getWINNING_ROUNDS(); // Assuming getWINNING_ROUNDS always returns a valid int
            lobby.lobbyWaitingTime = session.getLOBBY_WAITING_TIME(); // Assuming getLOBBY_WAITING_TIME always returns a valid int
            lobby.durationPerRound = session.getDURATION_PER_ROUNDS(); // Assuming getDURATION_PER_ROUNDS always returns a valid int
            lobby.delayPerRound = session.getDELAY_PER_ROUNDS(); // Assuming getDELAY_PER_ROUNDS always returns a valid int
            lobby.status = session.getStatus() != null ? session.getStatus().toString() : "UNKNOWN"; // Properly converting enum to string if needed
            lobby.playerCount = session.getCurrentPlayerCount(); // Assuming getCurrentPlayerCount always returns a valid int

            lobbiesArray[i] = lobby;
        }

        return lobbiesArray;
    }



    @Override
    public String[] viewPlayers(String name) {
// Retrieve the GameSession associated with the provided name
        GameSession gameSession = activeGameLobbies.get(name);

        // Check if the GameSession exists
        if (gameSession != null) {
            // Retrieve the players map from the GameSession
            Map<String, Integer> players = gameSession.getPlayers();

            // Create a list to hold the player names
            List<String> playerNamesList = new ArrayList<>();

            // Map session tokens to usernames and add to playerNamesList
            for (String sessionToken : players.keySet()) {
                String username = sessionTokens.get(sessionToken);
                if (username != null) {
                    playerNamesList.add(username);
                }
            }

            // Convert the list to an array and return
            return playerNamesList.toArray(new String[0]);
        } else {
            // Handle the case where the GameSession does not exist
            System.out.println("No active game lobby found with the name: " + name);
            return new String[0]; // Return an empty array if no game session is found
        }
    }


    @Override
    public String getLetters(String sessionToken, String gameId) {
        return null;
    }

    @Override
    public boolean _is_a(String repositoryIdentifier) {
        return false;
    }

    @Override
    public boolean _is_equivalent(Object other) {
        return false;
    }

    @Override
    public boolean _non_existent() {
        return false;
    }

    @Override
    public int _hash(int maximum) {
        return 0;
    }

    @Override
    public Object _duplicate() {
        return null;
    }

    @Override
    public void _release() {

    }

    @Override
    public Object _get_interface_def() {
        return null;
    }

    @Override
    public Request _request(String operation) {
        return null;
    }

    @Override
    public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result) {
        return null;
    }

    @Override
    public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result, ExceptionList exclist, ContextList ctxlist) {
        return null;
    }

    @Override
    public Policy _get_policy(int policy_type) {
        return null;
    }

    @Override
    public DomainManager[] _get_domain_managers() {
        return new DomainManager[0];
    }

    @Override
    public Object _set_policy_override(Policy[] policies, SetOverrideType set_add) {
        return null;
    }




}