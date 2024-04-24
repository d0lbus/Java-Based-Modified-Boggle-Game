package TestingGrounds.Client_Java;

public class User {
    private String playerId;
    private String username;
    private boolean inGame;
    private int score;

    public User(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
        this.inGame = false;
        this.score = 0;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
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
