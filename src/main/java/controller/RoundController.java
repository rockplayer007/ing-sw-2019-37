package controller;

import model.board.Square;
import model.card.Powerup;
import model.card.Weapon;
import model.exceptions.InterruptOperationException;
import model.exceptions.NotExecutedExeption;
import model.exceptions.NullTargetsException;
import model.player.ActionOption;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundController {

    private RoomController roomController;
    private boolean shot = false;

    private static final Logger logger = Logger.getLogger(RoundController.class.getName());


    public RoundController(RoomController roomController){
        this.roomController = roomController;
    }

    public void powerupController(Player player){
        Boolean usePowerups = true;
        while (usePowerups){
            List<Powerup> powerups = possiblePowerups(player);
            if(!powerups.isEmpty()){

                Powerup chosenCard = ActionHandler
                        .chooseCard(powerups, true, roomController.getRoom(), false);

                //null means the player doesnt want to use powerups
                if (chosenCard != null){
                    //the chosen powerup will be executed
                    usePowerup(chosenCard, player);
                }
                else {
                    usePowerups = false;
                }
            }
            else{
                usePowerups = false;
            }

        }
    }

    //check if the player can use the powerup
    private List<Powerup> possiblePowerups(Player player){

        List<Powerup> usable = new ArrayList<>();
        for(Powerup powerup : player.getPowerups()){

            //NEWTON before/after action. Not possibile if no players on board
            //TODO check this
            if(powerup.getName().equals("NEWTON") && roomController.getRoom().getBoard().getMap().getPlayersOnMap().size() > 1){
                usable.add(powerup);
            }
            //TELEPORTER before/after action
            if(powerup.getName().equals("TELEPORTER")){
                usable.add(powerup);
            }

            if(powerup.getName().equals("TARGETING SCOPE") && shot){
                usable.add(powerup);
            }
            //TARGETING SCOPE only after weapon

            //TAGBACK GRENADE only after a another player shot


        }
        //set back the shot flag for the next call
        shot = false;

        return usable;
    }

    private void usePowerup(Powerup powerup, Player player){
        //execute it
        try {
            powerup.getEffect().execute(roomController.getRoom());
        } catch (InterruptOperationException e) {
            logger.log(Level.WARNING, "Powerup operation interrupted", e);
        } catch (NullTargetsException e) {
            logger.log(Level.WARNING, "Powerup operation has no targets", e);
        }
        //remove the powerup from the player
        player.removePowerup(powerup);
        roomController.getRoom().getBoard().getPowerDeck().usedPowerups(powerup);

    }


    public void actionController(Player player){
        //check action in player
        List<String> send = player.getActionStatus().getJsonChoices(player,
                //if the there is only 1 player he cant shoot
                roomController.getRoom().getBoard().getMap().getPlayersOnMap().size() <= 1);
        List<ActionOption> actions = player.getActionStatus().getChoices(player,
                roomController.getRoom().getBoard().getMap().getPlayersOnMap().size() <= 1);

        ListResponse selected = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.ACTION_REQUEST));

        ActionOption choice;
        try{
            choice = actions.get(selected.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            return;
        }

        switch (choice){
            case MOVE3:
                ActionHandler.run(player, 3, roomController.getRoom());
                //player.movePlayer(ActionHandler.chooseSquare(player, player.getPosition().getValidPosition(3), roomController.getRoom()));
                //player.movePlayer(squareManager(player, 3));
                break;
            case MOVE1_GRAB:
                //send moving squares
                ActionHandler.run(player, 2, roomController.getRoom());
                //player.movePlayer(ActionHandler.chooseSquare(player, player.getPosition().getValidPosition(1), roomController.getRoom()));
                //squareManager(player, 1);
                //grap in this square
                try {
                    ActionHandler.grab(player, roomController.getRoom().getBoard(), roomController.getRoom());
                } catch (NotExecutedExeption notExecutedExeption) {
                    //send a message with exception to string
                    System.out.println(notExecutedExeption.getMessage());
                }

                break;
            case SHOOT:
                //ask card

                //execute this card
                //flag for shooting
                shot = true;
                break;
        }

    }

}