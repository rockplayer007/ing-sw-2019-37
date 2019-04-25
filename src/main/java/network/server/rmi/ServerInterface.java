package network.server.rmi;

import network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void notifyServer(Message message) throws RemoteException;

}
