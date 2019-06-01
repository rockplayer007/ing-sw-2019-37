package controller;

import model.board.AmmoSquare;
import model.board.Board;
import model.board.GenerationSquare;
import model.board.Square;
import model.card.*;

import model.exceptions.AmmoException;
import model.exceptions.NotEnoughException;
import model.exceptions.NotExecutedException;
import model.gamehandler.AttackHandler;
import model.gamehandler.Room;
import model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.exceptions.TimeFinishedException;
import java.util.stream.Collectors;

public class ActionHandler {

    private ActionHandler(){
        throw new IllegalStateException("Utility class");
    }


    /**
     * use the weapon to shoot
     * @param room of the player in
     * @param weapon that the player want to use
     */
    public static void shoot(Room room, Weapon weapon) throws NotExecutedException, TimeFinishedException {
        // reinitialization of AttackHandler,
        room.setAttackHandler(new AttackHandler());
        Map<Effect,Integer> effects = weapon.getEffects();
        Effect effectSelect;
        boolean used=false; // a variable local need for check in the exception
        Player player=room.getCurrentPlayer();
        // save the position of player for undo (use this only if is necessary)
        Square playerPosition = player.getPosition();

        // add immediately the effect that have level -1 and initialization of list, if the weapon haven't effect -1 do only initialization
        List<Effect> validEffect = new ArrayList<>(weapon.getLevelEffects(-1));
        //add the effect that have level 0
        validEffect.addAll(weapon.getLevelEffects(0));
        validEffect = validEffect.stream().filter(x->player.enoughAmmos(x.getExtraCost(),true)).collect(Collectors.toList());
        effectSelect = MessageHandler.chooseEffect(player,validEffect, room);
        int i = 1;// level counter
        // if the player dont want use any effect.
        if (effectSelect == null)
            throw new NotExecutedException("Not effect chosen");


        while (effectSelect!=null){
            validEffect.remove(effectSelect);

            try {
                payment(player,effectSelect.getExtraCost(), room);
                //TODO fare un funzione del tipo payment che permette undo
                effectSelect.execute(room);
                // if the effect used is not level -1, means the weapon is used so i need set state of "Charged"
                if (!used && effects.get(effectSelect)!=-1) {
                    used = true;
                    weapon.setCharged(false);
                }
                room.getRoomController().sendUpdate();
                if (!weapon.getOptional())
                    break;
            } catch (NotExecutedException e) {
                throw new NotExecutedException("Effect is not possible used");
            } catch (NotEnoughException e) {
                //TODO
            }finally {
                if (!used)
                    player.movePlayer(playerPosition);
                //TODO da vedere come e fatto il undo della payment.
            }

            if (validEffect.isEmpty()|| validEffect.stream().allMatch(x->effects.get(x)==-1)){
                validEffect.addAll(weapon.getLevelEffects(i));
                i++;
            }
            validEffect = validEffect.stream().filter(x->player.enoughAmmos(x.getExtraCost(),true)).collect(Collectors.toList());
            effectSelect = MessageHandler.chooseEffect(player,validEffect, room);
        }

    }


    /**
     * run actions let current player to changes position
     * @param player that do this action.
     * @param  distanceMax Max distance that the player can move
     */
    public static void run(Player player, int distanceMax, Room room) throws TimeFinishedException {
        Set<Square> validPositions = player.getPosition().getValidPosition(distanceMax);
        Square destination = MessageHandler.chooseSquare(player, validPositions, room);
        player.movePlayer(destination);
    }


