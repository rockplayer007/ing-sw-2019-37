package network.server.rmi;

import network.messages.clientToServer.ClientToServer;
import network.server.MainServer;
import network.server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implemets the {@link ServerInterface} to allow to execute the actions
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private transient MainServer server;

    ServerImplementation(MainServer server) throws RemoteException {
        this.server = server;
    }

    /**
     * Sends the massage directly to the server to handle it
     * @param message message that arrives from the {@link network.client.MainClient}
     *                and is sent to the {@link MainServer}
     * @throws RemoteException when there is a problem with the connection
     */
    @Override
    public void notifyServer(ClientToServer message) throws RemoteException{
        server.handleMessage(message);
    }
}
