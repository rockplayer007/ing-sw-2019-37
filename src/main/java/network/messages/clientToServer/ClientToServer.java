package network.messages.clientToServer;

import network.messages.Message;

public class ClientToServer extends Message {
    private String clientID;
    public ClientToServer(String username, String clientID, Content content){
        super(username, content);
        this.clientID = clientID;
    }

    public String getClientID() {
        return clientID;
    }
}
