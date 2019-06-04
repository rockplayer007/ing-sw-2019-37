package model.board;
;
import model.board.Board;
import model.board.BoardGenerator;
import model.board.GameBoard;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {

    //private Board board;
    private GameBoard map;

    @BeforeEach
    public void createBoard(){
        Board board = new Board();
        map = new BoardGenerator(board).createMap(0);

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

    @Test
    public void getSquareTest(){

        assertEquals(map.getSquare(0,0), map.getSquare(0));
        assertEquals(map.getSquare(1,0), map.getSquare(1));
        assertEquals(map.getSquare(0,1), map.getSquare(3));

        //last square
        assertEquals(map.getSquare(3,2), map.getSquare(9));

        //not existing square
        assertNull(map.getSquare(0,2));
    }

    @Test
    public void directionTest(){
        //first test from position 0
        Square testSquare = map.getSquare(0);
        Map<Square.Direction,Set<Square>> directionSquares = testSquare.directions(2);
        Set<Square> rightSquares = new HashSet<>();
        Set<Square> downSquares = new HashSet<>();

        rightSquares.add(map.getSquare(1));
        rightSquares.add(map.getSquare(2));
        downSquares.add(map.getSquare(3));

        assertTrue(rightSquares.containsAll(directionSquares.get(Square.Direction.RIGHT)));

        assertTrue(downSquares.containsAll(directionSquares.get(Square.Direction.DOWN)));


        //first test from position 0
        testSquare = map.getSquare(4);
        directionSquares = testSquare.directions(2);
        rightSquares = new HashSet<>();
        downSquares = new HashSet<>();
        Set<Square> leftSquares = new HashSet<>();
        Set<Square> topSquares = new HashSet<>();

        rightSquares.add(map.getSquare(5));
        rightSquares.add(map.getSquare(6));

        leftSquares.add(map.getSquare(3));

        topSquares.add(map.getSquare(1));
        downSquares.add(map.getSquare(7));


        assertTrue(rightSquares.containsAll(directionSquares.get(Square.Direction.RIGHT)));

        assertTrue(leftSquares.containsAll(directionSquares.get(Square.Direction.LEFT)));

        assertTrue(downSquares.containsAll(directionSquares.get(Square.Direction.DOWN)));

        //for absoluteDirection

        directionSquares = testSquare.directionAbsolute(map);

        assertTrue(rightSquares.containsAll(directionSquares.get(Square.Direction.RIGHT)));

        assertTrue(leftSquares.containsAll(directionSquares.get(Square.Direction.LEFT)));

        assertTrue(downSquares.containsAll(directionSquares.get(Square.Direction.DOWN)));

        assertTrue(topSquares.containsAll(directionSquares.get(Square.Direction.TOP)));
    }



}
