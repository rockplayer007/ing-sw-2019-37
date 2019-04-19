package network.client.rmi;

import network.client.Client;
import network.client.ClientInterface;

public class ClientImplementation implements ClientInterface {
    private Client client;
    ClientImplementation(Client client){
        this.client = client;
    }

    @Override
    public void notify(String message) {

        System.out.println("New notification");
    }

}
