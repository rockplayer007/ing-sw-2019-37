package network.client.rmi;

import network.client.MainClient;
import network.client.ClientInterface;
import network.messages.Message;

public class ClientImplementation implements ClientInterface {
    private MainClient mainClient;
    ClientImplementation(MainClient mainClient){
        this.mainClient = mainClient;
    }

    @Override
    public void notifyClient(Message message) {
        mainClient.handleMessage(message);

    }

}
