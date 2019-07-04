package network.client.rmi;

import network.client.MainClient;
import network.client.ClientInterface;
import network.client.ConnectionInterface;
import network.messages.Message;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.ServerToClient;
import network.server.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Creats an RMI connection to allow the {@link MainClient} to reach the {@link network.server.MainServer}
 */
public class ConnectionRMI implements ConnectionInterface {

    private ServerInterface server;
    private MainClient mainClient;

    /**
     * Constructor to set up the connection
     * @param c to keep a reference to the main client
     * @throws RemoteException when the client disconnects
     * @throws NotBoundException when there is an error in binding
     */
    public ConnectionRMI(MainClient c) throws RemoteException, NotBoundException {
        mainClient = c;
        Registry registry = LocateRegistry.getRegistry(MainClient.getServerIp(), 1099);
        server = (ServerInterface) registry.lookup("MainServer");
        ClientImplementation client = new ClientImplementation(mainClient);

        //userful for the server to be able to communicate with the client
        ClientInterface remoteClientRef = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
        mainClient.setClientInterface(remoteClientRef);
    }

    /**
     * Sends the message to the {@link network.server.MainServer}
     * @param message the message that arrives from the client
     */
    @Override
    public void sendMessage(ClientToServer message) {
        try {
            server.notifyServer(message);
        } catch (RemoteException e){
            //logger.log(Level.WARNING, "Unable to send message", e);
            mainClient.handleMessage(new ServerToClient(Message.Content.DISCONNECTION));
        }

    }
}
