package model.card;

import controller.MessageHandler;
import controller.RoomController;
import model.card.*;
import model.exceptions.NotExecutedException;
import model.gamehandler.AttackHandler;
import model.gamehandler.Room;
import model.player.Heroes;
import model.player.Player;
import network.client.MainClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import view.CLI.CLI;
import view.CLI.Printer;

import java.util.Arrays;

import static org.mockito.Mockito.mock;

public class OperationTest {

    Room room;
    WeaponDeck weaponDeck;


    @BeforeEach
    public void before() {
        weaponDeck = new WeaponDeck();
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
        player5.movePlayer(room.getBoard().getMap().getSquare(7));

        MessageHandler messageHandler = mock(MessageHandler.class);

        Printer printer = new Printer(new CLI(new MainClient()));
        printer.printAllInfo(room.getBoard().getMap(), player1.getPowerups(), room.getBoard().getSkullBoard());
        printer.printPlayersInfo(room.getBoard().getMap(), player1.getPowerups());
    }

    @Test
    public void operation1 () throws NotExecutedException {
        VisiblePlayers visiblePlayers = new VisiblePlayers();
        visiblePlayers.execute(room);




    }

    }
