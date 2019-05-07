package network.server;

import network.messages.clientToServer.ClientToServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to implement both RMI and socket
 */
public interface ServerInterface extends Remote {

    /**
     * Takes a message arrived through RMI or socket
     * and sends it to the {@link MainServer}
     * @param message message that arrives from the {@link network.client.MainClient}
     *                and is sent to the {@link MainServer}
     * @throws RemoteException
     */
    void notifyServer(ClientToServer message) throws RemoteException;

}
