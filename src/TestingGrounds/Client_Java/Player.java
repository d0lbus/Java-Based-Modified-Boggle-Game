package TestingGrounds.Client_Java;

import TestingGrounds.GameSystem.*;
import TestingGrounds.ImplementationClass.GameClientCallbackImpl;
import TestingGrounds.ReferenceClasses.User;
import TestingGrounds.Utilities.DataAccessObjects.UserDAO;
import View.ClientGUIFrame;
import View.Registration;
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
import java.sql.SQLException;

public class Player {
    private static String username = "";
    private static String password = "";
    private static String gameToken = "";
    private static org.omg.CORBA.StringHolder sessionToken = new StringHolder();
    static GameServer gameServerImp;
    static GameClientCallbackImpl cbi;
    static CallbackInterface callbackRef;
    static ClientGUIFrame clientGUIFrame = new ClientGUIFrame();
    static Registration registration = new Registration();
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(new String[]{"-ORBInitialPort", "900", "-ORBInitialHost", "192.168.1.108"}, null);

            cbi = new GameClientCallbackImpl(clientGUIFrame);

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

                boolean loginSuccessful = gameServerImp.login(username, password, sessionToken, callbackRef);

                if (loginSuccessful) {
                    System.out.println("Login for " + username + " is successful. Session token: " + sessionToken.value);
                    startGame();
                    registration.setVisible(false);
                } else {
                    System.out.println("Login failed.");
                }
            }
        });


        registration.getDoneButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = registration.getFirstNameTextfield().getText();
                String lastName = registration.getLastNameTextfield().getText();
                String username = registration.getUsernameRegisterTextfield().getText();
                char[] passwordChars = registration.getSignUpPasswordField().getPassword();
                String password = new String(passwordChars);
                char[] confirmPasswordChars = registration.getConfirmPasswordField().getPassword();
                String confirmPassword = new String(confirmPasswordChars);

                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(registration, "All fields need to be filled up", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(registration, "Passwords do not match", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    if (UserDAO.doesUsernameExist(username)) {
                        JOptionPane.showMessageDialog(registration, "Username is already taken. Please choose a different one.", "Signup Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException ex) {
                    System.err.println("Error checking username existence: " + ex.getMessage());
                    JOptionPane.showMessageDialog(registration, "Error checking username existence: " + ex.getMessage(), "Signup Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert user into the database
                try {
                    // Capitalize the first letter of first name and last name
                    String firstNameCapitalized = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                    String lastNameCapitalized = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

                    // Create a User object
                    User user = new User(null, username, null, false, 0, null);
                    user.setFirstName(firstNameCapitalized);
                    user.setLastName(lastNameCapitalized);
                    user.setUsername(username);
                    // Set other user properties as needed

                    // Call createUser method to insert user into the database
                    UserDAO.createUser(user, firstNameCapitalized, lastNameCapitalized, password);

                    JOptionPane.showMessageDialog(registration, "Registration successful! Welcome to Boggled, " + username);

                    // Clear the text fields after successful registration
                    registration.getFirstNameTextfield().setText("");
                    registration.getLastNameTextfield().setText("");
                    registration.getUsernameRegisterTextfield().setText("");
                    registration.getSignUpPasswordField().setText("");
                    registration.getConfirmPasswordField().setText("");

                    registration.setVisible(true);

                } catch (SQLException ex) {
                    System.err.println("Error executing SQL query: " + ex.getMessage());
                    JOptionPane.showMessageDialog(registration, "Error: " + ex.getMessage(), "Signup Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    public static void startGame() {
        clientGUIFrame.setVisible(true);
        clientGUIFrame.getCreateLobbyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Session Token: " + sessionToken.value);
                try {
                    gameToken = gameServerImp.hostGame(sessionToken.value, callbackRef);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        clientGUIFrame.getRandomButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameToken = gameServerImp.joinRandomGame(sessionToken.value, callbackRef);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (gameToken != null) {
                    clientGUIFrame.getLayeredPane().removeAll();
                    clientGUIFrame.getLayeredPane().add(clientGUIFrame.getLobbyPanel());
                    clientGUIFrame.getLayeredPane().repaint();
                    clientGUIFrame.getLayeredPane().revalidate();
                } else {
                    System.out.println("No game found or unable to join.");
                }
            }
        });

        clientGUIFrame.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameServerImp.startGame(sessionToken.value, gameToken);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        clientGUIFrame.getInputTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = clientGUIFrame.getInputTextField().getText();
                try {
                    gameServerImp.submitWord(sessionToken.value, gameToken, word);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                clientGUIFrame.getInputTextField().setText("");
            }
        });

        clientGUIFrame.getQuitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the logout method from GameServerImpl
                boolean logoutSuccessful = gameServerImp.logout(sessionToken.value);
                if (logoutSuccessful) {

                    System.out.println("Logout for " + username + " is successful.");
                    System.exit(0); // Exit the application after successful logout
                } else {
                    System.out.println("Logout for " + username + " failed");
                }
            }
        });

        clientGUIFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call the logout method from GameServerImpl
                boolean logoutSuccessful = gameServerImp.logout(sessionToken.value);
                if (logoutSuccessful) {
                    System.out.println("Logout for " + username + " is successful.");
                    System.exit(0); // Exit the application after successful logout
                } else {
                    System.out.println("Logout for " + username + " failed");
                }
            }
        });
    }


    public static void changeAccSettings(){
        String newUsername = clientGUIFrame.getcUsernameTextfield().getText();
        String newPassword = new String(clientGUIFrame.getPwField().getPassword());
        String confirmPassword = new String(clientGUIFrame.getConfirmpwField().getPassword());

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        if (newUsername.isEmpty()){
            System.out.println("Username is empty");
            return;
        }

        // Update the database
//        DatabaseManager dbManager = new DatabaseManager();
//        dbManager.changeAccSettings(newUsername, newPassword);

        System.out.println("Account settings updated successfully!");
    }

}
