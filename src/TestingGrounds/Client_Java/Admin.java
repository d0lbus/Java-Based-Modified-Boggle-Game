package TestingGrounds.Client_Java;

import TestingGrounds.GameSystem.*;
import TestingGrounds.ImplementationClass.GameClientCallbackImpl;
import TestingGrounds.View.AdminGUIFrame;
import TestingGrounds.View.AdminRegistration;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Admin {
    private static String username = "";
    private static String password = "";
    private static org.omg.CORBA.StringHolder sessionToken = new StringHolder();
    static GameServer gameServerImp;
    static GameClientCallbackImpl cbi;
    static CallbackInterface callbackRef;
    static AdminGUIFrame adminGUIFrame = new AdminGUIFrame();
    static AdminRegistration registration = new AdminRegistration();
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
                } catch (AlreadyLoggedIn ex) {
                    System.err.println("User is already logged in: " + ex.getMessage());
                    JOptionPane.showMessageDialog(registration, "User is already logged in! Try Another Account. " , "Login Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidCredentials ex) {
                    System.err.println("Invalid credentials: " + ex.getMessage());
                    JOptionPane.showMessageDialog(registration, "Invalid credentials! Please Try Again. " , "Login Error", JOptionPane.ERROR_MESSAGE);
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

        adminGUIFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call the logout method from GameServerImpl
                boolean logoutSuccessful = false;
                logoutSuccessful = gameServerImp.adminLogout(sessionToken.value);
                if (logoutSuccessful) {
                    System.out.println("Logout for " + username + " is successful.");
                    System.exit(0); // Exit the application after successful logout
                } else {
                    System.out.println("Logout for " + username + " failed");
                }
            }
        });


    }
}
