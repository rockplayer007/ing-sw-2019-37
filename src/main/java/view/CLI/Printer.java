package view.CLI;

import model.board.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

import model.card.AmmoColor;
import model.card.Effect;
import model.card.Powerup;
import model.card.Weapon;
import model.player.Player;
import org.fusesource.jansi.AnsiConsole;

public class Printer {

    private Thread thread;
    private int choice;
    private CLI cli;

    //private static final Logger logger = Logger.getLogger(Printer.class.getName());

    public Printer(CLI cli){
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
        this.cli = cli;
    }


    public void displayRequest(List<String> possibilities, Consumer<Integer> selection){
        //bofore asking something else cancel the previous request
        if(thread != null){
            closeRequest();
        }
        thread = new Thread( () ->
        {
            int nRequests = possibilities.size();
            for(int i = 0; i < nRequests; i ++){
                println((i + 1) + ") " + possibilities.get(i));
            }

            //println("THREAD IS  " + thread.getName());
            println("Choose an option between 1 and " + nRequests  + ":");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Scanner reader = new Scanner(bufferedReader);
            ///int choice;
            String tmp;
            try {
                do {
                    while (!bufferedReader.ready()) {

                        Thread.sleep(100);
                    }
                    if (reader.hasNextInt()) {
                        choice = reader.nextInt();
                    } else {
                        reader.next();
                        choice = -1;
                    }

                    if(choice < 1 || choice > nRequests){
                        println("Write a valid input:");
                        println("to THREAD   " + thread.getName());
                    }
                }while (choice < 1 || choice > nRequests);
                selection.accept(choice - 1);

            }catch (IOException|InterruptedException e) {
                //dont println anything for cli
            }

        });
        thread.start();
    }

    public void askPowerup(List<Powerup> powerups, Consumer<Integer> selection, boolean optional){
        List<String> printable = new ArrayList<>();
        for(Powerup powerup : powerups){
            String temp = powerup.getName() + colorToAnsi(powerup.getAmmo()) + " O" + colorToAnsi(Color.WHITE);
            printable.add(temp);
        }
        if(optional){
            printable.add("Dont use any powerup");
        }

        displayRequest(printable, selection);
    }

    public void askWeapon(List<Weapon> weapons, Consumer<Integer> selection, boolean optional){
        List<String> printable = new ArrayList<>();
        for(Weapon weapon : weapons){
            StringBuilder charge = new StringBuilder();
            StringBuilder buy = new StringBuilder();

            for(int n = 0; n < weapon.getBuyCost().size(); n++){
                buy.append(colorToAnsi(weapon.getBuyCost().get(n))).append("O");
            }

            for(int n = 0; n < weapon.getChargeCost().size(); n++){
                charge.append(colorToAnsi(weapon.getChargeCost().get(n))).append("O");
            }
            String temp = colorToAnsi(Color.WHITE) + weapon.getName()
                    + " buy cost: " + buy.toString() + colorToAnsi(Color.WHITE)
                    + " charge cost: " + charge.toString() + colorToAnsi(Color.WHITE);

            printable.add(temp);
        }
        if(optional){
            printable.add("Dont use any weapon");
        }

        displayRequest(printable, selection);
    }

    public void askSquare(List<Square> squares, Consumer<Integer> selection){
        List<String> printable = new ArrayList<>();
        for(Square square : squares){
            String temp = "Move to square " + colorToAnsi(square.getColor()) + (square.getId() + 1) + colorToAnsi(Color.WHITE);
            printable.add(temp);
        }

        displayRequest(printable, selection);
    }

    public void askEffect(List<Effect> effects, Consumer<Integer> selection){
        List<String> printable = new ArrayList<>();
        for(Effect effect : effects){
            StringBuilder temp = new StringBuilder();
            temp.append(colorToAnsi(Color.WHITE)).append("Effect: ").append(effect.getDescription());
            temp.append("Extra cost: ");
            for(AmmoColor color : effect.getExtraCost()){
                temp.append(colorToAnsi(color)).append("O");
            }

            printable.add(temp.toString());
        }

        displayRequest(printable, selection);
    }

    public void askPlayer(List<Player> players, Consumer<Integer> selection){
        List<String> printable = new ArrayList<>();
        for(Player player : players){
            String temp = colorToAnsi(Color.WHITE) + "Attack "
                    + colorToAnsi(player.getColor()) + player.getNickname() + colorToAnsi(Color.WHITE);
            printable.add(temp);
        }

        displayRequest(printable, selection);
    }

