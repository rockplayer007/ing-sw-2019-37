package view.CLI;

import network.client.MainClient;
import view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;

public class CLI implements ViewInterface {

    private MainClient mainClient;
    public CLI(MainClient mainClient){
        this.mainClient = mainClient;
    }

    @Override
    public void launch() throws NotBoundException, IOException {

        mainClient.connect();

        System.out.println("Connection successful!");
        logIn(true);

    }

    //if ask is true then ask the username
    public void logIn(boolean ask){
        if (ask){
            System.out.println("Write a username to login:");
            Scanner reader = new Scanner(System.in);
            String username = reader.nextLine();
            mainClient.setUsername(username);
            mainClient.sendCredentials();
        }
        else {
            System.out.println("Welcome "+ mainClient.getUsername());
        }

    }

    public void chooseBoard(Map<Integer, String> maps){
        maps.forEach((k,v)-> System.out.println("Map number  " + k + " " + v));
        System.out.println("Select map: ");
        Scanner reader = new Scanner(System.in);
        mainClient.sendSelectedBoard(reader.nextInt());
    }

}
