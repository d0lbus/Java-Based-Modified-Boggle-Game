package TestingGrounds.Client_Java;

import TestingGrounds.GameSystem.CallbackInterface;
import TestingGrounds.GameSystem.CallbackInterfaceHelper;
import TestingGrounds.GameSystem.GameServer;
import TestingGrounds.GameSystem.GameServerHelper;
import TestingGrounds.ImplementationClass.GameClientCallbackImpl;
import TestingGrounds.Utilities.DBConnection;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Player {
    private static String username = "";
    private static String password = "";
    private static String gameId = "";
    private static StringHolder sessionToken = new StringHolder();
    static GameServer gameServerImp;
    static GameClientCallbackImpl cbi;
    static CallbackInterface callbackRef;
    private static int lobbyCounter = 1;
    static ClientGUIFrame clientGUIFrame = new ClientGUIFrame();
    static Registration registration = new Registration();
    public static void main(String[] args) {
        try {
            cbi = new GameClientCallbackImpl(clientGUIFrame);
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
                String username = registration.getUsernameLoginTextfield().getText();
                char[] enteredPasswordChars = registration.getLoginPasswordField().getPassword();
                String enteredPassword = new String(enteredPasswordChars);

                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet resultSet = null;

                try {
                    // Obtain database connection
                    DBConnection dbConnection = new DBConnection();
                    connection = dbConnection.getConnection();

                    // Query to retrieve user information
                    String query = "SELECT password FROM players WHERE username=?";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, username);
                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        // Username exists in the database
                        String storedPassword = resultSet.getString("password");
                        if (storedPassword.equals(enteredPassword)) {
                            // Password is correct
                            // Generate session token
                            sessionToken.value = generateSessionToken();

                            // Update session token in database for the current user
                            updateSessionTokenInDatabase(connection, username, sessionToken.value);

                            System.out.println("Login for " + username + " is successful. Session token: " + sessionToken.value);
                            JOptionPane.showMessageDialog(registration, "Successful Login! Welcome to Boggled, " + username);
                            startGame(sessionToken.value);
                            registration.setVisible(false);
                        } else {
                            // Wrong password
                            JOptionPane.showMessageDialog(registration, "Wrong password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Username not found
                        JOptionPane.showMessageDialog(registration, "Username not found", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    System.err.println("Error executing SQL query: " + ex.getMessage());
                } finally {
                    // Close database resources
                    try {
                        if (resultSet != null) resultSet.close();
                        if (statement != null) statement.close();
                        if (connection != null) connection.close();
                    } catch (SQLException ex) {
                        System.err.println("Error closing database resources: " + ex.getMessage());
                    }
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

                // Insert user into the database
                try {
                    DBConnection dbConnection = new DBConnection();
                    Connection connection = dbConnection.getConnection();

                    // Capitalize the first letter of first name and last name
                    String firstNameCapitalized = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                    String lastNameCapitalized = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

                    String query = "INSERT INTO players (firstName, lastName, password, username, userType) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, firstNameCapitalized);
                    statement.setString(2, lastNameCapitalized);
                    statement.setString(3, password);
                    statement.setString(4, username);
                    statement.setString(5, "player");
                    statement.executeUpdate();

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


    // Generate a random session token using UUID
    private static String generateSessionToken() {
        return UUID.randomUUID().toString();
    }

    // Update session token in the database
    private static void updateSessionTokenInDatabase(Connection connection, String username, String sessionToken) throws SQLException {
        String updateQuery = "UPDATE players SET sessionToken = ? WHERE username = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setString(1, sessionToken);
        updateStatement.setString(2, username);
        updateStatement.executeUpdate();
        updateStatement.close();
    }

    public static String generateGameToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }
        return token.toString();
    }


    private static void insertLobbyIntoDatabase(Connection connection, String gameId, String sessionToken, String gameToken) throws SQLException {
        String insertQuery = "INSERT INTO activegames (gameId, sessionToken, gameToken) VALUES (?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, gameId);
        insertStatement.setString(2, sessionToken);
        insertStatement.setString(3, gameToken);
        insertStatement.executeUpdate();
        insertStatement.close();
    }


    public static void startGame(String sessionToken) {
        clientGUIFrame.setVisible(true);

        clientGUIFrame.getCreateLobbyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameId = String.valueOf(lobbyCounter++); // Increment lobby counter for each new lobby
                String gameToken = generateGameToken();
                try {
                    // Update the game token in the database
                    DBConnection dbConnection = new DBConnection();
                    Connection connection = dbConnection.getConnection();
                    // updateGameTokenInDatabase(connection, gameId, gameToken);

                    // Insert lobby information into the database
                    insertLobbyIntoDatabase(connection, gameId, sessionToken, gameToken);
                } catch (SQLException ex) {
                    System.err.println("Error updating game token in the database: " + ex.getMessage());
                }
                System.out.println("Game created with ID: " + gameId + " and token: " + gameToken);
            }
        });

        clientGUIFrame.getRandomButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean gameFound = gameServerImp.joinRandomGame(sessionToken, callbackRef);
                if (gameFound) {
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
                if (gameId != null) {
                    gameServerImp.startGame(sessionToken, gameId);
                } else {
                    System.err.println("No game ID available.");
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
