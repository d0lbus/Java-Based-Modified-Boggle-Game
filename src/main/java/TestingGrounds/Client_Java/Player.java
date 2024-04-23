package TestingGrounds.Client_Java;

import TestingGrounds.GameSystem.GameServer;
import TestingGrounds.GameSystem.GameServerHelper;
import View.ClientGUIFrame;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class Player {
    static GameServer gameServerImp;

    static ClientGUIFrame clientGUIFrame = new ClientGUIFrame();
    public static void main(String args[]) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "GameServer";
            gameServerImp = GameServerHelper.narrow(ncRef.resolve_str(name));

            String username = "Player1";
            String password = "password";
            StringHolder sessionToken = new StringHolder();

            boolean isLoggedIn = gameServerImp.login(username, password, sessionToken);

            if (isLoggedIn){
                clientGUIFrame.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
