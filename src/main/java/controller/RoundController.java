package controller;

import model.board.Square;
import model.card.Powerup;
import model.player.ActionOption;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.GeneralResponse;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;
import network.messages.serverToClient.GeneralRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundController {

    RoomController roomController;

    private static final Logger logger = Logger.getLogger(RoundController.class.getName());


    public RoundController(RoomController roomController){
        this.roomController = roomController;
    }

    public void powerupController(Player player){
        Boolean usePowerups = true;
        while (usePowerups){
            List<Powerup> powerups = possiblePowerups(player);
            if(!powerups.isEmpty()){
                //ask player if he wants to use a powerup
                usePowerups = ((GeneralResponse) roomController
                        .sendAndReceive(player, new GeneralRequest(Message.Content.YESNO_REQUEST)))
                        .getAnswer();

                if (usePowerups){
                    //send powerups
                    AnswerRequest message = new AnswerRequest(roomController
                            .toJsonCardList(powerups), Message.Content.CARD_REQUEST);
                    //sends the cards and receives the chosen one
                    ListResponse chosenCard =(ListResponse) roomController.sendAndReceive(player, message);

                    //the chosen powerup will be executed
                    usePowerup(powerups.get(chosenCard.getSelectedItem()));

                }
            }
            else{
                usePowerups = false;
            }
        }
    }

    public List<Powerup> possiblePowerups(Player player){

        List<Powerup> usable = new ArrayList<>();
        for(Powerup powerup : player.getPowerups()){
            //check if the player can use the powerup

        }
        return usable;
    }

    public void usePowerup(Powerup powerup){
        //execute it
        //remove the powerup from the player

    }


    public void actionController(Player player){
        //check action in player
        List<String> send = player.getActionStatus().getChoices();

        ListResponse action = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.ACTION_REQUEST));
        ActionOption choice;

        try{
            choice = ActionOption.valueOf(send.get(action.getSelectedItem()));
        }catch (RuntimeException e){
            //cheater
            return;
        }


        switch (choice){
            case MOVE3:

                player.movePlayer(squareManager(player, 3));
                break;
            case MOVE_GRAB:
                //send moving squares
                //grap in this square
                squareManager(player, 1);

                break;
            case SHOOT:
                //ask card
                //execute this card
                break;
        }

    }

    public Square squareManager(Player player, int n){
        List<String> send = roomController
                .toJsonSquareList(player.getPosition().getValidPosition(n));
        ListResponse action = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.SQUARE_REQUEST));
        List<Square> tempSquares;
        try{
             tempSquares = new ArrayList<>(player.getPosition().getValidPosition(3));

        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());

            return null;
        }
        return tempSquares.get(action.getSelectedItem());
    }

}
