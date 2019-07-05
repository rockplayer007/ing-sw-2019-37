package network.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * Singleton class for loading the the configuration file
 */
public class Configs {
    private static Configs ourInstance = new Configs();

    private int turnTime;// = 200;

    private int minimumPlayers;// = 3;
    private int maximumPlayers;// = 5;

    private int timeForTagBackRequest;// = 10;
    private int waitingTime; // = 30;
    private int boardRequestTime; // = 10;
    private int respawnTime; // = 15

    private int adrenalin1; // = 3;
    private int adrenalin2; // = 6;
    private int deadPoint; // = 11;
    private int overKill; // = 12;

    private int rmiPort; //1099
    private int socketPort; //

    private int skulls;// = 8;

    /**
     * Static instance of the configuration singleton
     * @return the configuration class to access all the configurations
     */
    public static Configs getInstance() {
        return ourInstance;
    }

    /**
     * Private constructor
     */
    private Configs() {
        readFile();
    }

    /**
     * The method will read the configurations from the file and set them
     */
    private void readFile(){
        String filename = "configuration.txt";
        File inputFile;
        String path;
        Scanner scanner;
        try {
            path = new File(getClass().getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParent() + File.separatorChar + filename;

            inputFile = new File(path);

            if(!inputFile.exists()){

                InputStream inputStream = getClass().getResourceAsStream("/"+ filename);

                scanner = new Scanner(inputStream);

            }
            else {

                scanner = new Scanner(inputFile);
            }

        } catch (URISyntaxException| FileNotFoundException e) {

            scanner = new Scanner(getConfigs());

        }


        turnTime = fill("turn time",200, scanner.nextLine());

        minimumPlayers = fill("minimum players", 3, scanner.nextLine());
        maximumPlayers = fill("maximum players", 5, scanner.nextLine());

        timeForTagBackRequest = fill("time for TAGBACK GRANADE", 10, scanner.nextLine());
        waitingTime = fill("creating room time", 30, scanner.nextLine());
        boardRequestTime = fill("choosing board time", 10, scanner.nextLine());
        respawnTime = fill("respawn time", 15, scanner.nextLine());

        skulls = fill("skulls", 8, scanner.nextLine());

        adrenalin1 = fill("points the first adrenalin actions", 3, scanner.nextLine());
        adrenalin2 = fill("points the second adrenalin actions", 6, scanner.nextLine());
        deadPoint = fill("points the dead point", 11, scanner.nextLine());
        overKill = fill("points the over kill", 12, scanner.nextLine());

        rmiPort = fill("RMI port", 1099, scanner.nextLine());
        socketPort = fill("Socket port", 8000, scanner.nextLine());


    }

    /**
     * Tells if the string is a number
     * @param maybeNumeric the string with the number
     * @return true if it is a number
     */
    private boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }

    /**
     * Reads the string and converts it to the parameter number or to the default
     * @param type name of the configuration parameters
     * @param def the default value
     * @param line the full line
     * @return the number of the parameter
     */
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

    int getWaitingTime() {
        return waitingTime;
    }

    public int getBoardRequestTime() {
        return boardRequestTime;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public int getSkulls() {
        return skulls;
    }

    public int getAdrenalin1() {
        return adrenalin1;
    }

    public int getAdrenalin2() {
        return adrenalin2;
    }

    public int getDeadPoint() {
        return deadPoint;
    }

    public int getOverKill() {
        return overKill;
    }

    int getRmiPort() {
        return rmiPort;
    }

    int getSocketPort() {
        return socketPort;
    }

    private String getConfigs(){
        return "turn time: 200\n" +
                "minimum players: 3\n" +
                "maximum players: 5\n" +
                "time for TAGBACK GRANADE: 10\n" +
                "creating room time: 30\n" +
                "choosing board time: 10\n" +
                "respawn time: 15\n" +
                "skulls: 8\n" +
                "points the first adrenalin actions: 3\n" +
                "points the second adrenalin actions: 6\n" +
                "points the dead point: 11\n" +
                "points the over kill: 12\n" +
                "RMI port: 1099\n" +
                "Socket port: 8000";
    }

}



