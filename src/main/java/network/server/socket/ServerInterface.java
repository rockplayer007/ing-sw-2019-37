package network.server.socket;

import network.messages.clientToServer.ClientToServer;

public interface ServerInterface {

    void notifyServer(ClientToServer message);
}
