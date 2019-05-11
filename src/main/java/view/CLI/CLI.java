package view.CLI;

import network.client.MainClient;
import view.ViewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Command Line Interface for the user
 */
public class CLI implements ViewInterface {

    private MainClient mainClient;
    private Printer printer;

    private static final Logger logger = Logger.getLogger(CLI.class.getName());

    public CLI(MainClient mainClient){
        this.mainClient = mainClient;
        printer = new Printer();
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

        printer.print("Connection successful!");
        logIn(true);

    }

    private void chooseConnection(){
        printer.print("RMI or SOCKET?[R/S] (default RMI)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            //Scanner reader = new Scanner(System.in);
            String choice = reader.readLine().toLowerCase();

            //if true its socket
            MainClient.setSocket(choice.equals("s"));

            printer.print("localhost or remote?[L/R] (default localhost)");

            choice = reader.readLine().toLowerCase();
            if (choice.equals("r")) {
                printer.print("Write IP address of the server:");
                MainClient.setServerIp(reader.readLine());
            } else {
                MainClient.setServerIp("localhost");
            }
        }catch (IOException e){
            logger.log(Level.WARNING, "Input error", e);
        }

    }

    /**
     * Allows the user to set a username
     * @param ask if true asks for the username, if false welcomes the user
     */
    public void logIn(boolean ask){

        if (ask){
            printer.print("Write a username to login:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String username = null;
            try {
                username = reader.readLine();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Input error", e);
            }
            mainClient.setUsername(username);
            mainClient.sendCredentials();
        }
        else {
            printer.print("Welcome "+ mainClient.getUsername());
        }
    }

    /**
     * Allows the user to choose a board
     * @param maps possible boards to choose from
     */
    public void chooseBoard(Map<Integer, String> maps){
        printer.displayRequest(new ArrayList<>(maps.values()), board -> mainClient.sendSelectedBoard(board));
    }


    public void timeout(){
        printer.closeRequest();
    }

}
