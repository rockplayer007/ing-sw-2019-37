package network.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Configs {
    private static Configs ourInstance = new Configs();

    private int turnTime;// = 180;
    private int minimumPlayers;// = 3;
    private int maximumPlayers;// = 5;
    private int timeForTagBackRequest;// = 10;
    private int waitingTime; // = 30;
    private int rmiPort; //1099
    private int socketPort; //



    private int skulls;// = 8;


    public static Configs getInstance() {
        return ourInstance;
    }

    private Configs() {
        readFile();
    }

    private void readFile(){
        String path = "."+ File.separatorChar + "src" + File.separatorChar+
                "main" + File.separatorChar + "resources" + File.separatorChar + "configuration.txt";
        File inputFile = new File(path);

        Scanner scanner;
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            return;
        }

        turnTime = fill("turn time",180, scanner.nextLine());
        minimumPlayers = fill("minimum players", 3, scanner.nextLine());
        maximumPlayers = fill("maximum players", 3, scanner.nextLine());
        timeForTagBackRequest = fill("time for TAGBACK GRANADE", 10, scanner.nextLine());
        waitingTime = fill("creating room time", 30, scanner.nextLine());
        skulls = fill("skulls", 8, scanner.nextLine());
        rmiPort = fill("RMI port", 1099, scanner.nextLine());
        socketPort = fill("Socket port", 8000, scanner.nextLine());


    }

    private boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }


    private int fill(String type, int def, String line){
        String[] fields = line.split(":");

        if(fields[0].equals(type)){
            if(isNumeric(fields[1].trim())){
                def = Integer.parseInt(fields[1].trim());
            }
        }
        return def;
    }

    public int getTurnTime() {
        return turnTime;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public int getTimeForTagBackRequest() {
        return timeForTagBackRequest;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getSkulls() {
        return skulls;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public int getSocketPort() {
        return socketPort;
    }
}



