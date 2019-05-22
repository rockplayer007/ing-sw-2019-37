package boardTest;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import model.board.*;
import model.player.Heroes;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CLI.Printer;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    private GameBoard map;

    @BeforeEach
    public void createBoard(){
        Board board = new Board();
        map = new BoardGenerator(board).createMap(0);


    }
    @Test
    public void boardCreationTest(){

        assertEquals(map.getGenerationPoint(Color.BLUE).getX(), 2);
        assertEquals(map.getGenerationPoint(Color.BLUE).getY(), 0);

    }

    @Test
    public void serializeMapTest(){

        map.getSquare(0).addPlayer(new Player("", Heroes.D_STRUCT_OR));
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
        printer.printBoard(gameBoard);
        printer.printWeaponsOnBoard(gameBoard);

    }

    @Test
    public void printBoardTest(){
        Printer printer = new Printer();
        printer.printBoard(map);
    }



}