    public void askAmmoColor(List<AmmoColor> ammoColors, Consumer<Integer> selection)   {
        List<String> printable = new ArrayList<>();
        for(AmmoColor ammo : ammoColors){
            String temp = colorToAnsi(Color.WHITE) + "Pay with: " + colorToAnsi(ammo) + "O" + colorToAnsi(Color.WHITE);
            printable.add(temp);
        }

        displayRequest(printable, selection);
    }

    public void askRoom(List<Color> rooms, Consumer<Integer> selection){
        List<String> printable = new ArrayList<>();
        for(Color room : rooms){
            String temp = colorToAnsi(room) + room.toString() + colorToAnsi(Color.WHITE) + "room";
            printable.add(temp);
        }

        displayRequest(printable, selection);
    }

    public void askDirection(List<Square.Direction> directions, Consumer<Integer> selection){
        List<String> printable = new ArrayList<>();
        for(Square.Direction direction : directions){
            String temp = colorToAnsi(Color.WHITE) + "Shoot " + direction.toString();
            printable.add(temp);
        }

        displayRequest(printable, selection);
    }


    public void closeRequest(){
        if(thread != null && !thread.isInterrupted()){
            BufferedReader tmp = new BufferedReader(new InputStreamReader(System.in));
            try {
                if (tmp.ready()) {
                    tmp.readLine();
                }
            } catch (IOException e) {
                //nothing to println for cli
            }
            thread.interrupt();
            //println("KILLING THREAD  " + thread.getName());
        }

    }

    public void println(String toPrint){
        AnsiConsole.out.println(toPrint);
    }
    public void print(String toPrint){
        AnsiConsole.out.print(toPrint);
    }

    public List<String> printBoard(GameBoard board){
        closeRequest();
        //println("chosen board is number "+ board.getId() + " " + board.getDescription());

        List<String> allLines = new ArrayList<>();

        int square = 0, tempS = 0;
        int playerCnt = 0;

        //maximum cells in high is 3
        for(int y = 0; y < 3; y++){
            //maximum horizontal cells is 4
            for(int z = 0; z < 5; z++){
                square = tempS;
                //one line
                StringBuilder line = new StringBuilder();
                for(int x = 0; x < 4; x++){

                    Square tempSquare = board.getSquare(square);
                    if(tempSquare.getX() == x && tempSquare.getY() == y){
                        switch (z){
                            case 0:
                                if (board.getSquare(x, y - 1) == null){
                                    line.append(colorToAnsi(tempSquare.getColor())).append("+-----+");
                                }
                                else {
                                    if (board.getSquare(x, y - 1).getColor() == tempSquare.getColor()){
                                        line.append(colorToAnsi(tempSquare.getColor())).append("+-   -+");
                                    }
                                    else {
                                        final int xx = x, yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx, yy - 1).getId())){
                                            line.append(colorToAnsi(tempSquare.getColor())).append("+|   |+");
                                        }
                                        else {
                                            line.append(colorToAnsi(tempSquare.getColor())).append("+-----+");
                                        }

                                    }
                                }

                                break;
                            case 1:
                                //first check if generation point
                                if(tempSquare.isGenerationPoint()){
                                    line.append(colorToAnsi(tempSquare.getColor())).append("| GEN |");
                                }
                                else {
                                    if(((AmmoSquare) tempSquare).getAmmoCard() != null) {
                                        if (((AmmoSquare) tempSquare).getAmmoCard().hasPowerup()) {
                                            line.append(colorToAnsi(tempSquare.getColor())).append("| ");
                                            for (int n = 0; n < 2; n++) {
                                                line.append(colorToAnsi(((AmmoSquare) tempSquare).getAmmoCard().getAmmoList().get(n))).append("O");
                                            }
                                            line.append(colorToAnsi(Color.WHITE)).append("?");
                                            line.append(colorToAnsi(tempSquare.getColor())).append(" |");
                                        } else {
                                            line.append(colorToAnsi(tempSquare.getColor())).append("| ");
                                            for (int n = 0; n < 3; n++) {
                                                line.append(colorToAnsi(((AmmoSquare) tempSquare).getAmmoCard().getAmmoList().get(n))).append("O");
                                            }
                                            line.append(colorToAnsi(tempSquare.getColor())).append(" |");
                                        }
                                    }
                                    else{
                                        line.append(colorToAnsi(tempSquare.getColor())).append("|     |");
                                    }
                                }
                                break;
                            case 2:
                                if (board.getSquare(x - 1, y ) == null){
                                    line.append(colorToAnsi(tempSquare.getColor())).append("| ");
                                }
                                else {
                                    if (board.getSquare(x - 1, y).getColor() == tempSquare.getColor()){
                                        line.append(colorToAnsi(tempSquare.getColor())).append("  ");
                                    }
                                    else {
                                        final int xx = x;
                                        final int yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx - 1, yy).getId())){
                                            line.append(colorToAnsi(tempSquare.getColor())).append("  ");
                                        }
                                        else {
                                            line.append(colorToAnsi(tempSquare.getColor())).append("| ");
                                        }

                                    }
                                }

