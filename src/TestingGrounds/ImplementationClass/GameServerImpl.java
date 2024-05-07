package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.InvalidCredentials;
import TestingGrounds.ReferenceClasses.GameSession;
import TestingGrounds.GameSystem.CallbackInterface;
import TestingGrounds.GameSystem.GameServerPOA;
import TestingGrounds.GameSystem.PlayerInfo;
import TestingGrounds.ReferenceClasses.User;
import TestingGrounds.Utilities.DataAccessObjects.UserDAO;
import TestingGrounds.Utilities.WordValidator;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
        return false;
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
    public boolean startGame(String sessionToken, String gameId) {
        GameSession session = activeGameLobbies.get(gameId);
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

        return true;
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
                System.out.println("Word has already been guessed: " + word);
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
                System.out.println("Valid word: " + word);
            }
        } else {
            System.out.println("Invalid word: " + word);
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