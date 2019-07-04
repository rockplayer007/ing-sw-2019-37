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

    /**
     * Constructor for the RMI server
     * @param server where to send the messages to
     */
    public ServerRMI(MainServer server){
        this.server = server;
    }

    /**
     * Starts the server on a specific port
     * @param port the port number where to connect the rmi
     * @throws RemoteException the exception thrown when there is an error in the connection
     */
    public void startServer(int port) throws RemoteException {
        //System.setProperty("java.rmi.server.hostname", "localhost");
        Registry registry = LocateRegistry.createRegistry(port);
        ServerImplementation serverImplementation = new ServerImplementation(server);

        registry.rebind("MainServer", serverImplementation);
    }
}
