package controller;

import com.google.gson.Gson;
import model.board.*;
import model.card.Powerup;
import model.gamehandler.Room;
import network.server.Configs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomControllerTest {


    private Room room;
    private RoomController roomController;
    private GameBoard map;

    @BeforeEach
    public void createBoard(){


        roomController = new RoomController();
        room = new Room(roomController);

        map = new BoardGenerator(room.getBoard()).createMap(3);

        room.getBoard().setMap(map);

    }


    @Test
    public void toJsonListTest(){
        List<Powerup> powerups = room.getBoard().getPowerDeck().getCard(2);

        List<String> list = roomController.everythingToJson( powerups);

        Gson gson = new Gson();
        Powerup arrivedCard1 = gson.fromJson(list.get(0), Powerup.class);
        Powerup arrivedCard2 = gson.fromJson(list.get(1), Powerup.class);

        assertEquals(( powerups.get(0)).getAmmo(), (arrivedCard1).getAmmo());

        assertEquals((powerups.get(1)).getAmmo(), (arrivedCard2).getAmmo());



        List<GenerationSquare> squares = room.getBoard().getMap().getGenPoints();
        list = roomController.everythingToJson(squares);


        Square arrivedSquare1 = gson.fromJson(list.get(0), GenerationSquare.class);
        Square arrivedSquare2 = gson.fromJson(list.get(1), GenerationSquare.class);

        assertEquals(( squares.get(0)).getColor(), arrivedSquare1.getColor());

        assertEquals((squares.get(1)).getColor(), arrivedSquare2.getColor());
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


