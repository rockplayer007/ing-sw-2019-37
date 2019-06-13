package model.player;

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

public class ActionHandlerTest {

    private Room room;


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

    }


    @Test
    public void setNextPlayerTest(){
        for (int i = 0; i < room.getPlayers().size() * 2; i ++){
            room.setNextPlayer();
        }

    }

}
