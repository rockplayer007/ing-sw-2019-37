package view.BOT;

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
import view.CLI.Printer;
import view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Command Line Interface for the user
 */
public class BOT implements ViewInterface {

    private MainClient mainClient;
    private Printer printer = new Printer();
    private GameBoard board;
    private List<Powerup> myPowerups;
    private SkullBoard skullBoard;


    public BOT(MainClient mainClient){
        this.mainClient = mainClient;
    }

    /**
     * Connects the chosen network and then allows him to log in
     */
    @Override
    public void launch()  {

        chooseConnection();
        try {
            mainClient.connect();
        } catch (NotBoundException| IOException e) {
            disconnection();
        }

        logIn(true);

    }

    private void chooseConnection(){

        //if true its socket
        MainClient.setSocket(true);

        MainClient.setServerIp("localhost");

    }

    /**
     * Allows the user to set a username
     * @param ask if true asks for the username, if false welcomes the user
     */
    public void logIn(boolean ask){

        if (ask){
            String name = "BOT_" + new Random().nextInt(1000);
            printer.println("HELLO! I AM " + name);
            mainClient.setUsername(name);
            mainClient.sendCredentials();
        }

    }

    @Override
    public void showInfo(String info) {
        //dont show for bot
    }

    /**
     * Allows the user to choose a board
     * @param maps possible boards to choose from
     */
    public void chooseBoard(Map<Integer, String> maps){
        showChoices(new ArrayList<>(maps.values()), "board");
        mainClient.sendSelectedBoard(randomChoice(new ArrayList<>(maps.values())));
    }

    @Override
    public void chooseWeapon(List<Weapon> weapons, boolean optional, String info) {
        showChoices(weapons, "'" +info + "'");
        mainClient.sendSelectedItem(randomChoice(weapons, optional), Message.Content.CARD_RESPONSE);
    }

    @Override
    public void chooseEffect(List<Effect> effects, boolean optional) {
        System.out.println("the effects are:");
        for(Effect e : effects){
            System.out.println(e.getName());
        }
        showChoices(effects, "effects");
        mainClient.sendSelectedItem(randomChoice(effects, optional), Message.Content.EFFECT_RESPOSNSE);
    }

    @Override
    public void choosePlayer(List<Player> players, String info) {
        showChoices(players, "'" +info + "'");
        mainClient.sendSelectedItem(randomChoice(players), Message.Content.PLAYER_RESPONSE);
    }

    @Override
    public void chooseDirection(List<Square.Direction> directions, String info) {
        showChoices(directions, "'" +info + "'");
        mainClient.sendSelectedItem(randomChoice(directions), Message.Content.DIRECTION_RESPONSE);
    }

    @Override
    public void chooseAmmoColor(List<AmmoColor> ammoColors) {
        showChoices(ammoColors, "AmmoColor");
        mainClient.sendSelectedItem(randomChoice(ammoColors), Message.Content.AMMO_RESPONSE);
    }

    @Override
    public void chooseRoom(List<Color> rooms) {
        showChoices(rooms, "rooms");
        mainClient.sendSelectedItem(randomChoice(rooms), Message.Content.ROOM_RESPONSE);
    }


    public void choosePowerup(List<Powerup> powerups, boolean optional, String info){
        showChoices(powerups, "'" +info + "'");
        mainClient.sendSelectedItem(randomChoice(powerups, optional), Message.Content.CARD_RESPONSE);
    }


    @Override
    public void chooseAction(List<ActionOption> actions) {
        showChoices(actions, "actions");
        mainClient.sendSelectedBoard(randomChoice(actions));
    }

    @Override
    public void chooseSquare(List<Square> squares, String info) {
        showChoices(squares, "'" +info + "'");
        mainClient.sendSelectedItem(randomChoice(squares), Message.Content.SQUARE_RESPONSE);
    }

    @Override
    public void showAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks) {
        printer.printAttack(attacker, hp, marks, mainClient.getUsername());
    }

    public void timeout(){
        //automatically reconnect with bot
        //shouldn't happen
        printer.println(mainClient.getUsername() + " got timeout problem");
        mainClient.sendCredentials();
    }

    @Override
    public void updateAll(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard) {
        //dont care for bot
        this.board = board;
        this.myPowerups = myPowerups;
        this.skullBoard = skullBoard;
        //printer.printAllInfo(board, myPowerups, skullBoard, mainClient.getUsername());
    }

    @Override
    public void showScore(List<Player> score) {

        printer.printAllInfo(board, myPowerups, skullBoard, mainClient.getUsername());
        printer.printScore(score);
    }

    @Override
    public void disconnection(){
        printer.println(mainClient.getUsername() + " disconnected.. reconnecting");
        mainClient.sendCredentials();
        //System.exit(-1);
    }

    private <T> int randomChoice(List<T> choices){
        int random = new Random().nextInt(choices.size());
        printer.println("I chose " + (random + 1));
        return random;
    }

    private <T> int randomWithReject(List<T> choices){
        // same chance of not choosing
        int rand = new Random().nextInt(choices.size() + 1);
        if(rand == choices.size()){
            printer.println("I didn't choose this time");
            return -1;
        }
        else {
            return rand;
        }
    }

    private <T> int randomChoice(List<T> choices, boolean optional){
        if(optional){
            return randomWithReject(choices);
        }
        else {
            return randomChoice(choices);
        }
    }

    private <T> void showChoices(List<T> choices, String type){
        printer.println("Choosing for " + type + " between 1 and " + choices.size());

    }

}

