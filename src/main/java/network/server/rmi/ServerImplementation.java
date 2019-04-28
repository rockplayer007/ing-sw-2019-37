package network.server.rmi;

import network.messages.clientToServer.ClientToServer;
import network.server.MainServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private transient MainServer server;

    public ServerImplementation(MainServer server) throws RemoteException {
        this.server = server;
    }

    @Override
    public void notifyServer(ClientToServer message) throws RemoteException{
        server.handleMessage(message);
    }
}
