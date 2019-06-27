package network.messages.clientToServer;

import network.client.ClientInterface;
import network.messages.Message;

public class ConnectionMessage extends ClientToServer{

    private ClientInterface clientInterface;
    public ConnectionMessage(String username, ClientInterface clientInterface, String clientID){
        super(username, clientID, Content.CONNECTION);

        this.clientInterface = clientInterface;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }
    public void setClientInterface(ClientInterface client){
        clientInterface = client;
    }
}
