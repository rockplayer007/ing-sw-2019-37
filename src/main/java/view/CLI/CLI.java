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


    public void launch() throws RemoteException, NotBoundException{

        client.connect();
        System.out.println("Connection successful!");
        logIn(true);

    }

    //if ask is true then ask the username
    public void logIn(boolean ask){
        if (ask){
            System.out.println("Write a username to login:");
            Scanner reader = new Scanner(System.in);
            String username = reader.nextLine();
            client.setUsername(username);
            client.sendCredentials();
        }
        else {
            System.out.println("Welcome "+ client.getUsername());
        }

    }
}
