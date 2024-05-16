package TestingGrounds.ImplementationClass;


import TestingGrounds.GameSystem.CallbackInterfacePOA;
import TestingGrounds.GameSystem.Lobbies;
import TestingGrounds.GameSystem.PlayerInfo;
import TestingGrounds.GameSystem.Users;
import TestingGrounds.View.AdminGUIFrame;
import TestingGrounds.View.ClientGUIFrame;


import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameClientCallbackImpl extends CallbackInterfacePOA {
    private ClientGUIFrame gui;
    private AdminGUIFrame guiAdmin;
    private final Random random = new Random();
    private static final String ICONS_PATH = "src/Icons";
    private Map<String, Icon> usernameToIconMap = new HashMap<>();
    private List<Icon> availableIcons = new ArrayList<>();
    private Set<Icon> assignedIcons = new HashSet<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService roundScheduler = Executors.newScheduledThreadPool(1);


    public GameClientCallbackImpl(){

    }
    public GameClientCallbackImpl(ClientGUIFrame gui) {
        this.gui = gui;
        loadAvailableIcons();
    }

    public GameClientCallbackImpl(AdminGUIFrame guiAdmin){
        this.guiAdmin = guiAdmin;
    }

    @Override
    public void CreateLobGUI(String username, String gameId) {
        SwingUtilities.invokeLater(() -> {
            gui.getPlayer1pic().setIcon(assignIcon(username));
        });
    }

    @Override
    public void UpdateLobGUI(PlayerInfo[] playerData, String gameToken) {
        SwingUtilities.invokeLater(() -> {
           gui.getUsernameLabel().setText(gameToken);


            // Create a map of default player positions
            Map<Integer, String> defaultPositions = new HashMap<>();
            defaultPositions.put(1, "Empty");
            defaultPositions.put(2, "Empty");
            defaultPositions.put(3, "Empty");
            defaultPositions.put(4, "Empty");

            Map<Integer, String> playerIcons = new HashMap<>();

            // Update the map with actual player data
            for (PlayerInfo info : playerData) {
                defaultPositions.put((int) info.position, info.username);
            }

            // Set GUI components based on the updated map
            gui.getPlayer1username().setText(defaultPositions.get(1));
            gui.getPlayer2username().setText(defaultPositions.get(2));
            gui.getPlayer3username().setText(defaultPositions.get(3));
            gui.getPlayer4username().setText(defaultPositions.get(4));

            gui.getPlayer1pic().setIcon(defaultPositions.get(1).equals("Empty") ? null : assignIcon(defaultPositions.get(1)));
            gui.getPlayer2pic().setIcon(defaultPositions.get(2).equals("Empty") ? null : assignIcon(defaultPositions.get(2)));
            gui.getPlayer3pic().setIcon(defaultPositions.get(3).equals("Empty") ? null : assignIcon(defaultPositions.get(3)));
            gui.getPlayer4pic().setIcon(defaultPositions.get(4).equals("Empty") ? null : assignIcon(defaultPositions.get(4)));



        });
    }

    @Override
    public void startGameGUI(PlayerInfo[] playerData, char[] charArrayList) {
        SwingUtilities.invokeLater(() -> {
            // Switch to the game panel
            gui.getLayeredPane().removeAll();
            gui.getLayeredPane().add(gui.getGamePanel());
            gui.getLayeredPane().repaint();
            gui.getLayeredPane().revalidate();

            JButton[] letterButtons = {
                    gui.getButton1(), gui.getButton2(), gui.getButton3(), gui.getButton4(),
                    gui.getButton5(), gui.getButton6(), gui.getButton7(), gui.getButton8(),
                    gui.getButton9(), gui.getButton10(), gui.getButton11(), gui.getButton12(),
                    gui.getButton13(), gui.getButton14(), gui.getButton15(), gui.getButton16(),
                    gui.getButton17(), gui.getButton18(), gui.getButton19(), gui.getButton20()
            };

            for (int i = 0; i < charArrayList.length && i < letterButtons.length; i++) {
                letterButtons[i].setText(String.valueOf(charArrayList[i]));
            }

            Map<Integer, String> defaultPositions = new HashMap<>();
            Map<Integer, Integer> defaultScores = new HashMap<>();
            Map<Integer, Integer> defaultRoundsWon = new HashMap<>();

            defaultPositions.put(1, "Empty");
            defaultPositions.put(2, "Empty");
            defaultPositions.put(3, "Empty");
            defaultPositions.put(4, "Empty");

            defaultScores.put(1, 0);
            defaultScores.put(2, 0);
            defaultScores.put(3, 0);
            defaultScores.put(4, 0);

            defaultRoundsWon.put(1, 0);
            defaultRoundsWon.put(2, 0);
            defaultRoundsWon.put(3, 0);
            defaultRoundsWon.put(4, 0);

            for (PlayerInfo info : playerData) {

                defaultPositions.put((int) info.position, info.username);
                defaultScores.put((int) info.position, info.score);
                defaultRoundsWon.put((int) info.position, info.roundsWon);
            }

            gui.getPlayer1gameUsername().setText(defaultPositions.get(1));
            gui.getPlayer2gameUsername().setText(defaultPositions.get(2));
            gui.getPlayer3gameUsername().setText(defaultPositions.get(3));
            gui.getPlayer4gameUsername().setText(defaultPositions.get(4));

            gui.getPlayer1gamePoints().setText(String.valueOf(defaultScores.get(1)));
            gui.getPlayer2gamePoints().setText(String.valueOf(defaultScores.get(2)));
            gui.getPlayer3gamePoints().setText(String.valueOf(defaultScores.get(3)));
            gui.getPlayer4gamePoints().setText(String.valueOf(defaultScores.get(4)));

            gui.getPlayer1RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(1));
            gui.getPlayer2RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(2));
            gui.getPlayer3RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(3));
            gui.getPlayer4RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(4));

            gui.getPlayer1gamePic().setIcon(defaultPositions.get(1).equals("Empty") ? null : assignIcon(defaultPositions.get(1)));
            gui.getPlayer2gamePic().setIcon(defaultPositions.get(2).equals("Empty") ? null : assignIcon(defaultPositions.get(2)));
            gui.getPlayer3gamePic().setIcon(defaultPositions.get(3).equals("Empty") ? null : assignIcon(defaultPositions.get(3)));
            gui.getPlayer4gamePic().setIcon(defaultPositions.get(4).equals("Empty") ? null : assignIcon(defaultPositions.get(4)));

        });
    }

    @Override
    public void updatePlayerReadyStatus(PlayerInfo[] playerData, boolean[] readyStatus) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Updating player ready status for all players");

            Map<Integer, String> defaultPositions = new HashMap<>();
            defaultPositions.put(1, "Empty");
            defaultPositions.put(2, "Empty");
            defaultPositions.put(3, "Empty");
            defaultPositions.put(4, "Empty");

            Map<Integer, Boolean> defaultReadyStatus = new HashMap<>();
            defaultReadyStatus.put(1, false);
            defaultReadyStatus.put(2, false);
            defaultReadyStatus.put(3, false);
            defaultReadyStatus.put(4, false);

            for (int i = 0; i < playerData.length; i++) {
                PlayerInfo info = playerData[i];
                int position = (int) info.position;
                defaultPositions.put(position, info.username);
                defaultReadyStatus.put(position, readyStatus[i]);
            }

            updateReadyStatusLabel(gui.getPlayer1Ready(), defaultPositions.get(1), defaultReadyStatus.get(1));
            updateReadyStatusLabel(gui.getPlayer2Ready(), defaultPositions.get(2), defaultReadyStatus.get(2));
            updateReadyStatusLabel(gui.getPlayer3Ready(), defaultPositions.get(3), defaultReadyStatus.get(3));
            updateReadyStatusLabel(gui.getPlayer4Ready(), defaultPositions.get(4), defaultReadyStatus.get(4));
        });
    }

    private void updateReadyStatusLabel(JLabel label, String username, boolean isReady) {
        if (username.equals("Empty")) {
            label.setText("Not Ready");
            label.setForeground(Color.RED);
        } else {
            label.setText(isReady ? "Ready" : "Not Ready");
            label.setForeground(isReady ? Color.GREEN : Color.RED);
        }
    }

    @Override
    public void ReadyStateException() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui, "Cannot start the game with fewer than two players.", "Insufficient Players", JOptionPane.WARNING_MESSAGE);
        });
    }

    @Override
    public void startLobbyTimer(int durationSeconds) {
        scheduler.scheduleAtFixedRate(new Runnable() {
            private int remainingSeconds = durationSeconds;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (remainingSeconds >= 0) {
                        gui.getLobbyTimerLabel().setText("00:" + remainingSeconds);
                        remainingSeconds--;
                    } else {
                        scheduler.shutdown();
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void startRoundTimer(int durationSeconds) {
        roundScheduler.scheduleAtFixedRate(new Runnable() {
            private int remainingSeconds = durationSeconds;
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (remainingSeconds >= 0) {
                        gui.getTimerLabel().setText("00:" + remainingSeconds);
                        remainingSeconds--;
                    } else {
                        scheduler.shutdown();
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void startRoundDelayTimer(int delaySeconds) {
        SwingUtilities.invokeLater(() -> {
            gui.getTimerLabel().setText("Next Round in: " + delaySeconds + " seconds");
        });

        Timer delayTimer = new Timer(1000, new ActionListener() {
            int remainingTime = delaySeconds;

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    gui.getTimerLabel().setText("Next Round in: " + remainingTime + " seconds");
                });

                remainingTime--;
                if (remainingTime < 0) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        delayTimer.start();
    }

    @Override
    public void broadcastGuessedWord(PlayerInfo[] playerData, String word, String playerWhoGuessed) {
        Map<Integer, String> defaultPositions = new HashMap<>();
        Map<Integer, Long> defaultScores = new HashMap<>();
        defaultPositions.put(1, "Empty");
        defaultPositions.put(2, "Empty");
        defaultPositions.put(3, "Empty");
        defaultPositions.put(4, "Empty");

        defaultScores.put(1, 0L);
        defaultScores.put(2, 0L);
        defaultScores.put(3, 0L);
        defaultScores.put(4, 0L);

        for (PlayerInfo info : playerData) {
            defaultPositions.put((int) info.position, info.username);
            defaultScores.put((int) info.position, (long) info.score);
        }

        // Set usernames
        gui.getPlayer1gameUsername().setText(defaultPositions.get(1));
        gui.getPlayer2gameUsername().setText(defaultPositions.get(2));
        gui.getPlayer3gameUsername().setText(defaultPositions.get(3));
        gui.getPlayer4gameUsername().setText(defaultPositions.get(4));

        // Set scores
        gui.getPlayer1gamePoints().setText(String.valueOf(defaultScores.get(1)));
        gui.getPlayer2gamePoints().setText(String.valueOf(defaultScores.get(2)));
        gui.getPlayer3gamePoints().setText(String.valueOf(defaultScores.get(3)));
        gui.getPlayer4gamePoints().setText(String.valueOf(defaultScores.get(4)));

        // Announce to players
        appendToAnnouncement(gui.getAnnouncementTextpane(), playerWhoGuessed + " has guessed the word " + word + "\n");
    }

    @Override
    public void displayRoundWinner(PlayerInfo[] playerData, String winnerName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui, "Round winner: " + winnerName, "Round Winner", JOptionPane.INFORMATION_MESSAGE);

            Map<Integer, String> defaultPositions = new HashMap<>();
            Map<Integer, Long> defaultScores = new HashMap<>();
            Map<Integer, Long> defaultRoundsWon = new HashMap<>();  // Map to hold rounds won

            defaultPositions.put(1, "Empty");
            defaultPositions.put(2, "Empty");
            defaultPositions.put(3, "Empty");
            defaultPositions.put(4, "Empty");

            defaultScores.put(1, 0L);
            defaultScores.put(2, 0L);
            defaultScores.put(3, 0L);
            defaultScores.put(4, 0L);

            defaultRoundsWon.put(1, 0L);  // Initialize rounds won
            defaultRoundsWon.put(2, 0L);
            defaultRoundsWon.put(3, 0L);
            defaultRoundsWon.put(4, 0L);

            for (PlayerInfo info : playerData) {
                defaultPositions.put((int) info.position, info.username);
                defaultScores.put((int) info.position, (long) info.score);
                defaultRoundsWon.put((int) info.position, (long) info.roundsWon);  // Update rounds won from PlayerInfo
            }

            // Set usernames and scores
            gui.getPlayer1gameUsername().setText(defaultPositions.get(1));
            gui.getPlayer2gameUsername().setText(defaultPositions.get(2));
            gui.getPlayer3gameUsername().setText(defaultPositions.get(3));
            gui.getPlayer4gameUsername().setText(defaultPositions.get(4));

            gui.getPlayer1gamePoints().setText(String.valueOf(defaultScores.get(1)));
            gui.getPlayer2gamePoints().setText(String.valueOf(defaultScores.get(2)));
            gui.getPlayer3gamePoints().setText(String.valueOf(defaultScores.get(3)));
            gui.getPlayer4gamePoints().setText(String.valueOf(defaultScores.get(4)));

            // Set rounds won
            gui.getPlayer1RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(1));
            gui.getPlayer2RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(2));
            gui.getPlayer3RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(3));
            gui.getPlayer4RoundsWonInGame().setText("Rounds Won: " + defaultRoundsWon.get(4));
        });
    }

    @Override
    public void displayOverallWinner(PlayerInfo[] playerData, String username) {
        SwingUtilities.invokeLater(() -> {
            gui.getLayeredPane().removeAll();
            gui.getLayeredPane().add(gui.getRankingPanel());

            Map<Integer, String> defaultNames = new HashMap<>();
            Map<Integer, Long> defaultRoundsWon = new HashMap<>();

            defaultNames.put(1, "Empty");
            defaultNames.put(2, "Empty");
            defaultNames.put(3, "Empty");
            defaultNames.put(4, "Empty");

            defaultRoundsWon.put(1, 0L);
            defaultRoundsWon.put(2, 0L);
            defaultRoundsWon.put(3, 0L);
            defaultRoundsWon.put(4, 0L);

            // Populate the maps with data from PlayerInfo
            for (PlayerInfo info : playerData) {
                int position = (int) info.position; // Assumes positions are 1-based and directly map to GUI elements
                defaultNames.put(position, info.username);
                defaultRoundsWon.put(position, (long) info.roundsWon); // This needs to be updated in the PlayerInfo struct
            }

            // Set player names and rounds won in the labels
            gui.getPlayer1usernameRanking().setText(defaultNames.get(1));
            gui.getPlayer2usernameRanking().setText(defaultNames.get(2));
            gui.getPlayer3usernameRanking().setText(defaultNames.get(3));
            gui.getPlayer4usernameRanking().setText(defaultNames.get(4));

            gui.getPlayer1roundsWon().setText(String.valueOf(defaultRoundsWon.get(1)));
            gui.getPlayer2roundsWon().setText(String.valueOf(defaultRoundsWon.get(2)));
            gui.getPlayer3roundsWon().setText(String.valueOf(defaultRoundsWon.get(3)));
            gui.getPlayer4roundsWon().setText(String.valueOf(defaultRoundsWon.get(4)));

            gui.getLayeredPane().repaint();
            gui.getLayeredPane().revalidate();

            JOptionPane.showMessageDialog(gui, "Player " + username + " has won the game", "Game Winner", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void wordIsValid(String word) {
        SwingUtilities.invokeLater(() -> {
            gui.getInputJLabel().setText(word + " is valid");
        });
    }

    @Override
    public void wordHasBeenGuessed(String word) {
        SwingUtilities.invokeLater(() -> {
            gui.getInputJLabel().setText(word + " has been guessed");
        });
    }

    @Override
    public void wordIsInvalid(String word) {
        SwingUtilities.invokeLater(() -> {
            gui.getInputJLabel().setText(word + " is invalid");
        });
    }

    public void updateWaitingTimeLabel(int newSeconds){
        SwingUtilities.invokeLater(() -> {
            gui.getWaitingTimeLabel().setText("Waiting time for players to join a game: " + newSeconds);
        });
    }

    @Override
    public void displayTie() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui, "No winner declared for the round due to a tie.", "Round Tie", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void updateLeaderBoardGUI(Users[] users) {
        SwingUtilities.invokeLater(() -> {
            gui.getLfirstUsername().setText("N/A");
            gui.getLsecondUsername().setText("N/A");
            gui.getLthirdUsername().setText("N/A");
            gui.getLfourthUsername().setText("N/A");
            gui.getLfifthUsername().setText("N/A");
            gui.getLpoint1().setText("0");
            gui.getLpoint2().setText("0");
            gui.getLpoint3().setText("0");
            gui.getLpoint4().setText("0");
            gui.getLpoint5().setText("0");

            // Update labels with user data if available
            if (users.length > 0 && users[0] != null) {
                gui.getLfirstUsername().setText(users[0].username);
                gui.getLpoint1().setText(String.valueOf(users[0].roundsWon));
            }
            if (users.length > 1 && users[1] != null) {
                gui.getLsecondUsername().setText(users[1].username);
                gui.getLpoint2().setText(String.valueOf(users[1].roundsWon));
            }
            if (users.length > 2 && users[2] != null) {
                gui.getLthirdUsername().setText(users[2].username);
                gui.getLpoint3().setText(String.valueOf(users[2].roundsWon));
            }
            if (users.length > 3 && users[3] != null) {
                gui.getLfourthUsername().setText(users[3].username);
                gui.getLpoint4().setText(String.valueOf(users[3].roundsWon));
            }
            if (users.length > 4 && users[4] != null) {
                gui.getLfifthUsername().setText(users[4].username);
                gui.getLpoint5().setText(String.valueOf(users[4].roundsWon));
            }
        });
    }

    @Override
    public void updateGameSessions(TestingGrounds.GameSystem.Lobbies[] lobbiesArray) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = (DefaultTableModel) guiAdmin.getTable().getModel();
            model.setRowCount(0); // Clear existing data

            for (TestingGrounds.GameSystem.Lobbies lobby : lobbiesArray) {
                Object[] row = new Object[6];
                row[0] = lobby.gameToken;
                row[1] = lobby.playerCount;
                row[2] = lobby.lobbyWaitingTime;
                row[3] = lobby.durationPerRound;
                row[4] = lobby.winningRounds;
                row[5] = lobby.status;
                model.addRow(row);
            }
        });
    }

    private void loadAvailableIcons() {
        File iconDirectory = new File(ICONS_PATH);
        if (!iconDirectory.exists() || !iconDirectory.isDirectory()) {
            System.err.println("Icon directory not found: " + ICONS_PATH);
            return;
        }

        File[] gifFiles = iconDirectory.listFiles((dir, name) -> name.endsWith(".gif"));

        if (gifFiles != null) {
            for (File gifFile : gifFiles) {
                ImageIcon icon = new ImageIcon(gifFile.getPath());
                availableIcons.add(icon);
            }
        } else {
            System.err.println("No GIF icons found in directory: " + ICONS_PATH);
        }
    }

    private Icon assignIcon(String username) {
        if (usernameToIconMap.containsKey(username)) {
            return usernameToIconMap.get(username);
        }

        // Ensure availableIcons is not empty
        if (availableIcons.isEmpty()) {
            System.err.println("No available icons to assign.");
            return null; // Return a fallback icon if needed, or return null
        }

        for (Icon icon : availableIcons) {
            if (!assignedIcons.contains(icon)) {
                assignedIcons.add(icon);
                usernameToIconMap.put(username, icon);
                return icon;
            }
        }

        System.err.println("All icons have been assigned. Using a default icon for " + username);
        return null;
    }

    private void appendToAnnouncement(JTextPane textPane, String message) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), message, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

