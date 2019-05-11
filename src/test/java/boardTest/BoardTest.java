package boardTest;

import model.board.Board;
import model.board.BoardMap;
import model.board.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    @Test
    public void boardCreationTest(){

        Board board = new Board();
        BoardMap boardMap = new BoardMap(board);

        boardMap.createMap(0);
        System.out.println("color is: " + boardMap.getGenerationPoint(Color.BLUE).getX());

    }

}
