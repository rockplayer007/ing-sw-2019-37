package network.messages.clientToServer;

import network.client.ClientInterface;

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
    public void setClientInterface(ClientInterface client){
        clientInterface = client;
    }

    @Override
    public String getClientID() {
        return clientID;
    }
}
