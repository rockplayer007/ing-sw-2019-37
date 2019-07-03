package model.board;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.GsonBuilder;

import model.board.*;
import model.card.AmmoColor;
import model.card.Weapon;
import model.player.Heroes;
import model.player.Player;
import network.client.MainClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CLI.CLI;
import view.CLI.Printer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    private GameBoard map;
    private Board board;

    @BeforeEach
    public void createBoard(){
        board = new Board();
        map = new BoardGenerator(board).createMap(3);
        board.setMap(map);


    }
    @Test
    public void boardCreationTest(){

        assertEquals(map.getGenerationPoint(Color.BLUE).getX(), 2);
        assertEquals(map.getGenerationPoint(Color.BLUE).getY(), 0);

    }

    @Test
    public void serializeMapTest(){

        Player player1 = new Player("destructor");
        player1.setHero(Heroes.D_STRUCT_OR);
        Player player2 = new Player("banshee");
        player2.setHero(Heroes.BANSHEE);
        Player player3 = new Player("dozer");
        player3.setHero(Heroes.DOZER);
        Weapon testWeapon1 = ((GenerationSquare) board.getMap().getGenerationPoint(Color.RED)).getWeaponDeck().get(0);
        ((GenerationSquare) board.getMap().getGenerationPoint(Color.RED)).removeWeapon(testWeapon1);

        Weapon testWeapon2 = ((GenerationSquare) board.getMap().getGenerationPoint(Color.RED)).getWeaponDeck().get(0);
        ((GenerationSquare) board.getMap().getGenerationPoint(Color.RED)).removeWeapon(testWeapon2);

        Weapon testWeapon3 = ((GenerationSquare) board.getMap().getGenerationPoint(Color.RED)).getWeaponDeck().get(0);
        ((GenerationSquare) board.getMap().getGenerationPoint(Color.RED)).removeWeapon(testWeapon3);

        //setup of the points
        player1.addWeapon(testWeapon1);
        testWeapon2.setCharged(false);
        player1.addWeapon(testWeapon2);
        player1.addWeapon(testWeapon3);

        player1.getPlayerBoard().addDamage(player2, 6);
        player1.getPlayerBoard().addDamage(player3, 6);

        player1.getPlayerBoard().addMark(player2, 2);
        player1.getPlayerBoard().addMark(player2, 3);
        //player1.getPlayerBoard().addMark(player2, 3);

        map.getSquare(0).addPlayer(player1);
        map.getSquare(0).addPlayer(player2);
        map.getSquare(2).addPlayer(player3);
        ((AmmoSquare)map.getSquare(0)).removeAmmoCard();
        board.fillAmmo();

        for(int i = 0; i < 3; i++){
            Cell cell = new Cell();
            cell.setKill(player1);
            board.getSkullBoard().addCell(cell);
        }

        for(int i = 0; i < 5; i++){
            Cell cell = new Cell();
            cell.setKill(player2);
            cell.setOverKill();
            board.getSkullBoard().addCell(cell);
        }

        for(int i = 0; i < 5; i++){
            Cell cell = new Cell();
            cell.setKill(player3);
            cell.setOverKill();
            board.getSkullBoard().addCell(cell);
        }

        RuntimeTypeAdapterFactory<Square> rfSquare = RuntimeTypeAdapterFactory
                .of(Square.class, "Square")
                .registerSubtype(AmmoSquare.class, "AmmoSquare")
                .registerSubtype(GenerationSquare.class, "GenerationSquare");
        /*
        RuntimeTypeAdapterFactory<Deck> rfDeck = RuntimeTypeAdapterFactory
                .of(Deck.class, "Deck")
                .registerSubtype(WeaponDeck.class, "Weapon")
                .registerSubtype(PowerDeck.class, "GenerationSquare");

        RuntimeTypeAdapterFactory<Card> rfCard = RuntimeTypeAdapterFactory
                .of(Card.class, "Card")
                .registerSubtype(Weapon.class, "Weapon")
                .registerSubtype(Powerup.class, "GenerationSquare");

         */


        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(rfSquare)
                .create();

        String mapJson = gson.toJson(map);

        //Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(mapJson, GameBoard.class);

        assertEquals(gameBoard.getGenerationPoint(Color.BLUE).getX(), 2);
        assertEquals(gameBoard.getGenerationPoint(Color.BLUE).getY(), 0);

        Printer printer = new Printer();
        //((AmmoSquare) gameBoard.getSquare(0)).removeAmmoCard();

        //printer.printBoard(gameBoard);
        //printer.printWeaponsOnBoard(gameBoard);
        printer.printAllInfo(gameBoard, player1.getPowerups(), board.getSkullBoard(), player1.getNickname());

        printer.printPlayersInfo(gameBoard, player1.getPowerups(), player1.getNickname());

    }

    @Test
    public void printBoardTest(){
        Printer printer = new Printer();

        printer.printBoard(map);
    }

    @Test
    public void serializationTest(){

        Gson gson = new Gson();
        Player attacker = new Player("pippo");
        HashMap<Player, Integer> hp = new HashMap<>();
        hp.put(attacker, 10);



        Player dAttacker = gson.fromJson(gson.toJson(attacker), Player.class);

        HashMap<String, Integer> stringed = new HashMap<>();
        hp.forEach((x, y) -> stringed.put(gson.toJson(x), y));

        String gsonString = gson.toJson(stringed);

        Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();


        //HashMap dHp = gson.fromJson(stringed, type);
        HashMap<String, Integer> dHp =  gson.fromJson(gsonString, type);

        assertEquals(dAttacker.getNickname(), "pippo");
        //assertEquals(dHp.get(dAttacker), 10);
        System.out.println(attacker.getNickname());

        for(String p :  dHp.keySet()){
            Player pl = gson.fromJson(p, Player.class);
            System.out.println(pl.getNickname());
        }
        //dHp.forEach((x,y) -> System.out.println("x: " + x.getNickname()));

    }

    @Test
    public void getAllSquaresTest(){
        map.allSquares();
    }

}
