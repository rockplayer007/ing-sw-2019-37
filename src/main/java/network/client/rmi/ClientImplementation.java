package network.client.rmi;

import network.client.MainClient;
import network.client.ClientInterface;
import network.messages.serverToClient.ServerToClient;

/**
 * Implemets the {@link ClientInterface} to allow to execute the actions
 */
public class ClientImplementation implements ClientInterface {
    private MainClient mainClient;
    ClientImplementation(MainClient mainClient){
        this.mainClient = mainClient;
    }

    /**
     * Sends the massage directly to the client to handle it
     * @param message message that arrives from the {@link MainClient}
     *                and is sent to the {@link network.client.MainClient}
     */
    @Override
    public void notifyClient(ServerToClient message) {
        mainClient.handleMessage(message);
    }

}
