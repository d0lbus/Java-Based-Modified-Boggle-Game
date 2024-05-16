package Java.Server_Java;

import Java.GameSystem.GameServer;
import Java.GameSystem.GameServerHelper;
import Java.ImplementationClass.GameServerImpl;
import Java.Utilities.DataAccessObjects.UserDAO;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


import java.sql.SQLException;

public class Server {
    public static void main(String args[]) {
        try {
            Runtime.getRuntime().exec("cmd /c start orbd -ORBInitialPort 900 -ORBInitialHost 192.168.1.108");


            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();


            GameServerImpl gameServerImpl = new GameServerImpl();
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(gameServerImpl);
            GameServer href = GameServerHelper.narrow(ref);


            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "GameServer";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("GameServer ready and waiting ...");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    // Perform cleanup operations before exiting
                    UserDAO.clearSessionTokens();
                    System.out.println("Session Token Deleted");
                } catch (SQLException e) {
                    // Handle any exceptions that occur during cleanup
                    System.err.println("Error clearing session tokens: " + e.getMessage());
                    e.printStackTrace();
                }
            }));

            // Run the ORB event loop
            orb.run();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
    }
}
