package network.client.rmi;

import network.client.MainClient;
import network.client.ClientInterface;
import network.client.ConnectionInterface;
import network.messages.clientToServer.ClientToServer;
import network.server.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creats an RMI connection to allow the {@link MainClient} to reach the {@link network.server.MainServer}
 */
public class ConnectionRMI implements ConnectionInterface {

    private ServerInterface server;
    private ClientInterface remoteClientRef;
    private static final Logger logger = Logger.getLogger(ConnectionRMI.class.getName());

    /**
     * Constructor to set up the connection
     * @param c
     * @throws RemoteException
     * @throws NotBoundException
     */
    public ConnectionRMI(MainClient c) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(MainClient.getServerIp(), 1099);
        server = (ServerInterface) registry.lookup("MainServer");
        ClientImplementation client = new ClientImplementation(c);

        //userful for the server to be able to communicate with the client
        remoteClientRef = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
        c.setClientInterface(remoteClientRef);
    }

    /**
     * Sends the message to the {@link network.server.MainServer}
     * @param message
     */
    @Override
    public void sendMessage(ClientToServer message) {
        try {
            server.notifyServer(message);
        } catch (RemoteException e){
            logger.log(Level.WARNING, "Unable to send message", e);
        }

    }
}
