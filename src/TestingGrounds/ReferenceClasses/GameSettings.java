package TestingGrounds.ReferenceClasses;

public class GameSettings {
    private int roundsToWin;
    private int secondsPerRound;

    public GameSettings(int roundsToWin, int secondsPerRound) {
        this.roundsToWin = roundsToWin;
        this.secondsPerRound = secondsPerRound;
    }

    public int getRoundsToWin() {
        return roundsToWin;
    }

    public void setRoundsToWin(int roundsToWin) {
        this.roundsToWin = roundsToWin;
    }

    public int getSecondsPerRound() {
        return secondsPerRound;
    }

    public void setSecondsPerRound(int secondsPerRound) {
        this.secondsPerRound = secondsPerRound;
    }
}