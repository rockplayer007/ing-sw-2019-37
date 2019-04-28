package network.server.rmi;

import network.server.MainServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMI {

    private MainServer server;
    public ServerRMI(MainServer server){
        this.server = server;
    }

    public void startServer(int port) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", "localhost");
        Registry registry = LocateRegistry.createRegistry(port);
        ServerImplementation serverImplementation = new ServerImplementation(server);

        registry.rebind("MainServer", serverImplementation);
    }
}
