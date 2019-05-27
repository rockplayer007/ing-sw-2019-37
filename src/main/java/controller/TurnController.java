package controller;

import model.board.Color;
import model.card.Card;
import model.card.Powerup;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class    TurnController {
    private RoomController roomController;
    private Room room;
    private Timer timer;
    private RoundStatus roundStatus;
    private RoundController roundController;

    private static final Logger logger = Logger.getLogger(TurnController.class.getName());

    public TurnController(RoomController roomController, Room room) {
        this.roomController = roomController;
        this.room = room;
        this.roundStatus = RoundStatus.FIRST_ROUND;
        roundController = new RoundController(roomController);

    }

    public void startPlayerRound(){

        //need to ckeck with currentPlayer
        for(Player player : room.getPlayers()){
            if(roundStatus == RoundStatus.FIRST_ROUND){
                firstRound(player);
                //continue with normal round
                normalRound(player);
            }
            else if (roundStatus == RoundStatus.NORMAL_ROUND){
                normalRound(player);
            }
            room.setNextPlayer();
        }
        roundStatus = RoundStatus.NORMAL_ROUND;


        //TODO change this
        startPlayerRound();
    }

    public void firstRound(Player currentPlayer){

        List<Card> powerup = room.getBoard().getPowerDeck().getCard(2);
        AnswerRequest message = new AnswerRequest(roomController.toJsonCardList(powerup), Message.Content.POWERUP_REQUEST);
        //sends the cards and receives the chosen one
        //chosen card is the card to KEEP
        ListResponse chosenCard =(ListResponse) roomController.sendAndReceive(currentPlayer, message);

        Powerup playerCard;
        try{
            //check if the size is not different
            playerCard = (Powerup) powerup.get(chosenCard.getSelectedItem());
        }catch (RuntimeException e){
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", currentPlayer.getNickname());
            return;
        }
        powerup.remove(chosenCard.getSelectedItem());

        //give the chosen card to the player
        currentPlayer.addPowerup(playerCard);

        //discard the second card

        room.getBoard().getPowerDeck().usedCard((Powerup) powerup.get(0));


        //put the player on the generation square
        Color spawnColor = Color.valueOf(playerCard.getAmmo().toString());
        currentPlayer.movePlayer(room.getBoard().getMap().getGenerationPoint(spawnColor));


        roomController.sendUpdate();
    }

    public void normalRound(Player player){
        //in normal round do this 2 times
        for(int i = 0; i < 2; i++){
            //first ask for powerup
            roundController.powerupController(player);
            //roomController.sendUpdate();
            //ask for action
            roundController.actionController(player);
            roomController.sendUpdate();
        }
        //ask last time
        roundController.powerupController(player);

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
