package controller;

import model.board.*;
import model.card.*;

import model.exceptions.TimeFinishedException;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;
import network.messages.serverToClient.AttackMessage;
import network.messages.serverToClient.InfoMessage;
import network.messages.serverToClient.ServerToClient;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * contain the min actions t
 */
public class MessageHandler {

    private static final Logger logger = Logger.getLogger(MessageHandler.class.getName());

    private MessageHandler(){
    throw new IllegalStateException("Utility class");
    }


    /**
     *  general way to let player chooses the Squere that he can go
     * @param player current player
     * @param  validPositions all square that you can choose.
     * @return the Square that the player choose to move
     */
    public static Square chooseSquare(Player player,Set<Square> validPositions, Room room) throws TimeFinishedException {
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

    // if effects isempty return null.
    public static Effect chooseEffect(Player player, List<Effect> effects, Room room) throws TimeFinishedException {
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

    public static List<Player> choosePlayers(Player player, List<Player> possiblePlayers, int maxPlayerToChoose, Room room) throws TimeFinishedException {

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
                possiblePlayers.remove(chosenPlayer.getSelectedItem());
            }catch (RuntimeException e){
                //cheater
                logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
                return null;
            }
        }
        return playersToAttack;
    }

    public static Square.Direction chooseDirection(Player player, List<Square.Direction> directions, Room room) throws TimeFinishedException {
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

    public static AmmoColor chooseAmmoColor(Player player, List<AmmoColor> ammo, Room room) throws TimeFinishedException {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonAmmoColorList(ammo);

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

    public static Color chooseRoom(Player player, List<Color> rooms, Room room) throws TimeFinishedException {
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
     * general way to let player chooses the cards he wants to use
     * @param cards cards that need to choose
     * @param isOptional if true the player can decide wheather to use the card or not
     * @return position of card choose in the List
     */

    public static <T extends Card> T chooseCard(List<T> cards, boolean isOptional, Room room, boolean isWeapon) throws TimeFinishedException {
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

    public static void sendInfo(Player player, String info, Room room){
        ServerToClient message = new InfoMessage(info);
        room.getRoomController().sendMessage(player, message);
    }

    public static void sendAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks, Room room){
        ServerToClient message = new AttackMessage(attacker, hp, marks);
        room.getRoomController().sendMessageToAll(message);
    }
}
