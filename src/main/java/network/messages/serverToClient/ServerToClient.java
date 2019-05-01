package network.messages.serverToClient;

import network.messages.Message;

public class ServerToClient extends Message {

    public ServerToClient(Content content){
        super("server", content);
    }
}
