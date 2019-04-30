package network.client;

import network.messages.serverToClient.ServerToClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void notifyClient(ServerToClient message) throws RemoteException;
}