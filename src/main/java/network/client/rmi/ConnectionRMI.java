package network.client.rmi;

import network.client.MainClient;
import network.client.ClientInterface;
import network.client.ConnectionInterface;
import network.messages.clientToServer.ClientToServer;
import network.server.rmi.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionRMI implements ConnectionInterface {

    private ServerInterface server;
    private ClientInterface remoteClientRef;
    private static final Logger logger = Logger.getLogger(ConnectionRMI.class.getName());

    public ConnectionRMI(MainClient c) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(MainClient.getServerIp(), 1099);
        server = (ServerInterface) registry.lookup("MainServer");
        ClientImplementation client = new ClientImplementation(c);

        //userful for the server to be able to communicate with the client
        remoteClientRef = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
        c.setClientInterface(remoteClientRef);


    }

    @Override
    public void sendMessage(ClientToServer message) {
        try {
            server.notifyServer(message);
        } catch (RemoteException e){
            logger.log(Level.WARNING, "Unable to send message", e);
        }

    }
}
