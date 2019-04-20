package network.client.rmi;

import network.client.Client;
import network.client.ClientInterface;
import network.client.ConnectionInterface;
import network.server.rmi.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ConnectionRMI implements ConnectionInterface {

    private ServerInterface server;
    private ClientInterface remoteClientRef;

    public ConnectionRMI(Client c) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(Client.getServerIp(), 1099);
        server = (ServerInterface) registry.lookup("Server");
        ClientImplementation client = new ClientImplementation(c);

        //the server will be able to communicate with the client
        remoteClientRef = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);

    }

    @Override
    public void registerClient(String username, String clientID) {
        try {
            server.register(username, clientID, remoteClientRef);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }
}
