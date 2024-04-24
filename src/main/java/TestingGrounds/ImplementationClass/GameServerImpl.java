package TestingGrounds.ImplementationClass;

import TestingGrounds.Client_Java.GameSession;
import TestingGrounds.Client_Java.User;
import TestingGrounds.GameSystem.GameServerPOA;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServerImpl extends GameServerPOA implements Object {

    Map<String, String> sessionTokens = new HashMap<>();
    Map<String, GameSession> activeGameLobbies = new HashMap<>();

    @Override
    public boolean login(String username, String password, org.omg.CORBA.StringHolder sessionToken) {
        if (username != null && password != null){
            String token = generateSecureToken();
            sessionToken.value = token;
            sessionTokens.put(token, username);
            return true;
        } else
            return false;
    }

    @Override
    public boolean logout(String sessionToken) {
        return false;
    }

    @Override
    public String hostGame(String sessionToken){
        if (!validateSessionToken(sessionToken)) {
            // ADD USER DEFINED EXCEPTION
        }
        GameSession newGameSession = new GameSession();
        activeGameLobbies.put(newGameSession.getSessionId(), newGameSession);
        System.out.println("Game session created with ID: " + newGameSession.getSessionId());
        return newGameSession.getSessionId();
    }

    @Override
    public boolean joinRandomGame(String sessionToken) {
        return false;
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
    public boolean _is_equivalent(Object other) {
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