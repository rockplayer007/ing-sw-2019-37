package controller;

import model.board.Square;
import model.card.Powerup;
import model.card.Weapon;
import model.exceptions.InterruptOperationException;
import model.exceptions.NotExecutedException;
import model.exceptions.NullTargetsException;
import model.player.ActionOption;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoundController {

    private RoomController roomController;
    private boolean shot;

    private static final Logger logger = Logger.getLogger(RoundController.class.getName());


    public RoundController(RoomController roomController){
        this.roomController = roomController;
        this.shot = false;
    }


    public void powerupController(Player player){
        boolean usePowerups = true;
        while (usePowerups){
            List<Powerup> powerups = possiblePowerups(player);
            if(!powerups.isEmpty()){

                Powerup chosenCard = ActionHandler
                        .chooseCard(powerups, true, roomController.getRoom(), false);

                //null means the player doesnt want to use powerups
                if (chosenCard != null){
                    //the chosen powerup will be executed
                    usePowerup(chosenCard, player);
                    roomController.sendUpdate();
                }
                else {
                    usePowerups = false;
                }
            }
            else{
                usePowerups = false;
            }

        }
        //set back the shot flag for the next call
        shot = false;

    }

    //check if the player can use the powerup
    private List<Powerup> possiblePowerups(Player player){

        List<Powerup> usable = new ArrayList<>();
        for(Powerup powerup : player.getPowerups()){

            //NEWTON before/after action. Not possibile if no players on board
            if(powerup.getName().equals("NEWTON") && roomController.getRoom().getBoard().getMap().getPlayersOnMap().size() > 1){
                usable.add(powerup);
            }
            //TELEPORTER before/after action
            if(powerup.getName().equals("TELEPORTER")){
                usable.add(powerup);
            }
            //TARGETING SCOPE only after weapon
            if(powerup.getName().equals("TARGETING SCOPE") && shot){
                usable.add(powerup);
            }


            //TAGBACK GRENADE only after a another player shot


        }


        return usable;
    }

    private void usePowerup(Powerup powerup, Player player){
        //execute it
        try {
            powerup.getEffect().execute(roomController.getRoom());
        } catch (NullTargetsException e) {
            logger.log(Level.WARNING, "Powerup operation has no targets", e);
        }
        //remove the powerup from the player
        player.removePowerup(powerup);
        roomController.getRoom().getBoard().getPowerDeck().usedCard(powerup);

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
                break;
            case MOVE1_GRAB:
                //backup square
                Square goBackSquare = player.getPosition();
                ActionHandler.run(player, 1, roomController.getRoom());

                //grap in this square
                try {
                    ActionHandler.grab(player, roomController.getRoom().getBoard(), roomController.getRoom());
                } catch (NotExecutedException notExecutedException) {
                    // set the player to his previous position
                    player.movePlayer(goBackSquare);
                    //send a message with exception to string
                    System.out.println(notExecutedException.getMessage());
                }

                break;
            case SHOOT:
                //ask card
                List<Weapon> usableWeapons = player.getWeapons().stream().filter(Weapon::getCharged).collect(Collectors.toList());
                Weapon chosenWeapon = ActionHandler.chooseCard(usableWeapons, false, roomController.getRoom(), true);
                try {
                    ActionHandler.shoot(roomController.getRoom(), chosenWeapon);
                } catch (NotExecutedException e) {
                    //send message
                    System.out.println("not executed");
                }
                //execute this card
                //flag for shooting, needed to decide to use a powerup or not
                shot = true;
                break;
        }

    }

}
