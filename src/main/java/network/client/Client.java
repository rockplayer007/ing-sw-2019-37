package network.client;

import network.client.rmi.ConnectionRMI;
import view.CLI.CLI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {

    private static String serverIp;
    private ConnectionInterface connection;
    private String username;

    public static void main(String[] args){
        System.out.println("localhost or remote?[L/R]");
        Scanner reader = new Scanner(System.in);
        String choice = reader.nextLine().toLowerCase();
        if(choice.equals("r")){
            System.out.println("Write IP address:");
            serverIp = reader.nextLine();

        }
        else {
            serverIp = "localhost";
        }

        Client client = new Client();
        CLI view = new CLI(client);
        try {
            view.lauch();
        }catch (Exception e ){

        }




    }

    public void connect() throws RemoteException, NotBoundException {
        connection = new ConnectionRMI(this);
        connection.registerClient(username);
    }

    public static String getServerIp() {
        return serverIp;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

}
