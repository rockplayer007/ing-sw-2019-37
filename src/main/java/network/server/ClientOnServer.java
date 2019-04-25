package network.server;

import model.player.Player;
import network.client.ClientInterface;

public class ClientOnServer {

    private String username;
    private ClientInterface clientInterface;
    private Player personalPlayer;

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

    public Player getPersonalPlayer() {
        return personalPlayer;
    }

    public void setPersonalPlayer(Player personalPlayer) {
        this.personalPlayer = personalPlayer;
    }
}
