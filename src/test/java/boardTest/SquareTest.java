package boardTest;

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
    private Board.BoardMap map;

    @BeforeEach
    public void createBoard(){
        board = new Board();
        board.getMap().createMap(0);
        map = board.getMap();
    }

    @Test
    public void neighbourTest(){
        Set<Square> squares;

        squares = map.getSquare(0).getValidPosition(1);
        assertTrue(squares.contains(map.getSquare(1)));
        assertTrue(squares.contains(map.getSquare(3)));

        squares = map.getSquare(0).getValidPosition(2);
        assertTrue(squares.contains(map.getSquare(2)));
        assertTrue(squares.contains(map.getSquare(4)));
        assertTrue(squares.contains(map.getSquare(0 )));

    }

    @Test
    public void visibleSquaresTest(){

        //first test from position 1
        Square testSquare = map.getSquare(1);
        Set<Square> testVisibleSquare = testSquare.visibleSquare(map);
        Set<Square> visibleSquares = new HashSet<>();
        visibleSquares.add(map.getSquare(0));
        visibleSquares.add(map.getSquare(1));
        visibleSquares.add(map.getSquare(2));

        for(Square x : visibleSquares){
            assertTrue(testVisibleSquare.contains(x));
        }

        //test from position 0
        testSquare = map.getSquare(0);
        testVisibleSquare = testSquare.visibleSquare(map);

        visibleSquares.add(map.getSquare(3));
        visibleSquares.add(map.getSquare(4));
        visibleSquares.add(map.getSquare(5));

        for(Square x : visibleSquares){
            assertTrue(testVisibleSquare.contains(x));
        }

        //test from position 5
        testSquare = map.getSquare(5);
        testVisibleSquare = testSquare.visibleSquare(map);
        visibleSquares.add(map.getSquare(6));
        visibleSquares.add(map.getSquare(9));

        for(Square x : visibleSquares){
            assertTrue(testVisibleSquare.contains(x));
        }

    }

}
