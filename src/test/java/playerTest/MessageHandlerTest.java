package playerTest;

import controller.RoomController;
import model.gamehandler.Room;
import model.card.AmmoCard;
import model.card.AmmoColor;
import model.card.PowerDeck;
import model.player.Heroes;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MessageHandlerTest {

    private Room room;
    private AmmoCard ammoCard, ammoCard1;


    @BeforeEach
    public void before(){

        Player p1=new Player("p1");
        p1.setHero(Heroes.BANSHEE);
        Player p2=new Player("p2");
        p2.setHero(Heroes.D_STRUCT_OR);

        room =new Room(new RoomController());
        room.setStartingPlayer(p1);
        room.setCurrentPlayer(p1);
        room.setPlayers(Arrays.asList(p1,p2));

        ammoCard=new AmmoCard("",AmmoColor.RED,AmmoColor.YELLOW,AmmoColor.BLUE);
        ammoCard1=new AmmoCard("",AmmoColor.RED,AmmoColor.BLUE);
        room.getBoard().setPowerDeck(new PowerDeck());
    }

    @Test
    public void grabAmmoCard(){
/*
        MessageHandler.grabAmmo(room.getCurrentPlayer(),ammoCard, room.getBoard());
        EnumMap<AmmoColor,Integer> map= (EnumMap<AmmoColor,Integer>) room.getCurrentPlayer().getAmmo();

        System.out.println(map);
        assertSame(map.get(AmmoColor.RED),2);
        assertSame(map.get(AmmoColor.BLUE),2);
        assertSame(map.get(AmmoColor.YELLOW),2);

 */


    }
    @Test
    public void grabAmmoCard1(){
/*
        MessageHandler.grabAmmo(room.getCurrentPlayer(),ammoCard1, room.getBoard());
        EnumMap<AmmoColor,Integer> map= (EnumMap<AmmoColor,Integer>) room.getCurrentPlayer().getAmmo();

        System.out.println(map);
        assertSame(map.get(AmmoColor.RED),2);
        assertSame(map.get(AmmoColor.BLUE),2);
        assertSame(map.get(AmmoColor.YELLOW),1);
        assertTrue(room.getCurrentPlayer().getPowerups()!=null);

 */
    }

    @Test
    public void setNextPlayerTest(){
        for (int i = 0; i < room.getPlayers().size() * 2; i ++){
            room.setNextPlayer();
        }

    }

}
