package TestingGrounds.Client_Java;

import TestingGrounds.GameSystem.GameServer;
import TestingGrounds.GameSystem.GameServerHelper;
import View.ClientGUIFrame;
import View.Registration;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Player {
    private static String username = "";
    private static String password = "";
    private static StringHolder sessionToken = new StringHolder();
    static GameServer gameServerImp;

    static ClientGUIFrame clientGUIFrame = new ClientGUIFrame();
    static Registration registration = new Registration();
    public static void main(String args[]) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "GameServer";
            gameServerImp = GameServerHelper.narrow(ncRef.resolve_str(name));
            startLogin();

        } catch (Exception e) {
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
                boolean loginSuccessful = gameServerImp.login(username, password, sessionToken);
                if (loginSuccessful) {
                    System.out.println("Login for " +username+ " is successful. Session token: " + sessionToken.value);
                    startGame();
                } else {
                    System.out.println("Login failed.");
                }
            }
        });
    }

    public static void startGame(){
        clientGUIFrame.setVisible(true);


        clientGUIFrame.getCreateLobbyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameServerImp.hostGame(sessionToken.toString());
            }
        });

    }
}
