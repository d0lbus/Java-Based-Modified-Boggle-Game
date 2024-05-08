package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.InvalidCredentials;
import TestingGrounds.ReferenceClasses.GameSession;
import TestingGrounds.GameSystem.CallbackInterface;
import TestingGrounds.GameSystem.GameServerPOA;
import TestingGrounds.GameSystem.PlayerInfo;
import TestingGrounds.ReferenceClasses.User;
import TestingGrounds.Utilities.DataAccessObjects.GameSettingsDAO;
import TestingGrounds.Utilities.DataAccessObjects.UserDAO;
import TestingGrounds.Utilities.WordValidator;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

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
    private static final char[] CONSONANTS = "BCDFGHJKLMNPQRSTVWXYZ".toCharArray();
    private static final char[] VOWELS = "AEIOU".toCharArray();
    private static final int NUM_CONSONANTS = 13;
    private static final int NUM_VOWELS = 7;
    private UserDAO userDAO = new UserDAO();
    private final Random random = new Random();
    private int durationPerRound;
    private int durationPerWaiting;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService roundScheduler = Executors.newScheduledThreadPool(1);


    @Override
    public boolean login(String username, String password, org.omg.CORBA.StringHolder sessionToken, CallbackInterface cbi) {
        try {
            User user = userDAO.getUserByUsername(username);

            if (user == null || !userDAO.validatePassword(user, password)) {
                throw new InvalidCredentials();
            }

            String token = generateSecureToken();
            sessionToken.value = token;

            // Update user session in the database
            user.setSessionToken(token);
            userDAO.updateSessionToken(user, token);

            sessionTokens.put(token, username);
            sessionCallbacks.put(token, cbi);

            System.out.println("Callback registered for " + username + " with token: " + token);
            return true;
        } catch (SQLException | InvalidCredentials e) {
            System.err.println("Login error: " + e.getMessage());
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
    public String hostGame(String sessionToken, CallbackInterface cbi){
        if (cbi == null) {
            throw new RuntimeException("Callback interface cannot be null.");
        }

        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
        }
        GameSession newGameSession = new GameSession();
        activeGameLobbies.put(newGameSession.getSessionId(), newGameSession);
        System.out.println("Game session created with ID: " + newGameSession.getSessionId());

        newGameSession.addPlayer(sessionToken);
        notifyPlayersAboutChanges(newGameSession);
        return newGameSession.getSessionId();
    }
    @Override
    public String joinRandomGame(String sessionToken, CallbackInterface gci) {
        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
        }

        List<GameSession> availableSessions = activeGameLobbies.values().stream()
                .filter(GameSession::canJoin)
                .collect(Collectors.toList());

        if (availableSessions.isEmpty()) {
            System.out.println("No available game sessions to join");
        }

        int randomIndex = new Random().nextInt(availableSessions.size());
        GameSession selectedSession = availableSessions.get(randomIndex);
        selectedSession.addPlayer(sessionToken);
        System.out.println("Player added to game " + selectedSession.getSessionId());

        notifyPlayersAboutChanges(selectedSession);
        return selectedSession.getSessionId();
    }
    @Override
    public String joinGame(String sessionToken, String gameId) {
        System.out.println("Player with session token " + sessionToken + " attempting to join game " + gameId);
        GameSession session = activeGameLobbies.get(gameId);

        //TO DO: ADD PLAYER TO THE SESSION
        return session.getSessionId();
    }
    public void notifyPlayersAboutChanges(GameSession session) {
        List<PlayerInfo> playerData = collectPlayerData(session);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.UpdateLobGUI(playerData.toArray(new PlayerInfo[0]));
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
            playerData.add(new PlayerInfo(token, username, position, score));
        });
        return playerData;
    }
    @Override
    public boolean startGame(String sessionToken, String gameToken) {
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

                startTimerForGui(session, fetchSecondsPerWaiting());
                session.setTimerRunning(true);

                scheduler.schedule(() -> {
                    if (!session.allPlayersReady()) {
                        System.out.println("Not all players are ready within the specified time.");
                        sendTimeoutExceptionToHost(sessionToken, session);
                    } else {
                        if (session.getStatus() != GameSession.GameStatus.ACTIVE) {
                            startGameSession(session);
                        }
                    }
                    session.setTimerRunning(false);
                }, fetchSecondsPerWaiting(), TimeUnit.SECONDS);
            } else {
                System.out.println("Timer is already running.");
            }
        } else {
            if (session.isTimerRunning()) {
                session.markReady(sessionToken);
                System.out.println("Player marked as ready: " + sessionToken);
                updateReadyStatusForPlayers(session);
                if (session.allPlayersReady() && session.getStatus() != GameSession.GameStatus.ACTIVE) {
                    startGameSession(session);
                }
            } else {
                System.out.println("Timer is not running. Cannot mark player as ready.");
            }
        }

        notifyPlayersAboutChanges(session);
        return true;
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
    private void startGameSession(GameSession session) {
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
        startRoundTimer(session);
    }
    private void startRoundTimer(GameSession session) {
        notifyRoundTimerToPlayers(session, fetchSecondsPerRound());
        scheduler.schedule(() -> {
            completeRound(session);
        }, fetchSecondsPerRound(), TimeUnit.SECONDS);
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
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.displayRoundWinner(winnerName);
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
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.displayOverallWinner(winnerName);
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
    private void completeRound(GameSession session) {

        String roundWinner = session.determineRoundWinner();
        if (roundWinner != null) {
            session.incrementRoundWinCount(roundWinner);
            System.out.println("Round winner is: " + retrievePlayerFromSessionToken(roundWinner));
            notifyRoundWinnerToPlayers(session, roundWinner);
        } else {
            System.out.println("No winner declared for the round due to a tie.");
            notifyTieToPlayers(session);
        }

        String overallWinner = session.determineOverallWinner();
        if (overallWinner != null) {
            System.out.println("Overall game winner is: " + retrievePlayerFromSessionToken(overallWinner));
            notifyGameWinner(session, overallWinner);
            session.setStatus(GameSession.GameStatus.COMPLETED);
        } else {
            int roundDelaySeconds = 5;
            notifyPlayersAboutChanges(session);

            session.getPlayers().forEach((token, position) -> {
                CallbackInterface callback = sessionCallbacks.get(token);
                if (callback != null) {
                    try {
                        callback.startRoundDelayTimer(roundDelaySeconds);
                    } catch (Exception e) {
                        System.err.println("Error starting round delay timer for token: " + token);
                        e.printStackTrace();
                    }
                }
            });

            // Reset scores for the next round
            session.resetScoresForNextRound();
            notifyPlayersAboutChanges(session);

            // Schedule the next round after the delay
            scheduler.schedule(() -> startNextRound(session), roundDelaySeconds, TimeUnit.SECONDS);
        }
    }
    private void startNextRound(GameSession session) {
        char[] charArrayList = generateRandomCharArray();
        session.setRandomLetters(charArrayList);
        session.setStatus(GameSession.GameStatus.ACTIVE);

        List<PlayerInfo> playerData = collectPlayerData(session);
        session.getPlayers().forEach((token, position) -> {
            CallbackInterface callback = sessionCallbacks.get(token);
            if (callback != null) {
                try {
                    callback.startGameGUI(playerData.toArray(new PlayerInfo[0]), charArrayList);
                    callback.startRoundTimer(fetchSecondsPerRound());  // Reset the round timer GUI
                } catch (Exception e) {
                    System.err.println("Error updating GUI for token: " + token);
                    e.printStackTrace();
                }
            } else {
                System.err.println("No callback registered for token: " + token);
            }
        });

        System.out.println("Next round started for session ID: " + session.getSessionId());

        // Start the round timer
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
    @Override
    public void submitWord(String sessionToken, String gameToken, String word) {
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
        }
    }
    private int fetchSecondsPerRound() {
        GameSettingsDAO settingsDAO = new GameSettingsDAO();
        try {
            durationPerRound = settingsDAO.fetchSecondsPerRound();
        } catch (SQLException e) {
            System.err.println("Error fetching seconds per round: " + e.getMessage());
            durationPerRound  = 30; // default
        }

        return durationPerRound;
    }
    private int fetchSecondsPerWaiting() {
        GameSettingsDAO settingsDAO = new GameSettingsDAO();
        try {
            durationPerWaiting = settingsDAO.fetchSecondsPerRound();
        } catch (SQLException e) {
            System.err.println("Error fetching seconds per round: " + e.getMessage());
            durationPerWaiting  = 10; // default
        }

        return durationPerWaiting;
    }

    @Override
    public void updateSecondsPerWaiting(int newSeconds) {
        GameSettingsDAO settingsDAO = new GameSettingsDAO();
        try {
            settingsDAO.updateSecondsPerWaiting(newSeconds);
        } catch (SQLException e) {
            System.err.println("Error fetching seconds per round: " + e.getMessage());
            durationPerRound  = 10;
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

    private String generateSecureToken() {
        return UUID.randomUUID().toString();
    }

    private boolean validateSessionToken(String sessionToken) {
        return sessionTokens.containsKey(sessionToken);
    }

    public String retrievePlayerFromSessionToken(String sessionToken) {
        return sessionTokens.get(sessionToken);
    }


}