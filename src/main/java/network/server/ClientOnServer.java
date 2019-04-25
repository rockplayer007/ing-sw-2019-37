package network.server;

import network.client.ClientInterface;

public class ClientOnServer {

    private String username;
    private ClientInterface clientInterface;

    public ClientOnServer(String username, ClientInterface clientInterface){
        this.username = username;
        this.clientInterface = clientInterface;

    }

    public String getUsername() {
        return username;
    }
    public ClientInterface getClientInterface(){
        return clientInterface;
    }
}
