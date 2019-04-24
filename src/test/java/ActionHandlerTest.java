
import model.board.Board;
import model.card.AmmoCard;
import model.card.AmmoColor;
import model.gamehandler.Room;
import model.player.ActionHandler;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class ActionHandlerTest {
    private ActionHandler actionHandler;
    private Room room;
    private AmmoCard ammoCard;


    @BeforeEach
    public void before(){
        actionHandler=new ActionHandler();
        Board board=new Board();
        room=new Room(board);
        Player p1=new Player("p1");
        Player p2=new Player("p2");
        ammoCard=new AmmoCard(AmmoColor.RED,AmmoColor.YELLOW,AmmoColor.BLUE);



        room.addPlayer(p1);
        room.addPlayer(p2);


    }

    @Test
    public void grabAmmoCard(){
        ActionHandler actionHandler=new ActionHandler();
        actionHandler.grabAmmo(room.getCurrentPlayer(),ammoCard,room.getBoard());
        EnumMap<AmmoColor,Integer> map= (EnumMap<AmmoColor,Integer>)room.getCurrentPlayer().getAmmo();

        System.out.println(map);
        assertSame(map.get(AmmoColor.RED),2);
        assertSame(map.get(AmmoColor.BLUE),2);
        assertSame(map.get(AmmoColor.YELLOW),2);
    }


}
