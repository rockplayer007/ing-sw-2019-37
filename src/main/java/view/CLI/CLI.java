package view.CLI;

import network.client.MainClient;
import view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Map;
import java.util.Scanner;


/**
 * Command Line Interface for the user
 */
public class CLI implements ViewInterface {

    private MainClient mainClient;
    public CLI(MainClient mainClient){
        this.mainClient = mainClient;
    }

    /**
     * Connects the chosen network and then allows him to log in
     * @throws NotBoundException
     * @throws IOException
     */
    @Override
    public void launch() throws NotBoundException, IOException {

        chooseConnection();
        mainClient.connect();

        System.out.println("Connection successful!");
        logIn(true);

    }

    private void chooseConnection(){
        System.out.println("RMI or SOCKET?[R/S]");
        Scanner reader = new Scanner(System.in);
        String choice = reader.nextLine().toLowerCase();

        //if true its socket
        mainClient.setSocket(choice.equals("s"));

        System.out.println("localhost or remote?[L/R]");

        choice = reader.nextLine().toLowerCase();
        if (choice.equals("r")) {
            System.out.println("Write IP address of the server:");
            mainClient.setServerIp(reader.nextLine());
        } else {
            mainClient.setServerIp("localhost");
        }
    }

    /**
     * Allows the user to set a username
     * @param ask if true asks for the username, if false welcomes the user
     */
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

    /**
     * Allows the user to choose a board
     * @param maps possible boards to choose from
     */
    public void chooseBoard(Map<Integer, String> maps){
        maps.forEach((k,v)-> System.out.println("Map number  " + k + " " + v));
        System.out.println("Select map: ");
        Scanner reader = new Scanner(System.in);
        mainClient.sendSelectedBoard(reader.nextInt());
    }
}
