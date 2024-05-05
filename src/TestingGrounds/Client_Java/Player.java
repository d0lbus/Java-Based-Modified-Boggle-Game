package TestingGrounds.Client_Java;

import TestingGrounds.GameSystem.CallbackInterface;
import TestingGrounds.GameSystem.CallbackInterfaceHelper;
import TestingGrounds.GameSystem.GameServer;
import TestingGrounds.GameSystem.GameServerHelper;
import TestingGrounds.ImplementationClass.GameClientCallbackImpl;
import View.ClientGUIFrame;
import View.Registration;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Player {
    private static String username = "";
    private static String password = "";
    private static String gameId = "";
    private static StringHolder sessionToken = new StringHolder();
    static GameServer gameServerImp;
    static GameClientCallbackImpl cbi;
    static CallbackInterface callbackRef;
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


    public static void startLogin(){
        registration.setVisible(true);

        registration.getSignInButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = registration.getUsernameLoginTextfield().getText();
                password = Arrays.toString(registration.getLoginPasswordField().getPassword());
                boolean loginSuccessful = gameServerImp.login(username, password, sessionToken, callbackRef);
                if (loginSuccessful) {
                    System.out.println("Login for " +username+ " is successful. Session token: " + sessionToken.value);
                    startGame();
                    registration.setVisible(false);
                } else {
                    System.out.println("Login failed.");
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
                gameId = gameServerImp.hostGame(sessionToken.value, callbackRef);
            }
        });

        clientGUIFrame.getRandomButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean gameFound = gameServerImp.joinRandomGame(sessionToken.value, callbackRef);
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
                gameServerImp.startGame(sessionToken.value, gameId);
            }
        });
    }

}
