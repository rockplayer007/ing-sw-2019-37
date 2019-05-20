package controllerTest;

import com.google.gson.Gson;
import controller.RoomController;
import model.card.Card;
import model.card.Powerup;
import model.gamehandler.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomControllerTest {


    private Room room;
    private RoomController roomController;

    @BeforeEach
    public void createBoard(){

        room = new Room();
        roomController = new RoomController();

    }


    @Test
    public void toJsonListTest(){
        List<Card> powerups = room.getBoard().getPowerDeck().getCard(2);

        List<String> list = roomController.toJsonCardList( powerups);

        Gson gson = new Gson();
        Card arrivedCard1 = gson.fromJson(list.get(0), Powerup.class);
        Card arrivedCard2 = gson.fromJson(list.get(1), Powerup.class);

        assertEquals(((Powerup) powerups.get(0)).getAmmo(), ((Powerup)arrivedCard1).getAmmo());

        assertEquals(((Powerup) powerups.get(1)).getAmmo(), ((Powerup)arrivedCard2).getAmmo());


    }


}