    /**
     * basic grab method let player to grab all card that they can
     * @param player that do this action.
     * @param board that the player play.
     */
    public static void grab(Player player, Board board, Room room) throws NotExecutedException, TimeFinishedException {

        if (!player.getPosition().isGenerationPoint()){

            AmmoCard card = ((AmmoSquare) player.getPosition()).getAmmoCard();
            card.getAmmoList().forEach(player::addAmmo);

            if (card.hasPowerup() && player.getPowerups().size() < 3) {
                player.addPowerup((Powerup) board.getPowerDeck().getCard());
            }
            //put the card int the used card deck
            room.getBoard().getAmmoDeck().usedCard(card);
            //remove the card from the board
            ((AmmoSquare) player.getPosition()).removeAmmoCard();
        }
        else {

            GenerationSquare currentSquare = (GenerationSquare) player.getPosition();
            //takes only weapons that the player can pay
            List<Weapon> weapons=currentSquare.getWeaponDeck().stream().
                    filter(i->player.enoughAmmos(i.getBuyCost(),true))
                    .collect(Collectors.toList());


            if (!weapons.isEmpty()){
                //weapon to choose
                Weapon weapon = MessageHandler.chooseCard(weapons, true, room, true);

                if (weapon==null){
                    throw new NotExecutedException("No card has been chosen");
                }

                //pay
                try {
                    List<AmmoColor> cost = weapon.getBuyCost();
                    payment(player,cost, room);
                } catch (NotEnoughException e) {
                    throw new NotExecutedException("Not enough ammo to pay");
                }


                //player needs to swap cards if he has already 3
                if (player.limitWeapon()) {
                    //choose weapon to discard

                    Weapon discardWeapon = null;
                    try {
                        discardWeapon = MessageHandler.chooseCard(player.getWeapons(), false, room, true);
                    } catch (TimeFinishedException e) {
                        //TODO pay back function
                        //handle exception

                        throw new TimeFinishedException();
                    }

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
                throw new NotExecutedException("No available cards to choose");
            }

        }
    }

    /**
     * reload the weapon
     * @param player that do this action
     */
    public static void reload(Player player, Room room) throws TimeFinishedException {
        List<Weapon> weapons = player.getWeapons().stream().filter(x->!x.getCharged()).collect(Collectors.toList());
        weapons =  weapons.stream().filter(x->player.enoughAmmos(x.getChargeCost(),true)).collect(Collectors.toList());
        while (!weapons.isEmpty()) {
            Weapon weapon = MessageHandler.chooseCard(weapons, true, room, true);
            if (weapon == null)
                break;
            List<AmmoColor> cost = weapon.getChargeCost();

            try {
                payment(player,cost, room); //questa eccezione può far continuare
            } catch (NotEnoughException e) {
                //TODO send message
                //The player can continue to reload another
            }
            weapon.setCharged(true);
            weapons.remove(weapon);
        }
    }

    //payment method
    public static void payment(Player player, List<AmmoColor> cost, Room room) throws NotEnoughException, TimeFinishedException {

        List<AmmoColor> tempCost = new ArrayList<>(cost);
        if (cost.isEmpty()){
            return;
        }
        if (!player.enoughAmmos(tempCost, true)){
            throw new NotEnoughException("Not enough ammo to pay");
        }

        // put only powerups that he can use to pay
        List<Powerup> possiblePowerups = player.getPowerups().stream().filter(x->tempCost.contains(x.getAmmo())).collect(Collectors.toList());
        // the effective powerups need to pay
        List<Powerup> powerupToPay = new ArrayList<>();

        //the player has to choose a card if he has no ammo
        //the player can choose to not use a card if he has ammo

        while (!tempCost.isEmpty()) {

            //is optional only if has enough ammo to pay
            Powerup chosenCard = null;
            try {
                chosenCard = MessageHandler
                        .chooseCard(possiblePowerups, true, room, false);
            } catch (TimeFinishedException e) {
                //TODO dont pay (go back function)
                throw new TimeFinishedException();
            }

            //null means the player doesnt want to use powerups
            if (chosenCard != null) {
                //the chosen powerup will be used to pay
                powerupToPay.add(chosenCard);
                tempCost.remove(chosenCard.getAmmo());
                possiblePowerups.remove(chosenCard);
            }
            else {
                if (player.enoughAmmos(tempCost, false)) {
                    //pay the remaining with ammo instead
                    for (AmmoColor ammo : tempCost) {
                        try {
                            player.removeAmmo(ammo);
                            //this gives an error, dont need anyway
                            //tempCost.remove(ammo);
                        } catch (AmmoException e) {
                            throw new NotEnoughException(e.getMessage());
                        }
                        powerupToPay.forEach(x->player.getPowerups().remove(x));
                    }
                    tempCost.clear();
                }else
                    throw new NotEnoughException("have not enough ammo");
            }

        }
    }

}
