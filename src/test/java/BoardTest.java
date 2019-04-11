import model.board.Board;
import model.board.Color;
import org.junit.Test;

public class BoardTest {

    @Test
    public void boardCreationTest(){

        Board board = new Board();
        board.map.loadMaps();
        board.map.createMap(0);
        System.out.println("color is: " + board.map.giveGenerationPoint(Color.BLUE).getX());


    }

}
