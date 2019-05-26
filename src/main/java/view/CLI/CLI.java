package view.CLI;

import model.board.Color;
import model.board.GameBoard;
import model.board.SkullBoard;
import model.board.Square;
import model.card.AmmoColor;
import model.card.Effect;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;
import model.player.Player;
import network.client.MainClient;
import view.ViewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        printer = new Printer(this);
    }

    MainClient getMainClient(){
        return mainClient;
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

        //TODO doesnt work with rmi
        printer.println("Connection successful!");
        logIn(true);

    }

    private void chooseConnection(){
        printer.println("RMI or SOCKET?[R/S] (default RMI)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            //Scanner reader = new Scanner(System.in);
            String choice = reader.readLine().toLowerCase();

            //if true its socket
            MainClient.setSocket(choice.equals("s"));

            printer.println("localhost or remote?[L/R] (default localhost)");

            choice = reader.readLine().toLowerCase();
            if (choice.equals("r")) {
                printer.println("Write IP address of the server:");
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
            printer.println("Write a username to login:");
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
            printer.println("Welcome "+ mainClient.getUsername());
        }
    }

    /**
     * Allows the user to choose a board
     * @param maps possible boards to choose from
     */
    public void chooseBoard(Map<Integer, String> maps){
        printer.displayRequest(new ArrayList<>(maps.values()), board -> mainClient.sendSelectedBoard(board));
    }

    @Override
    public void chooseWeapon(List<Weapon> weapons, boolean optional) {
        printer.askWeapon(weapons, weapon -> {
            if(weapon >= weapons.size()){
                mainClient.sendSelectedCard(-1);
            }
            else {
                mainClient.sendSelectedCard(weapon);
            }
        }, optional);
    }

    @Override
    public void chooseEffect(List<Effect> effects) {
        printer.askEffect(effects, effect -> mainClient.sendSelectedCard(effect));
    }

    @Override
    public void choosePlayer(List<Player> players) {
        printer.askPlayer(players, player -> mainClient.sendSelectedPlayer(player));
    }

    @Override
    public void chooseDirection(List<Square.Direction> directions) {
        printer.askDirection(directions, direction -> mainClient.sendSelectedDirection(direction));
    }

    @Override
    public void chooseAmmoColor(List<AmmoColor> ammoColors) {
        printer.askAmmoColor(ammoColors, color -> mainClient.sendSelectedAmmoColor(color));
    }

    @Override
    public void chooseRoom(List<Color> rooms) {
        printer.askRoom(rooms, room -> mainClient.sendSelectedRoom(room));
    }

    public void choosePowerup(List<Powerup> powerups, boolean optional){
        printer.askPowerup(powerups, powerup -> {
            if(powerup >= powerups.size()){
                mainClient.sendSelectedCard(-1);
            }
            else {
                mainClient.sendSelectedCard(powerup);
            }
        }, optional);
    }


    @Override
    public void chooseAction(List<ActionOption> actions) {
        List<String> stringed = new ArrayList<>();
        actions.forEach(x -> stringed.add(x.explenation));
        printer.displayRequest(stringed, board -> mainClient.sendSelectedBoard(board));
    }

    @Override
    public void chooseSquare(List<Square> squares) {
        printer.askSquare(squares, square -> mainClient.sendSelectedSquare(square));
    }


    public void timeout(){
        printer.println("You finished your time for choosing");
        printer.closeRequest();
    }

    @Override
    public void updateAll(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard) {
        printer.printAllInfo(board, myPowerups, skullBoard);

    }



}
