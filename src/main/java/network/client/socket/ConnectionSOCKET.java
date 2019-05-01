package network.client.socket;

import network.client.ClientInterface;
import network.client.ConnectionInterface;
import network.client.MainClient;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.ServerToClient;

import java.io.IOException;

public class ConnectionSOCKET implements ConnectionInterface, ClientInterface {

    private MainClient client;
    private ServerSimulator serverSimulator;

    public ConnectionSOCKET(MainClient client) throws IOException {
        this.client = client;
        serverSimulator = new ServerSimulator(this, client.getServerIp(), 8000);

    }

    @Override
    public void notifyClient(ServerToClient message){
        client.handleMessage(message);
    }

    @Override
    public void sendMessage(ClientToServer message){
        serverSimulator.notifyServer(message);
    }

}
