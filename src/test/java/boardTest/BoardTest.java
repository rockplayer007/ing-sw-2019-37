package boardTest;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.board.*;
import model.card.Card;
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

        RuntimeTypeAdapterFactory<Square> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Square.class, "Square")
                .registerSubtype(AmmoSquare.class, "AmmoSquare")
                .registerSubtype(GenerationSquare.class, "GenerationSquare");

        Gson gson2 = new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();

        String mapJson = gson2.toJson(map);

        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(mapJson, GameBoard.class);
        assertEquals(gameBoard.getGenerationPoint(Color.BLUE).getX(), 2);
        assertEquals(gameBoard.getGenerationPoint(Color.BLUE).getY(), 0);

        Printer printer = new Printer();
        printer.printBoard(gameBoard);

    }

    @Test
    public void printBoardTest(){
        Printer printer = new Printer();
        printer.printBoard(map);
    }

}
