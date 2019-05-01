package network.server.rmi;

import network.messages.clientToServer.ClientToServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void notifyServer(ClientToServer message) throws RemoteException;

}
