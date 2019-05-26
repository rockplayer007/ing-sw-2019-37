package controller;

import model.board.*;
import model.card.*;

import model.exceptions.InterruptOperationException;
import model.exceptions.NotEnoughException;
import model.exceptions.NotExecutedExeption;
import model.exceptions.NullTargetsException;
import model.gamehandler.AttackHandler;
import model.exceptions.*;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * contain the min actions t
 */
public class ActionHandler {

    private static final Logger logger = Logger.getLogger(ActionHandler.class.getName());

    private ActionHandler(){
    throw new IllegalStateException("Utility class");
    }


    /**
     * run actions let current player to changes position
     * @param player that do this action.
     * @param  distanceMax Max distance that the player can move
     */
    public static void run(Player player, int distanceMax, Room room) {
        Set<Square> validPositions = player.getPosition().getValidPosition(distanceMax);
        Square destination = chooseSquare(player, validPositions, room);
        player.movePlayer(destination);
    }
    /**
     *  general way to let player chooses the Squere that he can go
     * @param player current player
     * @param  validPositions all ssquare that you can choose.
     * @return the Square that the player choose to move
     */
    public static Square chooseSquare(Player player,Set<Square> validPositions, Room room) {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonSquareList(validPositions);
        ListResponse square = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.SQUARE_REQUEST));

        List<Square> tempSquares;
        try{
            //needed to convert set into the array
            tempSquares = new ArrayList<>(validPositions);
            return tempSquares.get(square.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());

            List<Square> x = new ArrayList<>(validPositions);
            return x.get(0);
        }
    }

    /**
     * use the weapon to shoot
     * @param room of the player in
     * @param weapon that the player want to use
     */
    public static void shoot(Room room, Weapon weapon) throws NotExecutedExeption {
        room.setAttackHandler(new AttackHandler());
        Map<Effect,Integer> effects = weapon.getEffects();
        Effect effectSelect;
        Player player=room.getCurrentPlayer();
        Square playerPosition = player.getPosition();
        List<Effect> validEffect = new ArrayList<>(weapon.getLevelEffects(-1));
        validEffect.addAll(weapon.getLevelEffects(0));
        effectSelect = chooseEffect(player,validEffect, room);
        int i = 1;
        while (effectSelect!=null){
            validEffect.remove(effectSelect);
            try {
                deduction(player,effectSelect.getExtraCost(), room);
                effectSelect.execute(room);
                if (!weapon.getOptional())
                    break;
            } catch (NullTargetsException|NotEnoughException|InterruptOperationException e) {
                e.printStackTrace();
//                TODO
            }
            if (validEffect.isEmpty()|| validEffect.stream().allMatch(x->effects.get(x)==-1)){
                validEffect.addAll(weapon.getLevelEffects(i));
                i++;
            }
            validEffect = validEffect.stream().filter(x->player.enoughAmmos(x.getExtraCost(),true)).collect(Collectors.toList());
            effectSelect = chooseEffect(player,validEffect, room);
        }
        weapon.setCharged(false);

    }

    // if effects isempty return null.
    public static Effect chooseEffect(Player player, List<Effect> effects, Room room){
        if(effects.isEmpty()){
            return null;
        }

        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonEffectList(effects);

        ListResponse effect = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.EFFECT_REQUEST));

        try{
            return effects.get(effect.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }

    }

    public static List<Player> choosePlayers(Player player, List<Player> possiblePlayers, int maxPlayerToChoose, Room room){

        List<Player> playersToAttack = new ArrayList<>();
        int askIterations = (maxPlayerToChoose < possiblePlayers.size() ? maxPlayerToChoose : possiblePlayers.size());

        RoomController roomController = room.getRoomController();

        for (int i = 0; i < askIterations; i++){

            List<String> send = roomController
                    .toJsonPlayerList(possiblePlayers);
            ListResponse chosenPlayer = (ListResponse) roomController
                    .sendAndReceive(player, new AnswerRequest(send, Message.Content.PLAYER_REQUEST));

            try{
                playersToAttack.add(possiblePlayers.get(chosenPlayer.getSelectedItem()));
            }catch (RuntimeException e){
                //cheater
                logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
                return null;
            }
        }
        return playersToAttack;
    }

    public static Square.Direction chooseDirection(Player player, List<Square.Direction> directions, Room room){
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonDirectionList(directions);

        ListResponse direction = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.DIRECTION_REQUEST));

        try{
            return directions.get(direction.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }
    }

    public static AmmoColor chooseAmmoColor(Player player, List<AmmoColor> ammo, Room room){
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonAmmoColortList(ammo);

        ListResponse chosenAmmo = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.AMMOCOLOR_REQUEST));

        try{
            return ammo.get(chosenAmmo.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }
    }

    public static Color chooseRoom(Player player, List<Color> rooms, Room room){
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonColorList(rooms);

        ListResponse chosenRoom = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.ROOM_REQUEST));

        try{
            return rooms.get(chosenRoom.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }
    }

    /**
     * basic grab method let player to grab all card that they can
     * @param player that do this action.
     * @param board that the player play.
     */
    public static void grab( Player player,Board board, Room room) throws NotExecutedExeption{

        if (!player.getPosition().isGenerationPoint()){
            AmmoCard card = ((AmmoSquare) player.getPosition()).getAmmoCard();
            card.getAmmoList().forEach(player::addAmmo);
            if (card.hasPowerup() && player.getPowerups().size() < 3) {
                player.addPowerup((Powerup) board.getPowerDeck().getCard());
            }
            //after taking the ammoCard set a new card
            ((AmmoSquare) player.getPosition()).setAmmoCard((AmmoCard) board.getAmmoDeck().getCard());
        }
        else {

            GenerationSquare currentSquare = (GenerationSquare) player.getPosition();
            //takes only weapons that the player can pay
            List<Weapon> weapons=currentSquare.getWeaponDeck().stream().
                    filter(i->player.enoughAmmos(i.getBuyCost(),true))
                    .collect(Collectors.toList());


            if (!weapons.isEmpty()){
                Weapon weapon = chooseCard(weapons, true, room, true);

                if (weapon==null){
                    throw new NotExecutedExeption("No card has been chosen");
                }

                //pay
                try {
                    List<AmmoColor> cost = weapon.getBuyCost();
                    deduction(player,cost, room);
                } catch (NotEnoughException e) {
                    throw new NotExecutedExeption("Not enough ammo to pay");
                }


                //player needs to swap cards if he has already 3
                if (player.limitWeapon()) {
                    //choose weapon to discard
                    Weapon discardWeapon = chooseCard(player.getWeapons(), false, room, true);

                    //just in case make this check
                    if (discardWeapon==null){
                        return;
                    }

                    discardWeapon.setCharged(true);
                    //places the discarded weapon in the same place of the weapon before
                    currentSquare.getWeaponDeck()
                            .set(currentSquare.getWeaponDeck().indexOf(weapon), discardWeapon);

                    //remove and add the new weapon in the same position
                    player.getWeapons()
                            .set(player.getWeapons().indexOf(discardWeapon), weapon);

                }
                else{
                    //replace the weapon in the deck with a new one if there are cards
                    Weapon temp = (Weapon) room.getBoard().getWeaponDeck().getCard();
                    if( temp != null){
                        currentSquare.getWeaponDeck()
                                .set(currentSquare.getWeaponDeck().indexOf(weapon), temp);
                    }
                    // add it to the player
                    player.addWeapon(weapon);

                }

            }
            else{
                // etiher no there are no cards or not enough ammo to pay
                throw new NotExecutedExeption("No available cards to choose");
            }

        }
    }

    /**
     * general way to let player chooses the cards he wants to use
     * @param cards cards that need to choose
     * @param isOptional if true the player can decide wheather to use the card or not
     * @return position of card choose in the List
     */

    public static <T extends Card> T chooseCard(List<T> cards, boolean isOptional, Room room, boolean isWeapon) {
//        TODO make it more general for other uses

        AnswerRequest message = new AnswerRequest(room
                .getRoomController()
                .toJsonCardList(cards),
                //send message corrisponding to the request
                isWeapon ? Message.Content.WEAPON_REQUEST : Message.Content.POWERUP_REQUEST);
        if(isOptional){
            message.setIsOptional();
        }

        //send powerups
        ListResponse chosenCard = (ListResponse) room
                .getRoomController().sendAndReceive(room.getCurrentPlayer(), message);

        //-1 means the player doesnt want to use powerups
        if (chosenCard.getSelectedItem() < cards.size() && chosenCard.getSelectedItem() >= 0){
            //the chosen powerup will be executed
            return cards.get(chosenCard.getSelectedItem());
        }
        else{
            if(isOptional){
                return null;
            }
            else{
                //is a cheater
                logger.log(Level.WARNING, "CHEATER DETECTED: {0}", room.getCurrentPlayer().getNickname());
                return null;
            }

        }
    }

    //payment method
    public static void deduction(Player player, List<AmmoColor> cost, Room room) throws NotEnoughException {
        List<AmmoColor> tempCost = new ArrayList<>(cost);
        if (cost.isEmpty()){
            return;
        }
        if (!player.enoughAmmos(tempCost, true)){
            throw new NotEnoughException("Not enough ammo to pay");
        }

        // put only powerups that he can use to pay
        List<Powerup> powerupsToPay = new ArrayList<>();
        for(Powerup powerup : player.getPowerups()){
            for(AmmoColor ammo : tempCost){
                if(powerup.getAmmo() == ammo){
                    powerupsToPay.add(powerup);
                }
            }
        }


        //the player has to choose a card if he has no ammo
        //the player can choose to not use a card if he has ammo

        while (!tempCost.isEmpty()) {

            if (!powerupsToPay.isEmpty()) {

                //is optional only if has enough ammo to pay
                Powerup chosenCard = ActionHandler
                        .chooseCard(powerupsToPay, player.enoughAmmos(cost, false), room, false);

                //null means the player doesnt want to use powerups
                if (chosenCard != null) {
                    //the chosen powerup will be used to pay
                    player.removePowerup(chosenCard);
                    tempCost.remove(chosenCard.getAmmo());
                    powerupsToPay.remove(chosenCard);
                }
                else {
                    //pay the remaining with ammo instead
                    for(AmmoColor ammo : tempCost){
                        try {
                            player.removeAmmo(ammo);
                            //this gives an error, dont need anyway
                            //tempCost.remove(ammo);
                        } catch (AmmoException e) {
                            throw new NotEnoughException("no ammo");
                        }
                    }
                    tempCost.clear();
                }
            }
            else {
                //pay with ammo instead of paying with powerups
                for(AmmoColor ammo : tempCost){
                    try {
                        player.removeAmmo(ammo);
                    } catch (AmmoException e) {
                        throw new NotEnoughException("no ammo");
                    }
                }
                tempCost.clear();
            }
        }
    }

    /**
     * reload the weapon
     * @param player that do this action
     */
    public static void reload(Player player, Room room) throws NotEnoughException {
        List<Weapon> weapons = player.getWeapons().stream().filter(x->!x.getCharged()).collect(Collectors.toList());
        while (!weapons.isEmpty()) {
            Weapon weapon = chooseCard(weapons, true, room, true);
                if (weapon == null)
                    break;
                List<AmmoColor> cost = weapon.getChargeCost();

                deduction(player,cost, room);
                weapon.setCharged(true);
        }

    }


}
