package TestingGrounds.ImplementationClass;

import TestingGrounds.GameSystem.CallbackInterfacePOA;
import View.ClientGUIFrame;

import javax.swing.*;

public class GameClientCallbackImpl extends CallbackInterfacePOA {
    private ClientGUIFrame gui;

    public GameClientCallbackImpl(ClientGUIFrame gui) {
        this.gui = gui;
    }

    @Override
    public void CreateLobGUI(String username, String gameId) {
        SwingUtilities.invokeLater(() -> {
            gui.getPlayer1username().setText(username);
        });
    }

    @Override
    public void UpdateLobGUI(String username) {
        SwingUtilities.invokeLater(() -> {
            gui.getPlayer3username().setText(username);
        });
    }

    @Override
    public void informUser(int userPosition, String word, int score) {
        SwingUtilities.invokeLater(() -> {

        });
    }
}

