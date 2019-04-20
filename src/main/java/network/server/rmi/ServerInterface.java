package network.server.rmi;

import network.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void register(String name, String clientID, ClientInterface clientInterface) throws RemoteException;

}
