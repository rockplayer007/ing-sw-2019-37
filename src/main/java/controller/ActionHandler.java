package controller;

import model.board.AmmoSquare;
import model.board.Board;
import model.board.GenerationSquare;
import model.board.Square;
import model.card.*;

import model.exceptions.*;
import model.gamehandler.AttackHandler;
import model.gamehandler.PaymentRecord;
import model.gamehandler.Room;
import model.player.Player;

import java.util.*;
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
                room.undoPayment();         // do undo payment
                if (!used) {                // if the weapon is not used do undo position for player and throw exception
                    player.movePlayer(playerPosition);
                    throw new NotExecutedException("Effect is not possible used, "+e.getMessage());
                }else
                    MessageHandler.sendInfo(player,e.getMessage(),room);
            } catch (NotEnoughException e) {
                MessageHandler.sendInfo(player,"you can not use this effect:"+e.getMessage(),room); // send message to player
            } catch (InterruptOperationException e) {
                MessageHandler.sendInfo(player,e.getMessage(),room); // send message to player
            }

            if (validEffect.isEmpty()|| validEffect.stream().allMatch(x->effects.get(x)==-1)){
                validEffect.addAll(weapon.getLevelEffects(i));
                i++;
            }
            validEffect = validEffect.stream().filter(x->player.enoughAmmos(x.getExtraCost(),true)).collect(Collectors.toList());
            effectSelect = MessageHandler.chooseEffect(player,validEffect, room);
        }
        MessageHandler.sendAttack(player,room.getAttackHandler().getDamaged(),room.getAttackHandler().getMarked(),room);
    }


    /**
     * run actions let current player to changes position
     * @param player that do this action.
     * @param  distanceMax Max distance that the player can move
     * @param room needed for choosing running square
     */
    public static void run(Player player, int distanceMax, Room room) throws TimeFinishedException {
        Set<Square> validPositions = player.getPosition().getValidPosition(distanceMax);
        Square destination = MessageHandler.chooseSquare(player, validPositions, room,"Which square do you like to move?");
        player.movePlayer(destination);
    }


    /**
     * basic grab method let player to grab what is on that square
     * @param player that do this action.
     * @param board that the player play.
     * @param room used to fill the square with new cards
     */
    public static void grab(Player player, Board board, Room room) throws NotExecutedException, TimeFinishedException {

        if (!player.getPosition().isGenerationPoint()){

            if(((AmmoSquare) player.getPosition()).getAmmoCard() == null){
                //in case there is no ammo card in the square
                throw new NotExecutedException("Nothing to grab here");
            }
            AmmoCard card = ((AmmoSquare) player.getPosition()).getAmmoCard();
            card.getAmmoList().forEach(player::addAmmo);

            if (card.hasPowerup() && player.getPowerups().size() < 3) {
                player.addPowerup(board.getPowerDeck().getCard());
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
                Weapon weapon = MessageHandler.chooseCard(weapons, true, room, true,
                        "Take a weapon!");

                if (weapon==null){
                    throw new NotExecutedException("You didn't choose any card...");
                }

                //pay
                try {
                    List<AmmoColor> cost = weapon.getBuyCost();
                    payment(player,cost, room);
                } catch (NotEnoughException e) {
                    throw new NotExecutedException("You don't have enough ammo to pay!");
                }


                //player needs to swap cards if he has already 3
                if (player.limitWeapon()) {
                    //choose weapon to discard

                    Weapon discardWeapon;
                    try {
                        discardWeapon = MessageHandler.chooseCard(player.getWeapons(), false, room, true,
                                "You have too many weapons, leave one!");
                    } catch (TimeFinishedException e) {

                        room.undoPayment();
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
                    //remove the card from the board and add it to the player
                    currentSquare.getWeaponDeck().remove(weapon);
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
     * @param room needed for the payment method
     */
    public static void reload(Player player, Room room) throws TimeFinishedException {
        List<Weapon> weapons = player.getWeapons().stream().filter(x->!x.getCharged()).collect(Collectors.toList());
        weapons =  weapons.stream().filter(x->player.enoughAmmos(x.getChargeCost(),true)).collect(Collectors.toList());
        while (!weapons.isEmpty()) {
            Weapon weapon = MessageHandler.chooseCard(weapons, true, room, true,
                    "Want to reload one of your weapons?");
            if (weapon == null)
                break;
            List<AmmoColor> cost = weapon.getChargeCost();

            try {
                payment(player,cost, room); //questa eccezione può far continuare
                weapon.setCharged(true);
                weapons.remove(weapon);
            } catch (NotEnoughException e) {
                //The player can continue to reload another
            }
            weapons =  weapons.stream().filter(x->player.enoughAmmos(x.getChargeCost(),true)).collect(Collectors.toList());
        }
    }

    /**
     * Allows the player to pay with ammo ro with powerups
     * @param player player that needs to pay
     * @param cost amount of ammo to pay
     * @param room room where the player is in
     * @throws NotEnoughException when the player doesn't have enough ammo to pay
     * @throws TimeFinishedException when the client takes too long for choosing
     */
    public static void payment(Player player, List<AmmoColor> cost, Room room) throws NotEnoughException, TimeFinishedException {
        room.setPaymentRecord(new PaymentRecord(Collections.emptyList(),Collections.emptyList()));
        List<AmmoColor> tempCost = new ArrayList<>(cost);
        if (cost.isEmpty()){
            return;
        }
        if (!player.enoughAmmos(tempCost, true)){
            throw new NotEnoughException("Not enough ammo to pay");
        }

        PowerDeck powerDeck =room.getBoard().getPowerDeck();
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
                        .chooseCard(possiblePowerups, true, room, false,
                                "Want to pay with one of your powerups?");
            } catch (TimeFinishedException e) {
                throw new TimeFinishedException();
            }

            //null means the player doesnt want to use powerups
            if (chosenCard != null) {
                //the chosen powerup will be used to pay
                powerupToPay.add(chosenCard);
                tempCost.remove(chosenCard.getAmmo());
                possiblePowerups.remove(chosenCard);
            }
            else
                break;
        }
        room.setPaymentRecord(new PaymentRecord(powerupToPay,tempCost));
        if (player.enoughAmmos(tempCost, false)) {
            //pay the remaining with ammo instead
            for (AmmoColor ammo : tempCost) {
                try {
                    player.removeAmmo(ammo);
                } catch (AmmoException e) {
                    throw new NotEnoughException(e.getMessage());
                }
            }
            powerupToPay.forEach(x->{
                player.removePowerup(x);
                powerDeck.usedCard(x);
                room.getBoard().getPowerDeck().usedCard(x);
            });

        }else
            throw new NotEnoughException("have not enough ammo");
    }

}