                                for(int p = 0; p < 3; p++){
                                    if (p < tempSquare.getPlayersOnSquare().size()){
                                        line.append(colorToAnsi(tempSquare.getPlayersOnSquare().get(p).getColor())).append(++playerCnt);

                                    }
                                    else {
                                        line.append(colorToAnsi(tempSquare.getColor())).append(" ");
                                    }
                                }

                                if (board.getSquare(x + 1, y ) == null){
                                    line.append(colorToAnsi(tempSquare.getColor())).append(" |");
                                }
                                else {
                                    if (board.getSquare(x + 1, y).getColor() == tempSquare.getColor()){
                                        line.append(colorToAnsi(tempSquare.getColor())).append("  ");
                                    }
                                    else {
                                        final int xx = x, yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx + 1, yy).getId())){
                                            line.append(colorToAnsi(tempSquare.getColor())).append("  ");
                                        }
                                        else {
                                            line.append(colorToAnsi(tempSquare.getColor())).append(" |");
                                        }

                                    }
                                }


                                break;
                            case 3:
                                line.append(colorToAnsi(tempSquare.getColor())).append("| ");
                                for(int p = 3; p < 5; p++){
                                    if (p < tempSquare.getPlayersOnSquare().size()){
                                        line.append(colorToAnsi(tempSquare.getPlayersOnSquare().get(p).getColor())).append(playerCnt++);
                                    }
                                    else {
                                        line.append(colorToAnsi(tempSquare.getColor())).append(" ");
                                    }
                                }
                                line.append(colorToAnsi(Color.WHITE)).append(tempSquare.getId() + 1);
                                if(tempSquare.getId() + 1 < 10){
                                    line.append(colorToAnsi(tempSquare.getColor())).append(" |");
                                }
                                else{
                                    line.append(colorToAnsi(tempSquare.getColor())).append("|");
                                }

                                break;
                            case 4:
                                if (board.getSquare(x, y + 1) == null){
                                    line.append(colorToAnsi(tempSquare.getColor())).append("+-----+");
                                }
                                else {
                                    if (board.getSquare(x, y + 1).getColor() == tempSquare.getColor()){
                                        line.append(colorToAnsi(tempSquare.getColor())).append("+-   -+");
                                    }
                                    else {
                                        final int xx = x;
                                        final int yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx, yy + 1).getId())){
                                            line.append(colorToAnsi(tempSquare.getColor())).append("+|   |+");
                                        }
                                        else {
                                            line.append(colorToAnsi(tempSquare.getColor())).append("+-----+");
                                        }

                                    }
                                }
                                break;
                        }

                        square++;
                    }
                    else{
                        line.append("       ");
                        //print("       ");
                    }


                }
                line.append(colorToAnsi(Color.WHITE));
                //println(line.toString());
                allLines.add(line.toString());
                //line.setLength(0);
            }
            tempS = square;

        }
        return allLines;
    }

    public List<String> printWeaponsOnBoard(GameBoard board){

        List<String> allLines = new ArrayList<>();

        for (int j = 0; j < board.getGenPoints().size(); j++){
            Color color = Color.values()[j];

            for(int i = 0; i < ((GenerationSquare) board.getGenerationPoint(color)).getWeaponDeck().size(); i++){
                StringBuilder line = new StringBuilder();

                Weapon weapon = ((GenerationSquare) board.getGenerationPoint(color)).getWeaponDeck().get(i);
                if(board.getGenerationPoint(color).getId() >= 9){
                    line.append(colorToAnsi(color)).append(board.getGenerationPoint(color).getId() + 1).append(") ");
                }
                else{
                    line.append(colorToAnsi(color)).append(board.getGenerationPoint(color).getId() + 1).append(")  ");
                }

                line.append(colorToAnsi(Color.WHITE)).append(String.format("%-17s", weapon.getName()));



                if(!weapon.getBuyCost().isEmpty()){
                    line.append(colorToAnsi(Color.WHITE)).append(" Buy: ");
                    for(int n = 0; n < weapon.getBuyCost().size(); n++){
                        line.append(colorToAnsi(weapon.getBuyCost().get(n))).append("O");
                    }
                    for(int n = weapon.getBuyCost().size(); n < 3; n++){
                        line.append(" ");
                    }
                }
                else{
                    line.append(colorToAnsi(Color.WHITE)).append(" Buy:    ");
                }

                line.append(colorToAnsi(Color.WHITE)).append(" Charge: ");
                for(int n = 0; n < weapon.getChargeCost().size(); n++){
                    line.append(colorToAnsi(weapon.getChargeCost().get(n))).append("O");
                }

                for(int n = weapon.getChargeCost().size(); n < 3; n++){
                    line.append(" ");
                }
                line.append(colorToAnsi(Color.WHITE));
                allLines.add(line.toString());
            }
        }
        return allLines;
    }

    public List<String> printPlayersInfo(GameBoard board, List<Powerup> myPowerups){
        int playerCnt = 0;

        List<String> stringedInfo = new ArrayList<>();

        //check in all squares where the players are
        for(Square square : board.allSquares()){

            //get the informations about about every player in the square
            for(Player player: square.getPlayersOnSquare()){
                StringBuilder playerInfo = new StringBuilder();
                StringBuilder stringedPowerups = new StringBuilder();
                StringBuilder stringedWeapons = new StringBuilder();

                playerInfo.append(colorToAnsi(player.getColor())).append(++playerCnt).append(") ");
                playerInfo.append(colorToAnsi(player.getColor())).append(player.getNickname()).append(" ");
                if(player.getNickname().equals(cli.getMainClient().getUsername())){
                    playerInfo.append(colorToAnsi(Color.WHITE)).append("(YOU) ");
                }

                //add the ammo
                for(AmmoColor ammo : player.getAmmo().keySet()){
                    for(int i = 0; i < player.getAmmo().get(ammo); i++){
                        playerInfo.append(colorToAnsi(ammo)).append("O");

                    }
                }
                //println(playerInfo.toString());
                stringedInfo.add(playerInfo.toString());

                if(player.getNickname().equals(cli.getMainClient().getUsername())){
                    stringedPowerups.append(colorToAnsi(Color.WHITE)).append("Powerups: ");
                    for(Powerup powerup : myPowerups){
                        stringedPowerups.append(colorToAnsi(Color.WHITE)).append(" ").append(powerup.getName());
                        stringedPowerups.append(colorToAnsi(powerup.getAmmo())).append(" O");
                    }
                    stringedInfo.add(stringedPowerups.toString());
                }

                stringedWeapons.append(colorToAnsi(Color.WHITE)).append("Weapons: ");
                for(Weapon weapon : player.getWeapons()){
                    stringedWeapons.append(weapon.getName());
                    stringedWeapons.append(weapon.getCharged() ? " (CHARGED) " : " (NOT CHARGED) ");
                }
                stringedInfo.add(stringedWeapons.toString());

            }

        }
        return stringedInfo;
    }

    public void printAllInfo(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard){
        List<String> stringedBoard = printBoard(board);
        List<String> stringedWeapons = printWeaponsOnBoard(board);
        List<String> stringedPlayers = printPlayersInfo(board, myPowerups);

        String header = String.format("%19s %12s %22s %12s %12s %13s",
                "GAME BOARD","|", "WEAPONS ON GAME BOARD", "COST", "|", "PLAYERS");

        println(header);

        int j = 0, k = 0;
        for(int i = 0; i < stringedBoard.size(); i++){
            StringBuilder line = new StringBuilder();
            line.append(stringedBoard.get(i));

            if(j < stringedWeapons.size()){
                line.append("   |   ").append(stringedWeapons.get(j));
                j++;
            }

            if(k < stringedPlayers.size()){
                line.append("   |   ").append(stringedPlayers.get(k));
                k++;
            }


            println(line.toString());
        }


    }

    public String colorToAnsi(AmmoColor color){
        switch (color){
            case BLUE:
                return "\u001B[0;34m";
            case RED:
                return "\u001B[0;31m";
            case YELLOW:
                return "\u001B[0;33m";
            default:
                return "\u001B[0;37m"; //white
        }
    }
    public String colorToAnsi(Color color){
        switch (color){
            case BLUE:
                return "\u001B[0;34m";
            case RED:
                return "\u001B[0;31m";
            case YELLOW:
                return "\u001B[0;33m";
            case GREEN:
                return "\u001B[0;32m";
            case PURPLE:
                return "\u001B[0;36m"; //CYAN
            default:
                return "\u001B[0;37m"; //white
        }
    }


}
