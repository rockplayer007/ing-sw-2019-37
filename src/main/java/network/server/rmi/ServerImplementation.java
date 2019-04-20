package network.server.rmi;

import network.client.ClientInterface;
import network.server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private Server server;

    protected ServerImplementation(Server server) throws RemoteException {
        this.server = server;
    }

    @Override
    public void register(String username, String clientID, ClientInterface clientInterface) {
        server.addClient(username);
        System.out.println(username + " added to the server");
    }
}
