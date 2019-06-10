package controller;

import model.board.Square;
import model.card.Powerup;
import model.card.Weapon;
import model.exceptions.NotExecutedException;

import model.exceptions.TimeFinishedException;

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
    public void resetShot(){
        shot = false;
    }
    public boolean shoot(){
        return shot;
    }


    public void powerupController(Player player) throws TimeFinishedException {
        boolean usePowerups = true;
        while (usePowerups){
            List<Powerup> powerups = possiblePowerups(player);
            if(!powerups.isEmpty()){

                Powerup chosenCard = MessageHandler
                        .chooseCard(powerups, true, roomController.getRoom(), false,
                                "Want to attack with one of your powerups?");

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

    private void usePowerup(Powerup powerup, Player player) throws TimeFinishedException{
        //execute it
        try {
            powerup.getEffect().execute(roomController.getRoom());
        } catch (NotExecutedException e) {
            logger.log(Level.WARNING, "Powerup operation has no targets", e);
            MessageHandler.sendInfo(player, "Powerup operation has no targets", roomController.getRoom());
        }
        //remove the powerup from the player
        player.removePowerup(powerup);
        roomController.getRoom().getBoard().getPowerDeck().usedCard(powerup);

    }


    public void actionController(Player player) throws TimeFinishedException{
        //check action in player
        List<String> send = player.getActionStatus().getJsonChoices(player,
                //if the there is only 1 player he cant shoot
                roomController.getRoom().getBoard().getMap().getPlayersOnMap().size() <= 1);
        List<ActionOption> actions = player.getActionStatus().getChoices(player,
                roomController.getRoom().getBoard().getMap().getPlayersOnMap().size() <= 1);

        ListResponse selected = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.ACTION_REQUEST,
                        "Choose an action"));

        ActionOption choice;
        try{
            choice = actions.get(selected.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            return;
        }
        //temp variables
        Square goBackSquare;
        List<Weapon> usableWeapons;
        Weapon chosenWeapon;

        switch (choice){
            case MOVE3:
                ActionHandler.run(player, 3, roomController.getRoom());
                break;
            case MOVE4:
                ActionHandler.run(player, 4, roomController.getRoom());
                break;
            case MOVE1_GRAB:
                //backup square
                goBackSquare = player.getPosition();
                ActionHandler.run(player, 1, roomController.getRoom());

                //grap in this square
                try {
                    ActionHandler.grab(player, roomController.getRoom().getBoard(), roomController.getRoom());
                } catch (NotExecutedException notExecutedException) {
                    // set the player to his previous position
                    player.movePlayer(goBackSquare);
                    //send a message with exception to string
                    MessageHandler.sendInfo(player, notExecutedException.getMessage(), roomController.getRoom());
                    System.out.println(notExecutedException.getMessage());

                    actionController(player);
                }

                break;

            case MOVE2_GRAB:
                //backup square
                goBackSquare = player.getPosition();
                ActionHandler.run(player, 2, roomController.getRoom());

                //grap in this square
                try {
                    ActionHandler.grab(player, roomController.getRoom().getBoard(), roomController.getRoom());
                } catch (NotExecutedException notExecutedException) {
                    // set the player to his previous position
                    player.movePlayer(goBackSquare);
                    //send a message with exception to string
                    MessageHandler.sendInfo(player, notExecutedException.getMessage(), roomController.getRoom());
                    System.out.println(notExecutedException.getMessage());

                    actionController(player);
                }
                break;

            case MOVE3_GRAB:
                //backup square
                goBackSquare = player.getPosition();
                ActionHandler.run(player, 3, roomController.getRoom());

                //grap in this square
                try {
                    ActionHandler.grab(player, roomController.getRoom().getBoard(), roomController.getRoom());
                } catch (NotExecutedException notExecutedException) {
                    // set the player to his previous position
                    player.movePlayer(goBackSquare);
                    //send a message with exception to string
                    MessageHandler.sendInfo(player, notExecutedException.getMessage(), roomController.getRoom());
                    System.out.println(notExecutedException.getMessage());

                    actionController(player);
                }
                break;

            case SHOOT:
                //ask card
                usableWeapons = player.getWeapons().stream().filter(Weapon::getCharged).collect(Collectors.toList());
                chosenWeapon = MessageHandler.chooseCard(usableWeapons, false, roomController.getRoom(), true,
                        "Shoot with one of your weapons!");

                if (chosenWeapon == null) {
                    //cheater
                }

                //execute this card
                try {
                    ActionHandler.shoot(roomController.getRoom(), chosenWeapon);
                    //flag for shooting, needed to decide to use a powerup or not
                    shot = true;
                } catch (NotExecutedException e) {
                    //send message
                    System.out.println("not executed");
                    MessageHandler.sendInfo(player, e.getMessage(), roomController.getRoom());
                    actionController(player);
                }

                break;

            case MOVE1_SHOOT:
                goBackSquare = player.getPosition();
                ActionHandler.run(player, 2, roomController.getRoom());

                //ask card
                usableWeapons = player.getWeapons().stream().filter(Weapon::getCharged).collect(Collectors.toList());
                chosenWeapon = MessageHandler.chooseCard(usableWeapons, false, roomController.getRoom(), true,
                        "Shoot with one of your weapons!");

                if (chosenWeapon == null) {
                    //cheater
                    actionController(player);
                }

                //execute this card
                try {
                    ActionHandler.shoot(roomController.getRoom(), chosenWeapon);
                    //flag for shooting, needed to decide to use a powerup or not
                    shot = true;
                } catch (NotExecutedException e) {
                    //set the player back
                    player.movePlayer(goBackSquare);

                    System.out.println("not executed");
                    MessageHandler.sendInfo(player, e.getMessage(), roomController.getRoom());

                    actionController(player);
                }
                break;
            case MOVE1_RELOAD_SHOOT:
                goBackSquare = player.getPosition();
                ActionHandler.run(player, 1, roomController.getRoom());
                ActionHandler.reload(player, roomController.getRoom());

                //ask card
                usableWeapons = player.getWeapons().stream().filter(Weapon::getCharged).collect(Collectors.toList());
                chosenWeapon = MessageHandler.chooseCard(usableWeapons, false, roomController.getRoom(), true,
                        "Shoot with one of your weapons!");

                if (chosenWeapon == null) {
                    //cheater
                    return;
                }

                //execute this card
                try {
                    ActionHandler.shoot(roomController.getRoom(), chosenWeapon);
                    //flag for shooting, needed to decide to use a powerup or not
                    shot = true;
                } catch (NotExecutedException e) {
                    //set the player back
                    player.movePlayer(goBackSquare);

                    System.out.println("not executed");
                    MessageHandler.sendInfo(player, e.getMessage(), roomController.getRoom());

                    actionController(player);
                }
                break;
            case MOVE2_RELOAD_SHOOT:
                goBackSquare = player.getPosition();
                ActionHandler.run(player, 2, roomController.getRoom());
                ActionHandler.reload(player, roomController.getRoom());

                //ask card
                usableWeapons = player.getWeapons().stream().filter(Weapon::getCharged).collect(Collectors.toList());
                chosenWeapon = MessageHandler.chooseCard(usableWeapons, false, roomController.getRoom(), true,
                        "Shoot with one of your weapons!");

                if (chosenWeapon == null) {
                    //cheater
                    return;
                }

                //execute this card
                try {
                    ActionHandler.shoot(roomController.getRoom(), chosenWeapon);
                    //flag for shooting, needed to decide to use a powerup or not
                    shot = true;
                } catch (NotExecutedException e) {
                    //set the player back
                    player.movePlayer(goBackSquare);

                    System.out.println("not executed");
                    MessageHandler.sendInfo(player, e.getMessage(), roomController.getRoom());

                    actionController(player);
                }
                break;

        }

    }

}
