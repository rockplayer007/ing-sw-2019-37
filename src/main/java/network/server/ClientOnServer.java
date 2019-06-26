package network.server;

import model.player.Player;
import network.client.ClientInterface;

/**
 * Class to store all client's data
 */
public class ClientOnServer {

    private String username;
    private ClientInterface clientInterface;
    private Player personalPlayer;
    private String clientID;

    public ClientOnServer(String username, ClientInterface clientInterface, String clientID){
        this.username = username;
        this.clientInterface = clientInterface;
        this.clientID = clientID;
        this.personalPlayer = new Player(username);
    }

    public String getUsername() {
        return username;
    }
    public ClientInterface getClientInterface(){
        return clientInterface;
    }
    public void setClientInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }


    public Player getPersonalPlayer() {
        return personalPlayer;
    }

    public void setPersonalPlayer(Player personalPlayer) {
        this.personalPlayer = personalPlayer;
    }

    public String getClientID(){
        return clientID;
    }

}
