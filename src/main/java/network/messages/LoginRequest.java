package network.messages;

import network.client.ClientInterface;

public class LoginRequest extends Message{

    private ClientInterface clientInterface;

    public LoginRequest(String username, ClientInterface clientInterface){
        super(username, Content.LOGIN_REQUEST);

        this.clientInterface = clientInterface;
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }
}
