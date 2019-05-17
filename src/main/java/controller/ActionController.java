package controller;

import model.card.Card;
import model.card.Powerup;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.GeneralResponse;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;
import network.messages.serverToClient.GeneralRequest;

import java.util.ArrayList;
import java.util.List;

public class ActionController {

    RoomController roomController;

    public ActionController(RoomController roomController){
        this.roomController = roomController;
    }

    public void playerActions(Player player){
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
                            .toJsonList(powerups), Message.Content.CARD_REQUEST);
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

    }


}
