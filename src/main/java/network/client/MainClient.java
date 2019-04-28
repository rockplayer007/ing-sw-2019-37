package network.client;

import network.client.rmi.ConnectionRMI;
import network.client.socket.ConnectionSOCKET;
import network.messages.*;
import network.messages.clientToServer.BoardResponse;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.BoardRequest;
import network.messages.serverToClient.LoginResponse;
import network.server.MainServer;
import view.CLI.CLI;
import view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClient {

    private static String serverIp;
    private ConnectionInterface connection;
    private String username;
    private static final String clientID =  UUID.randomUUID().toString();
    private ClientInterface clientInterface;

    private static ViewInterface view;
    private static boolean socket; //true uses socket false uses rmi

    private static final Logger logger = Logger.getLogger(MainServer.class.getName());

    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);

        System.out.println("RMI or SOCKET?[R/S]");
        String choice = reader.nextLine().toLowerCase();

        if(choice.equals("s")){
            socket = true;
        }
        else {
            socket = false;
        }


        System.out.println("localhost or remote?[L/R]");

        choice = reader.nextLine().toLowerCase();
        if(choice.equals("r")){
            System.out.println("Write IP address of the server:");
            serverIp = reader.nextLine();
        }
        else {
            serverIp = "localhost";
        }


        MainClient mainClient = new MainClient();
         view = new CLI(mainClient);
        try {
            view.launch();
        }catch (Exception e ){
            logger.log(Level.WARNING, "Unable to connect to server", e);
        }

    }

    public void connect() throws  NotBoundException, IOException {

        if(socket){
            connection = new ConnectionSOCKET(serverIp);
        }
        else{
            connection = new ConnectionRMI(this);
        }

    }

    public void sendCredentials(){
        if (socket) {
            //TODO  special message for socket without clientInterface
            connection.sendMessage(new LoginRequest(username, clientInterface, clientID));
        }
        else {
            connection.sendMessage(new LoginRequest(username, clientInterface, clientID));
        }

    }
    public void sendSelectedBoard(int board){
        connection.sendMessage(new BoardResponse(username, clientID, board));
    }

    public void handleMessage(Message message){
            switch (message.getContent()){
            case LOGIN_RESPONSE:
                view.logIn(((LoginResponse) message).getStatus());
                break;
            case BOARD_REQUEST:
                view.chooseBoard(((BoardRequest) message).getBoards());
                break;
            default:
                logger.log(Level.WARNING, "Unregistered message");

        }
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
    public static String getClientID(){
        return clientID;
    }
    public void setClientInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }

}
