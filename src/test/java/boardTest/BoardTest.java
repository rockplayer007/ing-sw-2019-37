package boardTest;

import com.google.gson.Gson;
import model.board.Board;
import model.board.BoardGenerator;
import model.board.Color;
import model.board.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        Gson gson = new Gson();
        String mapJson = gson.toJson(map);
        GameBoard gameBoard = gson.fromJson(mapJson, GameBoard.class);
        assertEquals(gameBoard.getGenerationPoint(Color.BLUE).getX(), 2);
        assertEquals(gameBoard.getGenerationPoint(Color.BLUE).getY(), 0);

    }

}
