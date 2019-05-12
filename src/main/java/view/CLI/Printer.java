package view.CLI;

import model.board.AmmoSquare;
import model.board.Color;
import model.board.GameBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

import model.board.Square;
import model.card.AmmoColor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Printer {

    private Thread thread;
    private int choice;

    private static final Logger logger = Logger.getLogger(Printer.class.getName());

    public Printer(){
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
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

            println("THRED IS  " + thread.getName());
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
            println("You finished your time for choosing");
            println("KILLING THREAD  " + thread.getName());
        }

    }

    public void println(String toPrint){
        AnsiConsole.out.println(toPrint);
    }
    public void print(String toPrint){
        AnsiConsole.out.print(toPrint);
    }

    public void printBoard(GameBoard board){
        closeRequest();
        println("chosen board is number "+ board.getId() + " " + board.getDescription());

        int square = 0, tempS = 0;

        //maximum cells in high is 3
        for(int y = 0; y < 3; y++){
            //maximum horizontal cells is 4
            for(int z = 0; z < 5; z++){
                square = tempS;
                for(int x = 0; x < 4; x++){
                    Square tempSquare = board.getSquare(square);
                    if(tempSquare.getX() == x && tempSquare.getY() == y){
                        switch (z){
                            case 0:
                                if (board.getSquare(x, y - 1) == null){
                                    print(colorToAnsi(tempSquare.getColor()) + "+-----+");
                                }
                                else {
                                    if (board.getSquare(x, y - 1).getColor() == tempSquare.getColor()){
                                        print(colorToAnsi(tempSquare.getColor()) + "+-   -+");
                                    }
                                    else {
                                        final int xx = x, yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx, yy - 1).getId())){
                                            print(colorToAnsi(tempSquare.getColor()) + "+|   |+");
                                        }
                                        else {
                                            print(colorToAnsi(tempSquare.getColor()) + "+-----+");
                                        }

                                    }
                                }

                                break;
                            case 1:
                                //first check if generation point
                                if(tempSquare.isGenerationPoint()){
                                    print(colorToAnsi(tempSquare.getColor()) + "| GEN |");
                                }
                                else {
                                    if(((AmmoSquare) tempSquare).getAmmoCard().hasPowerup()){
                                        print(colorToAnsi(tempSquare.getColor()) + "| ");
                                        for(int n = 0; n < 2; n++){
                                            print(colorToAnsi(((AmmoSquare) tempSquare).getAmmoCard().getAmmoList().get(n)) + "O");
                                        }
                                        print(colorToAnsi(Color.WHITE) + "?");
                                        print(colorToAnsi(tempSquare.getColor()) + " |");
                                    }
                                    else {
                                        print(colorToAnsi(tempSquare.getColor()) + "| ");
                                        for(int n = 0; n < 3; n++){
                                            print(colorToAnsi(((AmmoSquare) tempSquare).getAmmoCard().getAmmoList().get(n)) + "O");
                                        }
                                        print(colorToAnsi(tempSquare.getColor()) + " |");
                                    }
                                }
                                break;
                            case 2:
                                if (board.getSquare(x - 1, y ) == null){
                                    print(colorToAnsi(tempSquare.getColor()) + "| ");
                                }
                                else {
                                    if (board.getSquare(x - 1, y).getColor() == tempSquare.getColor()){
                                        print(colorToAnsi(tempSquare.getColor()) + "  ");
                                    }
                                    else {
                                        final int xx = x, yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx - 1, yy).getId())){
                                            print(colorToAnsi(tempSquare.getColor()) + "= ");
                                        }
                                        else {
                                            print(colorToAnsi(tempSquare.getColor()) + "| ");
                                        }

                                    }
                                }

                                for(int p = 0; p < 3; p++){
                                    if (p < tempSquare.getPlayersOnSquare().size()){
                                        print(colorToAnsi(tempSquare.getPlayersOnSquare().get(p).getColor()) + "" + (p+1));
                                    }
                                    else {
                                        print(colorToAnsi(tempSquare.getColor()) + " ");
                                    }
                                }

                                if (board.getSquare(x + 1, y ) == null){
                                    print(colorToAnsi(tempSquare.getColor()) + " |");
                                }
                                else {
                                    if (board.getSquare(x + 1, y).getColor() == tempSquare.getColor()){
                                        print(colorToAnsi(tempSquare.getColor()) + "  ");
                                    }
                                    else {
                                        final int xx = x, yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx + 1, yy).getId())){
                                            print(colorToAnsi(tempSquare.getColor()) + " =");
                                        }
                                        else {
                                            print(colorToAnsi(tempSquare.getColor()) + " |");
                                        }

                                    }
                                }


                                break;
                            case 3:
                                print(colorToAnsi(tempSquare.getColor()) + "| ");
                                for(int p = 3; p < 5; p++){
                                    if (p < tempSquare.getPlayersOnSquare().size()){
                                        print(colorToAnsi(tempSquare.getPlayersOnSquare().get(p).getColor()) + "" + (p+1));
                                    }
                                    else {
                                        print(colorToAnsi(tempSquare.getColor()) + " ");
                                    }
                                }
                                print(colorToAnsi(Color.WHITE) + (tempSquare.getId() + 1));
                                if(tempSquare.getId() + 1 < 10){
                                    print(colorToAnsi(tempSquare.getColor()) + "" + " |");
                                }
                                else{
                                    print(colorToAnsi(tempSquare.getColor()) + "" + "|");
                                }

                                break;
                            case 4:
                                if (board.getSquare(x, y + 1) == null){
                                    print(colorToAnsi(tempSquare.getColor()) + "+-----+");
                                }
                                else {
                                    if (board.getSquare(x, y + 1).getColor() == tempSquare.getColor()){
                                        print(colorToAnsi(tempSquare.getColor()) + "+-   -+");
                                    }
                                    else {
                                        final int xx = x, yy = y;
                                        if(tempSquare.getNeighbourId().stream().anyMatch(k -> k == board.getSquare(xx, yy + 1).getId())){
                                            print(colorToAnsi(tempSquare.getColor()) + "+|   |+");
                                        }
                                        else {
                                            print(colorToAnsi(tempSquare.getColor()) + "+-----+");
                                        }

                                    }
                                }
                                break;
                        }


                        square++;
                    }
                    else{
                        print("       ");
                    }

                }
                println("");
            }
            tempS = square;
        }







    }


    private String colorToAnsi(AmmoColor color){
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
    private String colorToAnsi(Color color){
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
