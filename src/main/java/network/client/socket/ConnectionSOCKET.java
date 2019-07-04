package network.client.socket;

import network.client.ClientInterface;
import network.client.ConnectionInterface;
import network.client.MainClient;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.ServerToClient;

import java.io.IOException;

/**
 * Layer between the socket server and the client to send and receive messages
 */
public class ConnectionSOCKET implements ConnectionInterface, ClientInterface {

    private MainClient client;
    private ServerSimulator serverSimulator;

    /**
     * Constructor to start a {@link ServerSimulator}
     * @param client the player that wants to connect
     * @throws IOException when there is an error in connecting
     */
    public ConnectionSOCKET(MainClient client) throws IOException {
        this.client = client;
        serverSimulator = new ServerSimulator(this, MainClient.getServerIp(), MainClient.getServerPort());

    }

    /**
     * Redirects the message to the {@link MainClient}
     * @param message the message that needs to be sent to the client
     */
    @Override
    public void notifyClient(ServerToClient message){
        client.handleMessage(message);
    }

    @Override
    public void closeConnection() {
        //no need to close the connection for the client yet
    }

    /**
     * Redirects the message to the {@link ServerSimulator}
     * to send it to the {@link network.server.MainServer}
     * @param message the message that needs to be sent to the server
     */
    @Override
    public void sendMessage(ClientToServer message){
        serverSimulator.notifyServer(message);
    }

}
