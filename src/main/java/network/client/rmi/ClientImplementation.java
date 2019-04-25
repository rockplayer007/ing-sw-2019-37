package network.client.rmi;

import network.client.Client;
import network.client.ClientInterface;
import network.messages.Message;

public class ClientImplementation implements ClientInterface {
    private Client client;
    ClientImplementation(Client client){
        this.client = client;
    }

    @Override
    public void notifyClient(Message message) {

        System.out.println("New notification");
    }

}
