package network.messages;

import network.client.ClientInterface;

public class LoginRequest extends Message{

    private ClientInterface clientInterface;
    private String clientID;

    public LoginRequest(String username, ClientInterface clientInterface, String clientID){
        super(username, Content.LOGIN_REQUEST);

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
