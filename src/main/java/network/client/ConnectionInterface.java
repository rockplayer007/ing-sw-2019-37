package network.client;

import network.messages.Message;
import network.messages.clientToServer.ClientToServer;

public interface ConnectionInterface {

    //void registerClient(String userName,String clientID);

    void sendMessage(ClientToServer message);
}
