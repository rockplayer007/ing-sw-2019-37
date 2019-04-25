package network.server.rmi;

import network.client.ClientInterface;
import network.messages.Message;
import network.server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private transient Server server;

    public ServerImplementation(Server server) throws RemoteException {
        this.server = server;
    }

    @Override
    public void notifyServer(Message message) throws RemoteException{
        server.handleMessage(message);
    }
}
