package Java.Client_Java;

import Java.GameSystem.*;
import Java.ImplementationClass.GameClientCallbackImpl;
import Java.GameSystem.lossConnection;
import Java.ReferenceClasses.GameSession;
import Java.ReferenceClasses.User;
import Java.Utilities.DataAccessObjects.UserDAO;
import Java.View.ClientGUIFrame;
import Java.View.EnterCodeFrame;
import Java.View.Registration;
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

/**
 * The Player class represents the main entry point for the client-side Java application. It handles user
 * authentication, game initialization, and interaction with the game server.
 */
public class Player {
    private static String username = "";
    private static String password = "";
    private static String gameToken = "";
    private static org.omg.CORBA.StringHolder sessionToken = new StringHolder();
    static GameSession gameSession;
    static GameServer gameServerImp;
    static GameClientCallbackImpl cbi;
    static CallbackInterface callbackRef;
    static ClientGUIFrame clientGUIFrame = new ClientGUIFrame();
    static EnterCodeFrame enterCodeFrame = new EnterCodeFrame();
    static Registration registration = new Registration();

    /**
     * The main method responsible for initializing the ORB, establishing connections with the
     * game server, and managing user interactions.
     * @param args
     */
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(new String[]{"-ORBInitialPort", "900", "-ORBInitialHost", "localhost"}, null);

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

            while (true) {
                // Check if the server is still running
                boolean serverRunning = checkServerStatus();

                if (!serverRunning) {
                    System.out.println("Server has terminated. Exiting...");
                    JOptionPane.showMessageDialog(null, "No Server Connection. Exiting...", "Connection Lost", JOptionPane.WARNING_MESSAGE,
                            null);
                    orb.shutdown(false); // Shutdown ORB without waiting for pending requests

                    // Clean up GUI resources before exiting
                    clientGUIFrame.dispose();
                    enterCodeFrame.dispose();
                    registration.dispose();


                    System.exit(0); // Exit the application
                }

                // Add a delay before checking again
                try {
                    Thread.sleep(1000); // Sleep for 1 second (1000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (lossConnection e) {
            System.err.println("Lost connection to the server.");
            // Handle the loss of connection gracefully
            JOptionPane.showMessageDialog(null, "Lost connection to the server. Please check your network connection and try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("CORBA initialization failed: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Starts the login process by displaying the registration frame and handling user authentication.
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
                    loginSuccessful = gameServerImp.login(username, password, sessionToken, callbackRef);
                } catch (AlreadyLoggedIn ex) {
                    System.err.println("User is already logged in: " + ex.getMessage());
                    JOptionPane.showMessageDialog(registration, "User is already logged in! Try Another Account. " , "Login Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidCredentials ex) {
                    System.err.println("Invalid credentials: " + ex.getMessage());
                    JOptionPane.showMessageDialog(registration, "Invalid credentials! Please Try Again. " , "Login Error", JOptionPane.ERROR_MESSAGE);
                }

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
                    UserDAO.createUser(user, firstNameCapitalized, lastNameCapitalized, password,null,null,null,null,null);

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


    /**
     * Starts the game by displaying the client GUI frame and handling game-related actions such as
     * hosting, joining, and leaving games.
     */
    public static void startGame() {
        clientGUIFrame.setVisible(true);
        clientGUIFrame.getCreateLobbyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Session Token: " + sessionToken.value);
                gameToken = gameServerImp.hostGame(sessionToken.value, callbackRef);
            }
        });

        clientGUIFrame.getRandomButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String newGameToken = gameServerImp.joinRandomGame(sessionToken.value, callbackRef);
                    if (newGameToken != null) {
                        // Move to lobby only if a game token is obtained
                        gameToken = newGameToken;
                        clientGUIFrame.getLayeredPane().removeAll();
                        clientGUIFrame.getLayeredPane().add(clientGUIFrame.getLobbyPanel());
                        clientGUIFrame.getLayeredPane().repaint();
                        clientGUIFrame.getLayeredPane().revalidate();
                    } else {
                        // No game token obtained, no game found or unable to join
                        System.out.println("No game found or unable to join.");
                    }
                } catch (NoWaitingGames ex) {
                    // Handle no waiting games available
                    System.err.println("No waiting games available! Try Again Later. ");
                    JOptionPane.showMessageDialog(null, "No waiting games available. Please try again later.", "Game Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clientGUIFrame.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameServerImp.startGame(sessionToken.value, gameToken);
                } catch (LobbyTimeExpired ex) {
                    System.err.println("Lobby Time Expired.  ");
                    JOptionPane.showMessageDialog(null, "Lobby Time Expired", "Expired Time", JOptionPane.ERROR_MESSAGE);
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
                } catch (InvalidWord ex) {
                    System.err.println("Invalid word " );
                }
                clientGUIFrame.getInputTextField().setText("");
            }
        });

        clientGUIFrame.getJoinButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterCodeFrame.setVisible(true);
                enterCodeFrame.getJoinButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameToken = enterCodeFrame.getCodeTextField().getText();
                        if (gameToken.isEmpty()){
                            JOptionPane.showMessageDialog(enterCodeFrame, "Please type a Game Code", "Join Game Failed", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            gameServerImp.joinGame(sessionToken.value, gameToken);
                        } catch (InvalidGameCode ex) {
                            System.err.println("Invalid game code: " + ex.getMessage());
                            JOptionPane.showMessageDialog(null, "Invalid game code. Please enter a valid one.", "Error", JOptionPane.ERROR_MESSAGE);
                            throw new RuntimeException("Invalid game code: " + ex.getMessage(), ex);
                        } catch (GameAlreadyActive ex) {
                            System.err.println("Game is already active: " + ex.getMessage());
                        }
                        if (gameToken != null) {
                            clientGUIFrame.getLayeredPane().removeAll();
                            clientGUIFrame.getLayeredPane().add(clientGUIFrame.getLobbyPanel());
                            clientGUIFrame.getLayeredPane().repaint();
                            clientGUIFrame.getLayeredPane().revalidate();
                            enterCodeFrame.setVisible(false);
                        } else {
                            System.out.println("No game found or unable to join.");
                        }
                    }
                });
            }
        });

        clientGUIFrame.getLeaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameServerImp.leaveGame(sessionToken.value,gameToken);
            }
        });

        clientGUIFrame.getexitLobbyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameServerImp.leaveLobby(sessionToken.value,gameToken);
            }
        });

        clientGUIFrame.getQuitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the logout method from GameServerImpl
                boolean logoutSuccessful = false;
                logoutSuccessful = gameServerImp.logout(sessionToken.value);
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
                boolean logoutSuccessful = false;
                logoutSuccessful = gameServerImp.logout(sessionToken.value);
                if (logoutSuccessful) {
                    System.out.println("Logout for " + username + " is successful.");
                    System.exit(0); // Exit the application after successful logout
                } else {
                    System.out.println("Logout for " + username + " failed");
                }
            }
        });


        clientGUIFrame.getBackToHomeButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameServerImp.backToHomeScreen(sessionToken.value);
            }
        });

    }

    /**
     * Checks the status of the game server to ensure connectivity.
     * @return
     * @throws lossConnection
     */
    private static boolean checkServerStatus() throws lossConnection {
        try {
            boolean serverConnected = gameServerImp.isServerConnected();
            return serverConnected;
        } catch (lossConnection e) {
            // If lossConnection exception is thrown, rethrow it to handle it in the main method
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions that may occur while checking the server status
            System.err.println("Error checking server status: " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
