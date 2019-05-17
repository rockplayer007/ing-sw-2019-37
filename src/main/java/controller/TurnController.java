package controller;

import com.google.gson.Gson;
import model.board.Color;
import model.card.Card;
import model.card.Powerup;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.GeneralResponse;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnController {
    private RoomController roomController;
    private Room room;
    private Timer timer;
    private RoundStatus roundStatus;

    private static final Logger logger = Logger.getLogger(TurnController.class.getName());

    public TurnController(RoomController roomController, Room room) {
        this.roomController = roomController;
        this.room = room;
        this.roundStatus = RoundStatus.FIRST_ROUND;
    }

    public void startPlayerRound(Player player){
        if(roundStatus == RoundStatus.FIRST_ROUND){
            firstRound(player);
            //continue with normal round
            normalRound();
        }
        else if (roundStatus == RoundStatus.NORMAL_ROUND){
            normalRound();
        }
    }

    public void firstRound(Player currentPlayer){

        List<Card> powerup = room.getBoard().getPowerDeck().getCard(2);
        AnswerRequest message = new AnswerRequest(roomController.toJsonList(powerup), Message.Content.CARD_REQUEST);
        //sends the cards and receives the chosen one
        //chosen card is the card to KEEP
        ListResponse chosenCard =(ListResponse) roomController.sendAndReceive(currentPlayer, message);

        if(chosenCard.getSelectedItem() != powerup.size()){
            //player is a cheater, set wrong card
            //to manage
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", currentPlayer.getNickname());
            return;
        }

        Powerup playerCard = (Powerup) powerup.get(chosenCard.getSelectedItem());
        powerup.remove(chosenCard.getSelectedItem());

        //give the chosen card to the player
        currentPlayer.addPowerup(playerCard);

        //discard the second card

        room.getBoard().getPowerDeck().usedPowerups((Powerup) powerup.get(0));


        //put the player on the generation square
        Color spawnColor = Color.valueOf(playerCard.getAmmo().toString());
        room.getBoard().getMap().getGenerationPoint(spawnColor).addPlayer(currentPlayer);



    }

    public void normalRound(){

    }

    public void finalFrenzy(){

    }


    private void startTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run(){

            }

        }, 1*5*1000);

    }




    private enum RoundStatus{
        FIRST_ROUND, NORMAL_ROUND, FINAL_ROUND;
    }

}
