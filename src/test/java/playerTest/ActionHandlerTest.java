package playerTest;

import model.gamehandler.Room;
import model.card.AmmoCard;
import model.card.AmmoColor;
import model.card.PowerDeck;
import model.player.ActionHandler;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActionHandlerTest {
    private ActionHandler actionHandler;
    private Room room;
    private AmmoCard ammoCard, ammoCard1;


    @BeforeEach
    public void before(){
        actionHandler=new ActionHandler();
        Player p1=new Player("p1");
        Player p2=new Player("p2");
        List<Player> players = new ArrayList<>();
        players.add(p1); players.add(p2);
        
        room =new Room();
        room.setStartingPlayer(p1);
        room.setCurrentPlayer(p1);
        room.setPlayers(players);

        ammoCard=new AmmoCard(AmmoColor.RED,AmmoColor.YELLOW,AmmoColor.BLUE);
        ammoCard1=new AmmoCard(AmmoColor.RED,AmmoColor.BLUE);
        room.getBoard().setPowerDeck(new PowerDeck());
    }

    @Test
    public void grabAmmoCard(){
        ActionHandler actionHandler=new ActionHandler();
        actionHandler.grabAmmo(room.getCurrentPlayer(),ammoCard, room.getBoard());
        EnumMap<AmmoColor,Integer> map= (EnumMap<AmmoColor,Integer>) room.getCurrentPlayer().getAmmo();

        System.out.println(map);
        assertSame(map.get(AmmoColor.RED),2);
        assertSame(map.get(AmmoColor.BLUE),2);
        assertSame(map.get(AmmoColor.YELLOW),2);


    }
    @Test
    public void grabAmmoCard1(){
        ActionHandler actionHandler=new ActionHandler();
        actionHandler.grabAmmo(room.getCurrentPlayer(),ammoCard1, room.getBoard());
        EnumMap<AmmoColor,Integer> map= (EnumMap<AmmoColor,Integer>) room.getCurrentPlayer().getAmmo();

        System.out.println(map);
        assertSame(map.get(AmmoColor.RED),2);
        assertSame(map.get(AmmoColor.BLUE),2);
        assertSame(map.get(AmmoColor.YELLOW),1);
        assertTrue(room.getCurrentPlayer().getPowerups()!=null);
    }

    @Test
    public void setNextPlayerTest(){
        for (int i = 0; i < room.getPlayers().size() * 2; i ++){
            room.setNextPlayer();
        }

    }

}
