package network.client.rmi;

import network.client.MainClient;
import network.client.ClientInterface;
import network.messages.serverToClient.ServerToClient;

public class ClientImplementation implements ClientInterface {
    private MainClient mainClient;
    ClientImplementation(MainClient mainClient){
        this.mainClient = mainClient;
    }

    @Override
    public void notifyClient(ServerToClient message) {
        mainClient.handleMessage(message);

    }

}
