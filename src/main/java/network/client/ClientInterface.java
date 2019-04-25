package network.client;

import network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void notifyClient(Message message) throws RemoteException;
}