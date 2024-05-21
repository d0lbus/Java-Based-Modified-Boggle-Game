package Java.Client_Java;

import Java.GameSystem.*;
import Java.ImplementationClass.GameClientCallbackImpl;
import Java.View.AdminGUIFrame;
import Java.View.AdminRegistration;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Admin class represents the main entry point for the admin client-side Java application. It facilitates
 * administrative tasks such as modifying game settings and viewing player information.
 */
public class Admin {
    private static String username = "";
    private static String password = "";
    private static org.omg.CORBA.StringHolder sessionToken = new StringHolder();
    static GameServer gameServerImp;
    static GameClientCallbackImpl cbi;
    static CallbackInterface callbackRef;
    static AdminGUIFrame adminGUIFrame = new AdminGUIFrame();
    static AdminRegistration registration = new AdminRegistration();

    /**
     * The main method responsible for initializing the ORB, establishing connections with the game server,
     * and managing admin interactions.
     * @param args
     */
    public static void main(String[] args) {
        try {
            cbi = new GameClientCallbackImpl(adminGUIFrame);
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "GameServer";
            gameServerImp = GameServerHelper.narrow(ncRef.resolve_str(name));
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            if (cbi == null) {
                throw new RuntimeException("Callback object is null");
            }
            rootpoa.activate_object(cbi);
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(cbi);
            callbackRef = CallbackInterfaceHelper.narrow(ref);

            startLogin();
        } catch (Exception e) {
            System.err.println("CORBA initialization failed: " + e.toString());
            e.printStackTrace();
        }
    }


    /**
     * Starts the admin login process by displaying the registration frame and handling admin authentication.
     */
    public static void startLogin() {
        registration.setVisible(true);
        registration.getSignInButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = registration.getUsernameLoginTextfield().getText();
                password = new String(registration.getLoginPasswordField().getPassword());
                boolean loginSuccessful = false;
                try {
                    loginSuccessful = gameServerImp.adminLogin(username, password, sessionToken, callbackRef);
                } catch (InvalidCredentials ex) {
                    throw new RuntimeException(ex);
                } catch (AlreadyLoggedIn ex) {
                    throw new RuntimeException(ex);
                }
                if (loginSuccessful) {
                    System.out.println("Login for " + username + " is successful. Session token: " + sessionToken.value);
                    startAdminDashboard();
                    registration.setVisible(false);
                } else {
                    System.out.println("Login failed.");
                }
            }
        });

    }

    /**
     * Starts the admin dashboard by displaying the admin GUI frame and handling admin-related actions
     * such as editing timers, viewing players, etc.
     */
    public static void startAdminDashboard() {
        if (gameServerImp == null) {
            System.err.println("GameServer implementation is not initialized.");
            return;
        }

        adminGUIFrame.setVisible(true);
        adminGUIFrame.getEditTimerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a spinner with a minimum value of 10
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 10, Integer.MAX_VALUE, 1);
                JSpinner timerSpinner = new JSpinner(spinnerModel);

                // Display the spinner in an input dialog
                int option = JOptionPane.showOptionDialog(adminGUIFrame, timerSpinner, "Edit Timer Duration",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    int newSecondsWaiting = (int) timerSpinner.getValue();
                    try {
                        gameServerImp.updateSecondsPerWaiting(newSecondsWaiting);
                        JOptionPane.showMessageDialog(adminGUIFrame, "Timer duration updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        System.err.println("Error updating timer duration: " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(adminGUIFrame, "Error updating timer duration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        adminGUIFrame.getEditRoundsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a spinner with a minimum value of 30 seconds
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(30, 30, 600, 1);
                JSpinner timerSpinner = new JSpinner(spinnerModel);

                // Display the spinner in an input dialog
                int option = JOptionPane.showOptionDialog(adminGUIFrame, timerSpinner, "Edit Round Timer Duration",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    int newSecondsWaiting = (int) timerSpinner.getValue();
                    try {
                        gameServerImp.editRoundTime(newSecondsWaiting);
                        JOptionPane.showMessageDialog(adminGUIFrame, "Timer duration updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        System.err.println("Error updating timer duration: " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(adminGUIFrame, "Error updating timer duration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        adminGUIFrame.getEditNumOfRoundsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a spinner with a minimum value of 10
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 3, 10, 1);
                JSpinner timerSpinner = new JSpinner(spinnerModel);

                // Display the spinner in an input dialog
                int option = JOptionPane.showOptionDialog(adminGUIFrame, timerSpinner, "Edit Number of Rounds to WIN",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    int newSecondsWaiting = (int) timerSpinner.getValue();
                    try {
                        gameServerImp.editNumRounds(newSecondsWaiting);
                        JOptionPane.showMessageDialog(adminGUIFrame, "Number of Rounds updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        System.err.println("Error updating Number of Rounds: " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(adminGUIFrame, "Error updating Number of Rounds: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        adminGUIFrame.getViewPlayersButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable playerTable = adminGUIFrame.getTable();
                // Get the selected row index
                int selectedRowIndex = playerTable.getSelectedRow();

                // Check if a row is actually selected
                if (selectedRowIndex != -1) {
                    // Get the value from the first column of the selected row
                    String firstColumnValue = playerTable.getValueAt(selectedRowIndex, 0).toString();

                    String selectedGameID = firstColumnValue;

                    // For demonstration purposes, you can print the value or use it as needed
                    System.out.println("Selected game ID: " + selectedGameID);

                    try {
                        // Get the player names from the server
                        String[] playerNames = gameServerImp.viewPlayers(selectedGameID);

                        // Display the player names in a JOptionPane
                        if (playerNames.length > 0) {
                            String message = "Players in game " + selectedGameID + ":\n" + String.join("\n", playerNames);
                            JOptionPane.showMessageDialog(adminGUIFrame, message, "Players", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(adminGUIFrame, "No players found for the selected game.", "No Players", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(adminGUIFrame, "Error retrieving players: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    // Optionally, show a message if no row is selected
                    JOptionPane.showMessageDialog(adminGUIFrame, "Please select a row first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

    }
}
