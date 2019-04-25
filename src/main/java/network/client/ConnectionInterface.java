package network.client;

import network.messages.Message;

public interface ConnectionInterface {

    //void registerClient(String userName,String clientID);

    void sendMessage(Message message);
}
