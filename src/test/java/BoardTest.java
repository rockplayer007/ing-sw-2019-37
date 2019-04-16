import model.board.Board;
import model.board.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    @Test
    public void boardCreationTest(){

        Board board = new Board();
        board.getMap().loadMaps();
        board.getMap().createMap(0);
        System.out.println("color is: " + board.getMap().getGenerationPoint(Color.BLUE).getX());


    }

}
