package view.CLI;

import network.client.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class CLI {

    private Client client;
    public CLI(Client client){
        this.client = client;
    }

    public void lauch() throws RemoteException, NotBoundException {
        System.out.println("Write username");
        Scanner reader = new Scanner(System.in);
        String username = reader.nextLine();
        client.setUsername(username);

        client.connect();
    }
}
