package controller;

import model.board.Color;
import model.card.Card;
import model.card.Powerup;
import model.exceptions.TimeFinishedException;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;
import network.messages.serverToClient.TimeoutMessage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnController {
    private RoomController roomController;
    private Room room;
    private CountDown timer;
    private boolean gameFinished;

    private RoundController roundController;

    private static final Logger logger = Logger.getLogger(TurnController.class.getName());

    public TurnController(RoomController roomController, Room room) {
        this.roomController = roomController;
        this.room = room;
        roundController = new RoundController(roomController);
        gameFinished = false;
        timer = new CountDown(1*20*1000, () -> {
            System.out.println("time finished");
            roomController.stopWaiting();

        }); //10 seconds
    }

    public void startPlayerRound(){

        //need to ckeck with currentPlayer

        while (!gameFinished){
            Player player = room.getCurrentPlayer();
            //in case the timer finished reset the shoot for the next player
            roundController.resetShot();
            timer.startTimer();
            try {
                if(player.getRoundStatus() == Player.RoundStatus.FIRST_ROUND){

                    firstRound(player);
                    player.setNextRoundstatus();

                    //continue with normal round
                    normalRound(player);

                    //do this only once
                    if(room.getStartingPlayer() == null){
                        room.setStartingPlayer(player);
                    }

                }
                else if (player.getRoundStatus() == Player.RoundStatus.NORMAL_ROUND){
                    normalRound(player);
                }

                timer.cancelTimer();
            } catch (TimeFinishedException e) {
                //send message
                roomController.sendMessage(room.getCurrentPlayer(), new TimeoutMessage());
                //set the player as disconnected
                //continue as normal
                System.out.println("player: " + room.getCurrentPlayer() + " diconnected");
                roomController.sendUpdate();
            }


            //after taking the ammoCard set a new card
            room.getBoard().fillAmmo();
            room.setNextPlayer();
        }

    }

    public void firstRound(Player currentPlayer) throws TimeFinishedException {

        List<Card> powerup = room.getBoard().getPowerDeck().getCard(2);
        AnswerRequest message = new AnswerRequest(roomController.toJsonCardList(powerup), Message.Content.POWERUP_REQUEST);
        //sends the cards and receives the chosen one
        //chosen card is the card to KEEP
        ListResponse chosenCard;
        try {
            chosenCard = (ListResponse) roomController.sendAndReceive(currentPlayer, message);
        } catch (TimeFinishedException e) {
            room.getBoard().getPowerDeck().usedCard(((Powerup) powerup.get(0)));
            room.getBoard().getPowerDeck().usedCard(((Powerup) powerup.get(1)));
            throw new TimeFinishedException();
        }

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

        //discard the second card (that is in first position now)
        room.getBoard().getPowerDeck().usedCard((Powerup) powerup.get(0));


        //put the player on the generation square
        Color spawnColor = Color.valueOf(playerCard.getAmmo().toString());
        currentPlayer.movePlayer(room.getBoard().getMap().getGenerationPoint(spawnColor));

        roomController.sendUpdate();
    }

    public void normalRound(Player player) throws TimeFinishedException{
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

        //reload
        ActionHandler.reload(player, room);

    }

    public void finalFrenzy(){

    }


}
