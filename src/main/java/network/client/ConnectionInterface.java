package network.client;

import network.messages.clientToServer.ClientToServer;

/**
 * Interface for RMI and socket Client side to send a message to the server
 */
public interface ConnectionInterface {

    /**
     * Allows to send a message to the {@link network.server.MainServer}
     * @param message
     */
    void sendMessage(ClientToServer message);
}
