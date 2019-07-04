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

    /**
     * Constructor for a new client information container
     * @param username the name of the client
     * @param clientInterface the interface to connect
     * @param clientID the client's id
     */
    public ClientOnServer(String username, ClientInterface clientInterface, String clientID){
        this.username = username;
        this.clientInterface = clientInterface;
        this.clientID = clientID;
        this.personalPlayer = new Player(username);
    }

    /**
     * Gives the name of the client
     * @return username of the client
     */
    public String getUsername() {
        return username;
    }

    /**
     * Give the interface to connect with the client
     * @return client's interface
     */
    public ClientInterface getClientInterface(){
        return clientInterface;
    }

    /**
     * Setter fot the client's interface
     * @param clientInterface the interface to connect with the client
     */
    void setClientInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }

    /**
     * Getter fo the client's player
     * @return the Player of the client
     */
    public Player getPersonalPlayer() {
        return personalPlayer;
    }

    /*
    public void setPersonalPlayer(Player personalPlayer) {
        this.personalPlayer = personalPlayer;
    }

     */

    /**
     * To get the client's id
     * @return the id of the client
     */
    String getClientID(){
        return clientID;
    }

}
