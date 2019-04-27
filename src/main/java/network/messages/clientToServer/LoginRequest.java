package network.messages.clientToServer;

import network.client.ClientInterface;
import network.messages.Message;

public class LoginRequest extends ClientToServer {

    private ClientInterface clientInterface;
    private String clientID;

    public LoginRequest(String username, ClientInterface clientInterface, String clientID){
        super(username, clientID, Content.LOGIN_REQUEST);

        this.clientInterface = clientInterface;
        this.clientID = clientID;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    public String getClientID() {
        return clientID;
    }
}