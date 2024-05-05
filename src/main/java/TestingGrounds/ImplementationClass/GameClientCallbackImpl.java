package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.CallbackInterfacePOA;
import TestingGrounds.GameSystem.PlayerInfo;
import View.ClientGUIFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class GameClientCallbackImpl extends CallbackInterfacePOA {
    private ClientGUIFrame gui;

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

            // Update the map with actual player data
            for (PlayerInfo info : playerData) {
                defaultPositions.put((int) info.position, info.username);
            }


            // Set GUI components based on the updated map
            gui.getPlayer1username().setText(defaultPositions.get(1));
            gui.getPlayer2username().setText(defaultPositions.get(2));
            gui.getPlayer3username().setText(defaultPositions.get(3));
            gui.getPlayer4username().setText(defaultPositions.get(4));

            // Print out the current state of the GUI components for debugging
            System.out.println("GUI State:");
            System.out.println("Player 1: " + gui.getPlayer1username().getText());
            System.out.println("Player 2: " + gui.getPlayer2username().getText());
            System.out.println("Player 3: " + gui.getPlayer3username().getText());
            System.out.println("Player 4: " + gui.getPlayer4username().getText());
        });
    }





    @Override
    public void informUser(int userPosition, String word, int score) {
        SwingUtilities.invokeLater(() -> {

        });
    }
}

