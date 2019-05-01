package network.client;

import network.client.rmi.ConnectionRMI;
import network.client.socket.ConnectionSOCKET;
import network.messages.clientToServer.BoardResponse;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.BoardRequest;
import network.messages.serverToClient.LoginResponse;
import network.messages.serverToClient.ServerToClient;
import network.server.MainServer;
import view.CLI.CLI;
import view.GUI.GUI;
import view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
<<<<<<< HEAD
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
=======
>>>>>>> a3a0e3e51d687ea5c920d0108ee08b5fa6499629
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClient {

    private static String serverIp;
    private ConnectionInterface connection;
    private String username;
    private String clientID;
    private ClientInterface clientInterface;

    private static ViewInterface view;
    private static boolean socket; //true uses socket false uses rmi

    private static final Logger logger = Logger.getLogger(MainServer.class.getName());

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("CLI or GUI?[C/G]");
        String choice = reader.nextLine().toLowerCase();

        if (choice.equals("g")) {
            MainClient mainClient = new MainClient();
            view = new GUI(mainClient);

            try {
                view.launch();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to connect to server", e);
            }
            /*
            //usato solo per test
            Map<Integer,String> map = new HashMap<>();
          map.put( 1 , "ideale per 3/4 giocatori");
            map.put(  2,"ideale per 3/4 giocatori" );
          map.put(  3,"third" );
            map.put(  0,"quarto" );
            view.chooseBoard(map);*/
        }
        else {


            System.out.println("RMI or SOCKET?[R/S]");
            choice = reader.nextLine().toLowerCase();

            if (choice.equals("s")) {
                socket = true;
            } else {
                socket = false;
            }


<<<<<<< HEAD
            System.out.println("localhost or remote?[L/R]");
=======
        MainClient mainClient = new MainClient();
        view = new CLI(mainClient);
        try {
            view.launch();
        }catch (Exception e ){
            logger.log(Level.WARNING, "Unable to connect to server", e);
        }
>>>>>>> a3a0e3e51d687ea5c920d0108ee08b5fa6499629

            choice = reader.nextLine().toLowerCase();
            if (choice.equals("r")) {
                System.out.println("Write IP address of the server:");
                serverIp = reader.nextLine();
            } else {
                serverIp = "localhost";
            }


            MainClient mainClient = new MainClient();
            view = new CLI(mainClient);
            try {
                view.launch();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to connect to server", e);
            }

        }
    }

    public void connect() throws  NotBoundException, IOException {

        if(socket){
            connection = new ConnectionSOCKET(this);
        }
        else{
            connection = new ConnectionRMI(this);
        }

    }

    public void sendCredentials(){
        if (socket) {
            connection.sendMessage(new LoginRequest(username, null, ""));
            //TODO rename the login request
            //connection.sendMessage(new LoginSocketRequest(username, clientID));

        }
        else {
            connection.sendMessage(new LoginRequest(username, clientInterface, ""));
        }

    }
    public void sendSelectedBoard(int board){
        connection.sendMessage(new BoardResponse(username, clientID, board));
    }

    public void handleMessage(ServerToClient message){
            switch (message.getContent()){
            case LOGIN_RESPONSE:
                clientID = ((LoginResponse) message).getClientID();
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
    public void setClientInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }
    public void setSocket(Boolean connection){
        this.socket=connection;
    }
}
