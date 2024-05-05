package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.CallbackInterfacePOA;
import TestingGrounds.GameSystem.PlayerInfo;
import View.ClientGUIFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class GameClientCallbackImpl extends CallbackInterfacePOA {
    private ClientGUIFrame gui;
    private final Random random = new Random();
    private static final String ICONS_PATH = "src/Icons";

    public GameClientCallbackImpl(){

    }
    public GameClientCallbackImpl(ClientGUIFrame gui) {
        this.gui = gui;
    }

    @Override
    public void CreateLobGUI(String username, String gameId) {
        SwingUtilities.invokeLater(() -> {

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

            gui.getPlayer1pic().setIcon(defaultPositions.get(1).equals("Empty") ? null : getRandomIcon());
            gui.getPlayer2pic().setIcon(defaultPositions.get(2).equals("Empty") ? null : getRandomIcon());
            gui.getPlayer3pic().setIcon(defaultPositions.get(3).equals("Empty") ? null : getRandomIcon());
            gui.getPlayer4pic().setIcon(defaultPositions.get(4).equals("Empty") ? null : getRandomIcon());

            // Print out the current state of the GUI components for debugging
            System.out.println("GUI State:");
            System.out.println("Player 1: " + gui.getPlayer1username().getText());
            System.out.println("Player 2: " + gui.getPlayer2username().getText());
            System.out.println("Player 3: " + gui.getPlayer3username().getText());
            System.out.println("Player 4: " + gui.getPlayer4username().getText());


        });
    }

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

            // Update player labels directly within the function
            Map<Integer, String> defaultPositions = new HashMap<>();
            defaultPositions.put(1, "Empty");
            defaultPositions.put(2, "Empty");
            defaultPositions.put(3, "Empty");
            defaultPositions.put(4, "Empty");

            for (PlayerInfo info : playerData) {
                defaultPositions.put((int) info.position, info.username);
            }

            gui.getPlayer1gameUsername().setText(defaultPositions.get(1));
            gui.getPlayer2gameUsername().setText(defaultPositions.get(2));
            gui.getPlayer3gameUsername().setText(defaultPositions.get(3));
            gui.getPlayer4gameUsername().setText(defaultPositions.get(4));
        });
    }

    @Override
    public void informUser(int userPosition, String word, int score) {
        SwingUtilities.invokeLater(() -> {

        });
    }

    private Icon getRandomIcon() {
        File iconDirectory = new File(ICONS_PATH);
        if (!iconDirectory.exists() || !iconDirectory.isDirectory()) {
            System.err.println("Icon directory not found: " + ICONS_PATH);
            return null;
        }

        File[] icons = iconDirectory.listFiles((dir, name) -> name.endsWith(".gif"));

        if (icons != null && icons.length > 0) {
            File randomIconFile = icons[random.nextInt(icons.length)];
            System.out.println("Selected Icon: " + randomIconFile.getName());

            ImageIcon icon = new ImageIcon(randomIconFile.getPath());
            Image image = icon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } else {
            System.err.println("No icons found in directory: " + ICONS_PATH);
            return null;
        }
    }
}

