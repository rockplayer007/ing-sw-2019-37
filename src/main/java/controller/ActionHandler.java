package controller;

import model.board.AmmoSquare;
import model.board.Board;
import model.board.GenerationSquare;
import model.board.Square;
import model.card.*;

import model.exceptions.InterruptOperationException;
import model.exceptions.NotEnoughException;
import model.exceptions.NotExecutedExeption;
import model.exceptions.NullTargetsException;
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
        //TODO inizializza attacHandler
        Map<Effect,Integer> effects = weapon.getEffects();
        Effect effectSelect;
        Player player=room.getCurrentPlayer();
        List<Effect> validEffect = new ArrayList<>(weapon.getLevelEffects(-1));
        validEffect.addAll(weapon.getLevelEffects(0));
        effectSelect = chooseEffects(player,validEffect);
        int i = 1;
        while (effectSelect!=null){
            validEffect.remove(effectSelect);
            try {
                decuction(player,effectSelect.getExtraCost());
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
            effectSelect = chooseEffects(player,validEffect);
        }
        weapon.setCharged(false);

    }

    // if effects isempty return null.
    public static Effect chooseEffects(Player player,List<Effect> effects){
        if(effects.isEmpty()){
            return null;
        }
        //TODO



        return null;
    }

    /**
     * basic grab method let player to grab all card that they can
     * @param player that do this action.
     * @param board that the player play.
     */
    public static void grab( Player player,Board board) {
        if (!player.getPosition().isGenerationPoint())
            grabAmmo(player,((AmmoSquare) player.getPosition()).getAmmoCard(),board);
        else {
            List<Weapon> weapons=((GenerationSquare) player.getPosition()).getWeaponDeck().stream().
                    filter(i->player.enoughAmmos(i.getBuyCost(),true))
                    .collect(Collectors.toList());
            if (!weapons.isEmpty()){
                Weapon weapon = chooseCard(weapons,"to grab");
                if (weapon==null)
                    return;
                if (player.limitWeapon()) {
                    Weapon changeWeapon = chooseCard(player.getWeapons(), "change");
                    if (changeWeapon==null)
                        return;
                    changeWeapon.setCharged(true);
                    ((GenerationSquare) player.getPosition()).addWeapon(changeWeapon);
                    player.getWeapons().remove(changeWeapon);
                }
                try {
                    grabWeapon(player, weapon);
                } catch (NotEnoughException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * general way to let player chooses the cards he wants to use
     * @param cards cards that need to choose
     * @param reason why go to this choose
     * @return position of card choose in the List
     */

    public static <T extends Card> T chooseCard(List<T> cards, String reason) {
//        TODO
        return null;
    }

    /**
     * add WeaponCard in player only if he has less than 3 weapons
     * @param player current player
     * @param  card weapon need to add in the player's hand
     */
    private static void grabWeapon(Player player, Weapon card) throws NotEnoughException{
        List<AmmoColor> cost = card.getChargeCost();
        decuction(player,cost);
        player.addWeapon(card);
    }

    public static void decuction(Player player,List<AmmoColor> cost) throws NotEnoughException {
        if (cost.isEmpty())
            return;
        Powerup powerup;
        int c=0;
        List<Powerup> temp = new ArrayList<>();
        List<Powerup> powerups = player.getPowerups();
        if (player.enoughAmmos(cost, true)) {
            while (cost.stream().distinct().anyMatch(player::usePowerupAsAmmo)) {//if the player has the powerups that can use as ammo
                powerup = chooseCard(powerups, "use as ammo");
                if (powerup==null)
                    break;
                cost.remove(powerup.getAmmo());
                temp.add(powerup);
                c++;
            }
            if (player.enoughAmmos(cost, false)) {
                temp.forEach(powerups::remove);
                cost.forEach(player::removeAmmo);
            } else
                throw new NotEnoughException("haven't enough ammo.");

        }
    }

    /**
     * grab AmmoCard and add the Ammos and Powerup in player
     * @param player current player
     * @param  card Ammocard need to analize
     */

    public static void grabAmmo(Player player, AmmoCard card, Board board) {
        card.getAmmoList().forEach(player::addAmmo);
        if (card.hasPowerup() && player.getPowerups().size() < 3) {
            player.addPowerup((Powerup) board.getPowerDeck().getCard());
        }
    }


    /**
     * reload the weapon
     * @param player that do this action
     */
    public static void reload(Player player) {
        List<Weapon> weapons=player.getWeapons().stream().filter(x->!x.getCharged()).collect(Collectors.toList());
        while (!weapons.isEmpty()) {
            Weapon weapon = chooseCard(weapons, "charge");
                if (weapon==null)
                    break;
                List<AmmoColor> cost = weapon.getChargeCost();
                try {
                    decuction(player,cost);
                    weapon.setCharged(true);
                    continue;
                } catch (NotEnoughException e) {
                    e.printStackTrace();
                }
        }

    }







}
