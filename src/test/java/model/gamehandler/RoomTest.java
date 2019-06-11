package model.gamehandler;

import controller.RoomController;
import model.player.Heroes;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class RoomTest {
    private Room room;
    @BeforeEach
    void before(){
        room= new Room(new RoomController());
        room.setAttackHandler(new AttackHandler());
        room.createMap(0);
        Player player1 = new Player("ciao");
        player1.setHero(Heroes.BANSHEE);
        Player player2 = new Player("hello");
        player2.setHero(Heroes.D_STRUCT_OR);
        Player player3 = new Player("lol");
        player3.setHero(Heroes.DOZER);
        Player player4 = new Player("kek");
        player4.setHero(Heroes.SPROG);
        Player player5 = new Player("rip");
        player5.setHero(Heroes.VIOLET);
        room.setPlayers(Arrays.asList(player1,player2,player3,player4,player5));
        room.setCurrentPlayer(player1);
        player1.movePlayer(room.getBoard().getMap().getSquare(0));
        player2.movePlayer(room.getBoard().getMap().getSquare(2));
        player3.movePlayer(room.getBoard().getMap().getSquare(4));
        player4.movePlayer(room.getBoard().getMap().getSquare(6));
        player5.movePlayer(room.getBoard().getMap().getSquare(0));
    }

    @Test
    void endScoreTest(){
        room.getPlayers().get(0).getPlayerBoard().addPoints(10);
        room.getPlayers().get(1).getPlayerBoard().addPoints(11);
        room.getPlayers().get(2).getPlayerBoard().addPoints(12);
        room.getPlayers().get(3).getPlayerBoard().addPoints(20);
        room.getPlayers().get(4).getPlayerBoard().addPoints(5);
        room.endScoreboard().keySet().forEach(x->System.out.println(x.getNickname()));

    }

    @Test
    void endTurnControllTest(){

    }
}
