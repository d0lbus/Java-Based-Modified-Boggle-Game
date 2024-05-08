package TestingGrounds.ReferenceClasses;

public class User {
    private String playerId;
    private String username;
    private String firstName;
    private String lastName;
    private String sessionToken;
    private boolean inGame;
    private int score;
    private String currentGameToken;


    public User(String playerId, String username, String sessionToken, boolean inGame, int score, String currentGameToken) {
        this.playerId = getPlayerId();
        this.username = username;
        this.sessionToken = sessionToken;
        this.inGame = false;
        this.score = 0;
        this.currentGameToken = "";
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void resetForNewGame() {
        this.inGame = true;
        this.score = 0;
    }


    public void leaveGame() {
        this.inGame = false;
        this.score = 0;  // (RESET TO 0 OR KEEP THE SCORE)
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    public void setFirstName(String firstName) {
        this.playerId = firstName;
    }
    public void setLastName(String lastName) {
        this.playerId = lastName;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getCurrentGameToken() {
        return currentGameToken;
    }

    public void setCurrentGameToken(String currentGameToken) {
        this.currentGameToken = currentGameToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId='" + playerId + '\'' +
                ", username='" + username + '\'' +
                ", inGame=" + inGame +
                ", score=" + score +
                '}';
    }
}
