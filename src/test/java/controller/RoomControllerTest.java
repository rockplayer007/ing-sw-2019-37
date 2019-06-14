package controller;

import com.google.gson.Gson;
import controller.RoomController;
import model.card.Card;
import model.card.Powerup;
import model.gamehandler.Room;
import model.player.Player;
import network.server.Configs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomControllerTest {


    private Room room;
    private RoomController roomController;

    @BeforeEach
    public void createBoard(){


        roomController = new RoomController();
        room = new Room(roomController);

    }


    @Test
    public void toJsonListTest(){
        List<Powerup> powerups = room.getBoard().getPowerDeck().getCard(2);

        List<String> list = roomController.toJsonCardList( powerups);

        Gson gson = new Gson();
        Powerup arrivedCard1 = gson.fromJson(list.get(0), Powerup.class);
        Powerup arrivedCard2 = gson.fromJson(list.get(1), Powerup.class);

        assertEquals(( powerups.get(0)).getAmmo(), (arrivedCard1).getAmmo());

        assertEquals((powerups.get(1)).getAmmo(), (arrivedCard2).getAmmo());


    }

    @Test
    public void fileTest(){
        Configs configs = Configs.getInstance();
        configs.getTurnTime();
        configs.getMaximumPlayers();
        configs.getMinimumPlayers();
        configs.getSkulls();
        configs.getTimeForTagBackRequest();

    }

}
