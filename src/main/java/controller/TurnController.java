package controller;

import model.card.PowerDeck;
import model.card.Powerup;
import model.gamehandler.Room;
import model.player.Player;

import java.util.List;

public class TurnController {
    private RoomController roomController;
    private Room room;

    public TurnController(RoomController roomController, Room room) {
        this.roomController = roomController;
        this.room = room;
    }

    public void firstRound(){
        for (Player currentPlayer : room.getPlayers()){

            List<Powerup> powerup = (List<Powerup>)(List<?>) room.getBoard().getPowerDeck().getCard(2);


            //send cards
            //put one card to player and one back in the deck
            //put the player on the generation square

        }
    }

    public void normalRound(){

    }

    public void finalFrenzy(){

    }


}
