package network.server.rmi;

import network.server.MainServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Creates an RMI server to connect the client
 */
public class ServerRMI {

    private MainServer server;
    public ServerRMI(MainServer server){
        this.server = server;
    }

    /**
     * Starts the server on a specific port
     * @param port
     * @throws RemoteException
     */
    public void startServer(int port) throws RemoteException {
        //System.setProperty("java.rmi.server.hostname", "localhost");
        Registry registry = LocateRegistry.createRegistry(port);
        ServerImplementation serverImplementation = new ServerImplementation(server);

        registry.rebind("MainServer", serverImplementation);
    }
}
