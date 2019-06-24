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
import network.messages.Message;
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
    private boolean firstTime;

    private static final Logger logger = Logger.getLogger(CLI.class.getName());

    public CLI(MainClient mainClient){
        this.mainClient = mainClient;
        printer = new Printer(this);
        firstTime = true;
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
            if(firstTime){
                printer.println(printer.colorToAnsi(Color.GREEN) + "Write a username to login:");
                firstTime = false;
            }
            else{
                printer.println(printer.colorToAnsi(Color.RED) + "Write another username to login:");
            }
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
            printer.println(printer.colorToAnsi(Color.GREEN)
                    + "WELCOME "+ mainClient.getUsername() + "\n\n" + printer.colorToAnsi(Color.WHITE));
        }
    }

    @Override
    public void showInfo(String info) {
        printer.closeRequest();
        printer.println(printer.colorToAnsi(AmmoColor.RED) + info + printer.colorToAnsi(Color.WHITE));
    }

    /**
     * Allows the user to choose a board
     * @param maps possible boards to choose from
     */
    public void chooseBoard(Map<Integer, String> maps){
        printer.displayRequest(new ArrayList<>(maps.values()), board -> mainClient.sendSelectedBoard(board),
                "Where do you want to fight?" );
    }

    @Override
    public void chooseWeapon(List<Weapon> weapons, boolean optional, String info) {
        printer.askWeapon(weapons, weapon -> {
            if(weapon >= weapons.size()){
                mainClient.sendSelectedItem(-1, Message.Content.CARD_RESPONSE);
            }
            else {
                mainClient.sendSelectedItem(weapon, Message.Content.CARD_RESPONSE);
            }
        }, optional, info);
    }

    @Override
    public void chooseEffect(List<Effect> effects, boolean optional) {
        printer.askEffect(effects, effect -> mainClient
                .sendSelectedItem(effect, Message.Content.EFFECT_RESPOSNSE), optional);
    }

    @Override
    public void choosePlayer(List<Player> players, String info) {
        printer.askPlayer(players, player -> mainClient
                .sendSelectedItem(player, Message.Content.PLAYER_RESPONSE), info);
    }

    @Override
    public void chooseDirection(List<Square.Direction> directions, String info) {
        printer.askDirection(directions, direction -> mainClient
                .sendSelectedItem(direction, Message.Content.DIRECTION_RESPONSE), info);
    }

    @Override
    public void chooseAmmoColor(List<AmmoColor> ammoColors) {
        printer.askAmmoColor(ammoColors, color -> mainClient
                .sendSelectedItem(color, Message.Content.AMMO_RESPONSE));
    }

    @Override
    public void chooseRoom(List<Color> rooms) {
        printer.askRoom(rooms, room -> mainClient.sendSelectedItem(room, Message.Content.ROOM_RESPONSE));
    }


    public void choosePowerup(List<Powerup> powerups, boolean optional, String info){
        printer.askPowerup(powerups, powerup -> {
            if(powerup >= powerups.size()){
                mainClient.sendSelectedItem(-1, Message.Content.CARD_RESPONSE);
            }
            else {
                mainClient.sendSelectedItem(powerup, Message.Content.CARD_RESPONSE);
            }
        }, optional, info);
    }


    @Override
    public void chooseAction(List<ActionOption> actions) {
        List<String> stringed = new ArrayList<>();
        actions.forEach(x -> stringed.add(x.explenation));
        printer.displayRequest(stringed, board -> mainClient.sendSelectedBoard(board),
                "Go and fight!");
    }

    @Override
    public void chooseSquare(List<Square> squares, String info) {
        printer.askSquare(squares, square -> mainClient
                .sendSelectedItem(square, Message.Content.SQUARE_RESPONSE), info);
    }

    @Override
    public void showAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks) {
        printer.printAttack(attacker, hp, marks);
    }

    public void timeout(){

        printer.askReconnect(reconnect -> {
            if(reconnect) {
                mainClient.sendCredentials();
            }
            else {
                printer.println("Bye bey " + mainClient.getUsername());
            }
        });
    }

    @Override
    public void updateAll(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard) {
        printer.printAllInfo(board, myPowerups, skullBoard);

    }

    @Override
    public void showScore(List<Player> score) {
        printer.printScore(score);
    }

}
