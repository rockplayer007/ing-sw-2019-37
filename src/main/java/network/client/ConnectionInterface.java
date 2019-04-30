package network.client;

import network.messages.clientToServer.ClientToServer;

public interface ConnectionInterface {

    void sendMessage(ClientToServer message);
}
