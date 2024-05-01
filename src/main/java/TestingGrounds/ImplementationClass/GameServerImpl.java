package TestingGrounds.ImplementationClass;

import TestingGrounds.Client_Java.GameSession;
import TestingGrounds.Client_Java.User;
import TestingGrounds.GameSystem.CallbackInterface;
import TestingGrounds.GameSystem.GameServerPOA;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

import java.util.*;
import java.util.stream.Collectors;

public class GameServerImpl extends GameServerPOA implements Object {

    Map<String, String> sessionTokens = new HashMap<>();
    Map<String, GameSession> activeGameLobbies = new HashMap<>();


    @Override
    public boolean login(String username, String password, org.omg.CORBA.StringHolder sessionToken) {
        if (username != null && password != null){
            String token = generateSecureToken();
            sessionToken.value = token;
            sessionTokens.put(token, username);
            System.out.println(username + " " + token);
            return true;
        } else
            return false;
    }

    @Override
    public boolean logout(String sessionToken) {
        return false;
    }

    @Override
    public String hostGame(String sessionToken, CallbackInterface gci){
        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
        }
        GameSession newGameSession = new GameSession();
        activeGameLobbies.put(newGameSession.getSessionId(), newGameSession);
        System.out.println("Game session created with ID: " + newGameSession.getSessionId());
        return newGameSession.getSessionId();
    }

    @Override
    public boolean joinRandomGame(String sessionToken, CallbackInterface gci) {
        if (!validateSessionToken(sessionToken)) {
            System.out.println("Invalid session token");
            return false;
        }

        List<GameSession> availableSessions = activeGameLobbies.values().stream()
                .filter(GameSession::canJoin) // Assuming GameSession has a canJoin method to check if it is joinable
                .collect(Collectors.toList());

        if (availableSessions.isEmpty()) {
            System.out.println("No available game sessions to join");
            return false;
        }

        int randomIndex = new Random().nextInt(availableSessions.size());
        GameSession selectedSession = availableSessions.get(randomIndex);

        String username = sessionTokens.get(sessionToken);
        if (username == null) {
            System.out.println("No username found for the given session token");
            return false;
        }

        User user = new User("PlayerID", sessionToken);

        selectedSession.addPlayer(user);
        System.out.println("Player " + username + " added to game " + selectedSession.getSessionId());

       return true;
    }

    @Override
    public boolean joinGame(String sessionToken, String gameId) {
        System.out.println("Player with session token " + sessionToken + " attempting to join game " + gameId);
        GameSession session = activeGameLobbies.get(gameId);

        //TO DO: ADD PLAYER TO THE SESSION
        return true;
    }

    @Override
    public boolean startGame(String sessionToken, String gameId) {
        return false;
    }

    @Override
    public void submitWord(String sessionToken, String gameId, String word) {

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
        // Example: Generate a UUID as a secure token
        return UUID.randomUUID().toString();
    }

    private boolean validateSessionToken(String sessionToken) {
        return sessionTokens.containsKey(sessionToken);
    }

    public String retrievePlayerFromSessionToken(String sessionToken) {
        return sessionTokens.get(sessionToken);
    }
}