package view.CLI;

import model.board.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

import model.card.AmmoColor;
import model.card.Effect;
import model.card.Powerup;
import model.card.Weapon;
import model.player.Player;
import org.fusesource.jansi.AnsiConsole;

public class Printer {

    private Thread thread;
    private int choice;
    private String decision;
    private CLI cli;
    private static final String AMMO = "O";
    private static final String MARK = "@";
    private static final String DROP = "X";
    private static final String SKULL = "$";

    //makes the text white
    private static final String RESET = "\u001B[0m";

    //private static final Logger logger = Logger.getLogger(Printer.class.getName());

    public Printer(CLI cli){
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
        this.cli = cli;
    }


    void displayRequest(List<String> possibilities, Consumer<Integer> selection, String info){
        //bofore asking something else cancel the previous request
        if(thread != null){
            closeRequest();
        }
        thread = new Thread( () ->
        {
            println(colorToAnsi(AmmoColor.RED) + info + RESET);
            int nRequests = possibilities.size();
            for(int i = 0; i < nRequests; i ++){
                println((i + 1) + ") " + possibilities.get(i));
            }

            //println("THREAD IS  " + thread.getName());
            println("Choose an option between 1 and " + nRequests  + ":");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Scanner reader = new Scanner(bufferedReader);

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
                    }
                }while (choice < 1 || choice > nRequests);
                selection.accept(choice - 1);

            }catch (IOException|InterruptedException e) {
                //dont println anything for cli
            }

        });
        thread.start();
    }

    private void displaySquares(List<String> possibilities, List<Integer> ids, Consumer<Integer> selection, String info){
        //bofore asking something else cancel the previous request
        if(thread != null){
            closeRequest();
        }
        thread = new Thread( () ->
        {

            println(colorToAnsi(AmmoColor.RED) + info + RESET);
            for(String temp : possibilities){
                println(temp);
            }
            //println("THREAD IS  " + thread.getName());
            println("Choose a square: ");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Scanner reader = new Scanner(bufferedReader);

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

                    if(!ids.contains(choice - 1)){
                        println("Write a valid input:");
                    }
                }while (!ids.contains(choice - 1));
                selection.accept(choice - 1);

            }catch (IOException|InterruptedException e) {
                //dont println anything for cli
            }

        });
        thread.start();
    }

    void askPowerup(List<Powerup> powerups, Consumer<Integer> selection, boolean optional, String info){

        List<String> printable = new ArrayList<>();
        for(Powerup powerup : powerups){
            String temp =  colorToAnsi(powerup.getAmmo()) + powerup.getName() + RESET;
            printable.add(temp);
        }
        if(optional){
            printable.add("Dont choose any powerup");
        }

        displayRequest(printable, selection, info);
    }

    void askWeapon(List<Weapon> weapons, Consumer<Integer> selection, boolean optional, String info){
        List<String> printable = new ArrayList<>();
        for(Weapon weapon : weapons){
            StringBuilder charge = new StringBuilder();
            StringBuilder buy = new StringBuilder();

            for(int n = 0; n < weapon.getBuyCost().size(); n++){
                buy.append(colorToAnsi(weapon.getBuyCost().get(n))).append(AMMO);
            }

            for(int n = 0; n < weapon.getChargeCost().size(); n++){
                charge.append(colorToAnsi(weapon.getChargeCost().get(n))).append(AMMO);
            }
            String temp = (weapon.getCharged() ? colorToAnsi(Color.GREEN) : colorToAnsi(Color.RED))
                    + weapon.getName() + RESET
                    + " buy cost: " + buy.toString() + RESET
                    + " charge cost: " + charge.toString() + RESET;

            printable.add(temp);
        }
        if(optional){
            printable.add("Dont choose any weapon");
        }

        displayRequest(printable, selection, info);
    }

    void askSquare(List<Square> squares, Consumer<Integer> selection, String info){
        List<String> printable = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for(Square square : squares){
            String temp = "Move to square " + colorToAnsi(square.getColor()) + (square.getId() + 1) + RESET;
            printable.add(temp);
            ids.add(square.getId());
        }

        displaySquares(printable, ids, id ->
        {
            Square tempS = null;
            for(Square s : squares){
                if(s.getId() == id){
                    tempS = s;
                    break;
                }
            }
            selection.accept(squares.indexOf(tempS));
        }, info);
    }

    void askEffect(List<Effect> effects, Consumer<Integer> selection, boolean optional){
        List<String> printable = new ArrayList<>();
        for(Effect effect : effects){
            StringBuilder temp = new StringBuilder();
            temp.append(RESET).append("Effect: ").append(effect.getDescription());
            temp.append("Extra cost: ");
            for(AmmoColor color : effect.getExtraCost()){
                temp.append(colorToAnsi(color)).append(AMMO).append(RESET);
            }

            printable.add(temp.toString());
        }

        if(optional){
            printable.add("Dont choose effect");
        }

        displayRequest(printable, selection, "How do you want to attack your enemies?");
    }

    void askPlayer(List<Player> players, Consumer<Integer> selection, String info){
        List<String> printable = new ArrayList<>();
        for(Player player : players){
            String temp = RESET + "Attack "
                    + colorToAnsi(player.getColor()) + player.getNickname() + RESET;
            printable.add(temp);
        }

        displayRequest(printable, selection, info);
    }

    void askAmmoColor(List<AmmoColor> ammoColors, Consumer<Integer> selection)   {
        List<String> printable = new ArrayList<>();
        for(AmmoColor ammo : ammoColors){
            String temp = RESET + "Pay with: " + colorToAnsi(ammo) + AMMO + RESET;
            printable.add(temp);
        }

        displayRequest(printable, selection, "What color do you want to use to pay TARGETING SCOPE?");
    }

    void askRoom(List<Color> rooms, Consumer<Integer> selection){
        List<String> printable = new ArrayList<>();
        for(Color room : rooms){
            String temp = colorToAnsi(room) + room.toString() + RESET + " room";
            printable.add(temp);
        }

        displayRequest(printable, selection, "In which room do you want to fire?");
    }

    void askDirection(List<Square.Direction> directions, Consumer<Integer> selection, String info){
        List<String> printable = new ArrayList<>();
        for(Square.Direction direction : directions){
            String temp = RESET + "Shoot " + direction.toString();
            printable.add(temp);
        }

        displayRequest(printable, selection, info);
    }

    void askReconnect(Consumer<Boolean> reconnect){
        if(thread != null){
            closeRequest();
        }
        println(colorToAnsi(Color.RED) + "TIME IS FINISHED" + RESET);
        println(RESET + "Want to reconnect? [Y/N]");

        thread = new Thread( () ->
        {
            //println("THREAD IS  " + thread.getName());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Scanner reader = new Scanner(bufferedReader);

            try {

                boolean cont = false;
                boolean yesNo = false;
                do {
                    while (!bufferedReader.ready()) {

                        Thread.sleep(100);
                    }

                    if(reader.hasNextLine()){
                        decision = reader.nextLine().toLowerCase();
                        if(decision.equals("y")){
                            yesNo = true;
                            cont = false;
                        }
                        else if(decision.equals("n")){
                            cont = false;
                        }
                        else{
                            cont = true;
                        }
                    }


                    //yesNo = decision.toLowerCase().substring(0, 1);
                    //cont = yesNo.equals("y") || yesNo.equals("n");

                    if(cont){
                        println("Write a valid input: ");
                    }
                }while (cont);


                reconnect.accept(yesNo);

            }catch (IOException|InterruptedException e) {
                //dont println anything for cli
            }

        });
        thread.start();
    }

    void closeRequest(){
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
    void print(String toPrint){
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
                                                line.append(colorToAnsi(((AmmoSquare) tempSquare).getAmmoCard().getAmmoList().get(n))).append(AMMO);
                                            }
                                            line.append(RESET).append("?");
                                            line.append(colorToAnsi(tempSquare.getColor())).append(" |");
                                        } else {
                                            line.append(colorToAnsi(tempSquare.getColor())).append("| ");
                                            for (int n = 0; n < 3; n++) {
                                                line.append(colorToAnsi(((AmmoSquare) tempSquare).getAmmoCard().getAmmoList().get(n))).append(AMMO);
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
                                line.append(RESET).append(tempSquare.getId() + 1);
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
                line.append(RESET);
                //println(line.toString());
                allLines.add(line.toString());
                //line.setLength(0);
            }
            tempS = square;

        }
        return allLines;
    }

    List<String> printWeaponsOnBoard(GameBoard board){

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

                line.append(RESET).append(String.format("%-17s", weapon.getName()));



                if(!weapon.getBuyCost().isEmpty()){
                    line.append(RESET).append(" Buy: ");
                    for(int n = 0; n < weapon.getBuyCost().size(); n++){
                        line.append(colorToAnsi(weapon.getBuyCost().get(n))).append(AMMO);
                    }
                    for(int n = weapon.getBuyCost().size(); n < 3; n++){
                        line.append(" ");
                    }
                }
                else{
                    line.append(RESET).append(" Buy:    ");
                }

                line.append(RESET).append(" Charge: ");
                for(int n = 0; n < weapon.getChargeCost().size(); n++){
                    line.append(colorToAnsi(weapon.getChargeCost().get(n))).append(AMMO);
                }

                for(int n = weapon.getChargeCost().size(); n < 3; n++){
                    line.append(" ");
                }
                line.append(RESET);
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
                    playerInfo.append(RESET).append("(YOU) ");
                }

                //add the ammo
                for(AmmoColor ammo : player.getAmmo().keySet()){
                    for(int i = 0; i < player.getAmmo().get(ammo); i++){
                        playerInfo.append(colorToAnsi(ammo)).append(AMMO);
                    }
                }

                playerInfo.append("   ");

                //add points
                for(Color hp : player.getPlayerBoard().getHpColor()){
                    playerInfo.append(colorToAnsi(hp)).append(DROP);
                }
                playerInfo.append(RESET).append("   ");

                //add marks

                for(Color marks : player.getPlayerBoard().getMarksColor()){
                    playerInfo.append(colorToAnsi(marks)).append(MARK);
                }

                //dead times
                int dead = player.getPlayerBoard().getDeathTimes();
                if(dead > 0){
                    playerInfo.append(colorToAnsi(AmmoColor.RED))
                            .append("DIED ").append(dead).append(dead > 1 ? " TIMES" : " TIME");
                }

                //add points
                int points = player.getPlayerBoard().getPoints();
                playerInfo.append(RESET)
                        .append("  POINTS: ").append(colorToAnsi(Color.GREEN)).append(points);


                playerInfo.append(RESET);
                stringedInfo.add(playerInfo.toString());

                //powerups
                String delimiter = ""; //trick
                if(player.getNickname().equals(cli.getMainClient().getUsername())){
                    stringedPowerups.append(RESET).append("Powerups: ");
                    for(Powerup powerup : myPowerups){
                        stringedPowerups.append(RESET).append(delimiter);
                        delimiter = ", ";
                        stringedPowerups.append(colorToAnsi(powerup.getAmmo())).append(powerup.getName());
                    }
                    stringedInfo.add(stringedPowerups.toString());
                }

                //weapons
                stringedWeapons.append(RESET).append("Weapons: ");
                delimiter = ""; //trick
                for(Weapon weapon : player.getWeapons()){
                    stringedWeapons.append(RESET).append(delimiter);
                    delimiter = ", ";
                    stringedWeapons.append(weapon.getCharged() ? colorToAnsi(Color.GREEN) : colorToAnsi(Color.RED));
                    stringedWeapons.append(weapon.getName());
                }

                stringedInfo.add(stringedWeapons.toString());

            }

        }
        return stringedInfo;
    }

    List<String> printSkullBoard(SkullBoard skullBoard){
        StringBuilder raw1 = new StringBuilder();
        StringBuilder raw2 = new StringBuilder();
        List<String> stringed = new ArrayList<>();

        int nSkulls = skullBoard.getInitSkulls();

        for(int i = 0; i < nSkulls ; i++){
            raw1.append("+---");
            raw2.append("| ");
            StringBuilder drops = new StringBuilder();

            if(i < skullBoard.getCells().size()){
                for(int j = 0; j < skullBoard.getCells().get(i).getPoint(); j++){
                    drops.append(colorToAnsi(skullBoard.getCells().get(i).getKillColor())).append(SKULL);
                }
                for(int k = skullBoard.getCells().get(i).getPoint(); k < 2; k++){
                    drops.append(" ");
                }
            }
            else {
                drops.append(" ");
            }

            raw2.append(RESET).append(String.format("%2s", drops.toString()))
                    .append(RESET);

        }
        raw2.append(RESET).append("| ");
        raw1.append("+");
        if(nSkulls < skullBoard.getCells().size()){
            raw1.append("-");
            for(int k = nSkulls; k < skullBoard.getCells().size(); k++){
                for (int w = 0; w < skullBoard.getCells().get(k).getPoint(); w++){
                    raw1.append("-");
                    raw2.append(colorToAnsi(skullBoard.getCells().get(k).getKillColor()))
                            .append(SKULL);
                }
            }
            raw1.append("-+");
            raw2.append(RESET).append(" |");
        }



        stringed.add(raw1.toString());
        stringed.add(raw2.toString());
        stringed.add(raw1.toString());

        return stringed;
    }

    public void printAllInfo(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard){

        //dont know, should clear the screen
        print("\u001b[2J");

        List<String> stringedBoard = printBoard(board);
        List<String> stringedWeapons = printWeaponsOnBoard(board);
        List<String> stringedPlayers = printPlayersInfo(board, myPowerups);
        List<String> stringedSkullBoard = printSkullBoard(skullBoard);

        int nWeapons = stringedWeapons.size();

        String header = String.format("%19s %10s %22s %10s %10s %13s",
                "GAME BOARD","|", "WEAPONS ON GAME BOARD", "COST", "|", "PLAYERS");

        println(header);

        int j = 0;
        int k = 0;
        int w = -2;
        boolean skullB = false;
        for(int i = 0; i < stringedBoard.size(); i++){
            StringBuilder line = new StringBuilder();
            line.append(stringedBoard.get(i));

            if(j < stringedWeapons.size()){
                line.append(" | ").append(stringedWeapons.get(j));
                j++;
            }

            else if(skullB){
                if(w == -1){
                    line.append(" | ").append(String.format("%20s","SKULL BOARD"));
                    w++;
                }
                else if (w < -1){
                    line.append(String.format(" %45s", " "));
                    w++;
                }
                else {
                    line.append(" |  ").append(stringedSkullBoard.get(w));
                    w++;
                }

            }
            else{
                //add extra space when there are less weapons
                if(nWeapons < 9){
                    line.append(String.format(" %45s", " "));
                    nWeapons++;
                }
                else {
                    line.append(String.format(" %45s", " "));
                    skullB = true;
                }
            }


            if(k < stringedPlayers.size()){
                line.append(" | ").append(stringedPlayers.get(k));
                k++;
            }

            println(line.toString());
        }
        println("\n");

    }

    public void printAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks){
        StringBuilder attack = new StringBuilder();

        attack.append(colorToAnsi(attacker.getColor()));
        if(attacker.getNickname().equals(cli.getMainClient().getUsername())){
            attack.append("YOU");
        }
        else{
            attack.append(attacker.getNickname());
        }
        attack.append(colorToAnsi(AmmoColor.RED)).append(" attacked:\n");
        hp.forEach((x, y) -> {
            attack.append(colorToAnsi(x.getColor())).append(x.getNickname())
                    .append(RESET).append(" giving ");
            for(int i = 0; i < y; i ++){
                attack.append(colorToAnsi(attacker.getColor())).append(DROP);
            }

            attack.append(RESET).append("\n");
        });

        marks.forEach((x, y) -> {
            attack.append(colorToAnsi(x.getColor())).append(x.getNickname())
                    .append(RESET).append(" giving ");
            for(int i = 0; i < y; i ++){
                attack.append(colorToAnsi(attacker.getColor())).append(MARK);
            }
            attack.append(RESET).append("\n");
        });

        print(attack.toString());

    }

    public void printScore(List<Player> score) {
        if(thread != null){
            closeRequest();
        }
        Player winner = score.stream().findFirst().get();
        List<String> lines = new ArrayList<>();
        score.forEach(x -> lines.add(colorToAnsi(x.getColor())+ x.getNickname()
                + RESET + " points: " + x.getPlayerBoard().getPoints()));

        println( colorToAnsi(winner.getColor()) + winner.getNickname()
                + colorToAnsi(AmmoColor.RED) + "  WON" + RESET);
        int i = 1;
        for(String line : lines){
            println(i + " - " + line);
            i++;
        }
    }

    String colorToAnsi(AmmoColor color){
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

    String colorToAnsi(Color color){
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
                return "\u001B[0;37m"; //white-gray
        }
    }

}
