import model.board.Board;
import model.board.Color;
import model.board.Square;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {

    private Board board;

    @BeforeEach
    public void createBoard(){
        board = new Board();
        board.getMap().loadMaps();
        board.getMap().createMap(0);
    }

    @Test
    public void neighbourTest(){
        Set<Square> squares;
        Board.BoardMap map = board.getMap();
        squares = map.getSquare(0).getValidPosition(1);
        assertTrue(squares.contains(map.getSquare(1)));
        assertTrue(squares.contains(map.getSquare(3)));

        squares = map.getSquare(0).getValidPosition(2);
        assertTrue(squares.contains(map.getSquare(2)));
        assertTrue(squares.contains(map.getSquare(4)));
        assertTrue(squares.contains(map.getSquare(0 )));

    }

}
