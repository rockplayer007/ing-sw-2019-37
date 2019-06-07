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
     * @param client
     * @throws IOException
     */
    public ConnectionSOCKET(MainClient client) throws IOException {
        this.client = client;
        serverSimulator = new ServerSimulator(this, client.getServerIp(), 8000);

    }

    /**
     * Redirects the message to the {@link MainClient}
     * @param message
     */
    @Override
    public void notifyClient(ServerToClient message){
        client.handleMessage(message);
    }

    @Override
    public void closeConnection() {

    }

    /**
     * Redirects the message to the {@link ServerSimulator}
     * to send it to the {@link network.server.MainServer}
     * @param message
     */
    @Override
    public void sendMessage(ClientToServer message){
        serverSimulator.notifyServer(message);
    }

}
