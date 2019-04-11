import model.board.Board;
import model.board.Color;
import org.junit.Test;

public class BoardTest {

    @Test
    public void boardCreationTest(){

        Board board = new Board();
        board.getMap().loadMaps();
        board.getMap().createMap(0);
        System.out.println("color is: " + board.getMap().giveGenerationPoint(Color.BLUE).getX());


    }

}
