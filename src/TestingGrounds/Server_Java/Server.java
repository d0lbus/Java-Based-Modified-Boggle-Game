package TestingGrounds.Server_Java;

import TestingGrounds.GameSystem.GameServer;
import TestingGrounds.GameSystem.GameServerHelper;
import TestingGrounds.ImplementationClass.GameServerImpl;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Server {
    public static void main(String args[]) {
        try {
            Runtime.getRuntime().exec("cmd /c start orbd -ORBInitialPort 900 -ORBInitialHost 0.0.0.0");
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

            orb.run();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
    }
}
