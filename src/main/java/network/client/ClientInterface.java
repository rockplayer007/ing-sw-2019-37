package network.client;

import network.messages.serverToClient.ServerToClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for RMI and socket to send messages to the client
 */
public interface ClientInterface extends Remote {

    /**
     * Redirects the message that comes from the {@link network.server.MainServer}
     * to the {@link MainClient}
     * @param message
     * @throws RemoteException
     */
    void notifyClient(ServerToClient message) throws RemoteException;
}