package controller;

import com.google.gson.Gson;
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
        ServerToClient message = new AnswerRequest(send, Message.Content.SQUARE_REQUEST, "Choose a square");
        ListResponse square = (ListResponse) roomController
                .sendAndReceive(player, message);

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
     * method for choosing an {@link Effect}
     * @param player player that needs to choose
     * @param effects possible effects that the player can choose
     * @param room where the player is playing
     * @return the effect that the player has chosen
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static Effect chooseEffect(Player player, List<Effect> effects, Room room) throws TimeFinishedException {
        if(effects.isEmpty()){
            return null;
        }

        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonEffectList(effects);

        ListResponse effect = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.EFFECT_REQUEST, "Choose an effect"));

        try{
            return effects.get(effect.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }

    }

    /**
     * method for choosing one or more {@link Player}
     * @param player player that needs to choose
     * @param possiblePlayers possible players that the current player needs to choose
     * @param maxPlayerToChoose maximum number of players that he can choose
     * @param room where the player is playing
     * @return the players that the player has chosen
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static List<Player> choosePlayers(Player player, List<Player> possiblePlayers, int maxPlayerToChoose, Room room) throws TimeFinishedException {

        List<Player> playersToAttack = new ArrayList<>();
        int askIterations = (maxPlayerToChoose < possiblePlayers.size() ? maxPlayerToChoose : possiblePlayers.size());

        RoomController roomController = room.getRoomController();

        for (int i = 0; i < askIterations; i++){

            List<String> send = roomController
                    .toJsonPlayerList(possiblePlayers);
            ListResponse chosenPlayer = (ListResponse) roomController
                    .sendAndReceive(player, new AnswerRequest(send, Message.Content.PLAYER_REQUEST, "Choose a player"));

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

    /**
     * method for choosing a {@link model.board.Square.Direction}
     * @param player player that needs to choose
     * @param directions possible directions that the current player can choose from
     * @param room where the player is playing
     * @return the chosen direction
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static Square.Direction chooseDirection(Player player, List<Square.Direction> directions, Room room) throws TimeFinishedException {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonDirectionList(directions);

        ListResponse direction = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.DIRECTION_REQUEST, "Choose a direction"));

        try{
            return directions.get(direction.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }
    }

    /**
     * method for choosing an {@link AmmoColor} when he can choose how to pay
     * @param player player that needs to choose
     * @param ammo possible ammo colors that he can choose from
     * @param room where the player is playing
     * @return the chosen ammo color
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static AmmoColor chooseAmmoColor(Player player, List<AmmoColor> ammo, Room room) throws TimeFinishedException {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonAmmoColorList(ammo);

        ListResponse chosenAmmo = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.AMMOCOLOR_REQUEST, "Choose an ammo color"));

        try{
            return ammo.get(chosenAmmo.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }
    }

    /**
     * method for choosing a room where to use a specific card
     * @param player player that needs to choose
     * @param rooms possible room colors that he can choose from
     * @param room where the player is playing
     * @return the color of the chosen room
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static Color chooseRoom(Player player, List<Color> rooms, Room room) throws TimeFinishedException {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .toJsonColorList(rooms);

        ListResponse chosenRoom = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.ROOM_REQUEST, "Choose a room"));

        try{
            return rooms.get(chosenRoom.getSelectedItem());
        }catch (RuntimeException e){
            //cheater
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", player.getNickname());
            return null;
        }
    }

    /**
     * general way to let player choose the cards he wants to use
     * @param cards cards that need to choose
     * @param isOptional if true the player can decide whether to use the card or not
     * @param room wherethe player is playing
     * @param isWeapon indicates if the card is a weapon or a powerup
     * @return position of card choose in the List
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static <T extends Card> T chooseCard(List<T> cards, boolean isOptional, Room room, boolean isWeapon) throws TimeFinishedException {
        if (cards.isEmpty())
            return null;
        AnswerRequest message = new AnswerRequest(room
                .getRoomController()
                .toJsonCardList(cards),
                //send message corresponding to the request
                isWeapon ? Message.Content.WEAPON_REQUEST : Message.Content.POWERUP_REQUEST, "Choose a card");
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

    /**
     * sends a general message with just a string of the information
     * @param player to send the message
     * @param info the information to send to the player
     * @param room where the player is playing
     */
    public static void sendInfo(Player player, String info, Room room){
        ServerToClient message = new InfoMessage(info);
        room.getRoomController().sendMessage(player, message);
    }

    /**
     * sends a message with all the attack's details
     * @param attacker the player that made the attack
     * @param hp points given to the players
     * @param marks marks given to the players
     * @param room room where they are playing
     */
    public static void sendAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks, Room room){
        Gson gson = new Gson();

        //couldn't find any other way to make it work
        HashMap<String, Integer> sHp = new HashMap<>();
        hp.forEach((x, y) -> sHp.put(gson.toJson(x), y));

        HashMap<String, Integer> sMarks = new HashMap<>();
        marks.forEach((x, y) -> sMarks.put(gson.toJson(x), y));

        ServerToClient message = new AttackMessage(gson.toJson(attacker), gson.toJson(sHp), gson.toJson(sMarks));
        room.getRoomController().sendMessageToAll(message);
    }
}
