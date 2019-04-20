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

    public void launch() {
        try{
            client.connect();
        }catch (RemoteException | NotBoundException e){
            System.out.println("Unable to connect to server\n" + e.getMessage());
        }
        System.out.println("Connection successul!");

        System.out.println("Write username to login:");
        Scanner reader = new Scanner(System.in);
        String username = reader.nextLine();
        client.setUsername(username);

        client.sendCredentials();
    }
}
