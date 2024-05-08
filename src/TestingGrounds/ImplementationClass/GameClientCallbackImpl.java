package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.CallbackInterfacePOA;
import TestingGrounds.GameSystem.PlayerInfo;
import View.ClientGUIFrame;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameClientCallbackImpl extends CallbackInterfacePOA {
    private ClientGUIFrame gui;
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

    @Override
    public void CreateLobGUI(String username, String gameId) {
        SwingUtilities.invokeLater(() -> {
            gui.getPlayer1pic().setIcon(assignIcon(username));
        });
    }

    @Override
    public void UpdateLobGUI(PlayerInfo[] playerData) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Updating GUI for all players");

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

            defaultPositions.put(1, "Empty");
            defaultPositions.put(2, "Empty");
            defaultPositions.put(3, "Empty");
            defaultPositions.put(4, "Empty");

            defaultScores.put(1, 0);
            defaultScores.put(2, 0);
            defaultScores.put(3, 0);
            defaultScores.put(4, 0);

            for (PlayerInfo info : playerData) {
                defaultPositions.put((int) info.position, info.username);
                defaultScores.put((int) info.position, info.score);
            }

            gui.getPlayer1gameUsername().setText(defaultPositions.get(1));
            gui.getPlayer2gameUsername().setText(defaultPositions.get(2));
            gui.getPlayer3gameUsername().setText(defaultPositions.get(3));
            gui.getPlayer4gameUsername().setText(defaultPositions.get(4));

            gui.getPlayer1gamePoints().setText(String.valueOf(defaultScores.get(1)));
            gui.getPlayer2gamePoints().setText(String.valueOf(defaultScores.get(2)));
            gui.getPlayer3gamePoints().setText(String.valueOf(defaultScores.get(3)));
            gui.getPlayer4gamePoints().setText(String.valueOf(defaultScores.get(4)));

            gui.getPlayer1gamePic().setIcon(defaultPositions.get(1).equals("Empty") ? null : assignIcon(defaultPositions.get(1)));
            gui.getPlayer2gamePic().setIcon(defaultPositions.get(2).equals("Empty") ? null : assignIcon(defaultPositions.get(2)));
            gui.getPlayer3gamePic().setIcon(defaultPositions.get(3).equals("Empty") ? null : assignIcon(defaultPositions.get(3)));
            gui.getPlayer4gamePic().setIcon(defaultPositions.get(4).equals("Empty") ? null : assignIcon(defaultPositions.get(4)));

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
    public void displayRoundWinner(String winnerName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui, "Round winner: " + winnerName, "Round Winner", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void displayOverallWinner(String winnerName) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui, "Overall winner: " + winnerName + " has won the game!", "Overall Winner", JOptionPane.INFORMATION_MESSAGE);
            gui.getTimerLabel().setText("Timer: 0");
        });
    }

    @Override
    public void displayTie() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(gui, "No winner declared for the round due to a tie.", "Round Tie", JOptionPane.INFORMATION_MESSAGE);
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

