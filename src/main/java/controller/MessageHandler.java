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
 * Contains methods to send specific messages
 */
public class MessageHandler {

    private static final String CHEATER = "CHEATER DETECTED: {0}";
    private static final Logger logger = Logger.getLogger(MessageHandler.class.getName());

    private MessageHandler(){
    throw new IllegalStateException("Utility class");
    }


    /**
     * Sends messages to allow to choose a {@link Square}
     * @param player player that has to choose the square
     * @param validPositions all possible positions where the player can choose from
     * @param room the room where the player is playing
     * @param reason a string explaining why the player has to choose
     * @return the square that the player has chosen
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static Square chooseSquare(Player player,Set<Square> validPositions, Room room, String reason) throws TimeFinishedException {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .everythingToJson(validPositions);
        AnswerRequest message = new AnswerRequest(send, Message.Content.SQUARE_REQUEST);
        message.setInfo(reason);
        ListResponse square = (ListResponse) roomController
                .sendAndReceive(player, message);

        List<Square> tempSquares;
        try{
            //needed to convert set into the array
            tempSquares = new ArrayList<>(validPositions);
            return tempSquares.get(square.getSelectedItem());
        }catch (RuntimeException e){
            //CHEATER
            logger.log(Level.WARNING, CHEATER, player.getNickname());

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
    static Effect chooseEffect(Player player, List<Effect> effects, Room room) throws TimeFinishedException {
        if(effects.isEmpty()){
            return null;
        }

        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .everythingToJson(effects);

        AnswerRequest message = new AnswerRequest(send, Message.Content.EFFECT_REQUEST);
        message.setIsOptional();
        ListResponse effect = (ListResponse) roomController
                .sendAndReceive(player, message);

        if (effect.getSelectedItem() < effects.size() && effect.getSelectedItem() >= 0){
            //the chosen powerup will be executed
            return effects.get(effect.getSelectedItem());
        }
        else{
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
    public static List<Player> choosePlayers(Player player, List<Player> possiblePlayers, int maxPlayerToChoose, Room room, String info) throws TimeFinishedException {

        List<Player> playersToAttack = new ArrayList<>();
        int askIterations = (maxPlayerToChoose < possiblePlayers.size() ? maxPlayerToChoose : possiblePlayers.size());

        RoomController roomController = room.getRoomController();

        for (int i = 0; i < askIterations; i++){

            List<String> send = roomController
                    .everythingToJson(possiblePlayers);
            AnswerRequest message = new AnswerRequest(send, Message.Content.PLAYER_REQUEST);
            message.setInfo(info);
            ListResponse chosenPlayer = (ListResponse) roomController
                    .sendAndReceive(player, message);


            try{
                playersToAttack.add(possiblePlayers.get(chosenPlayer.getSelectedItem()));
                possiblePlayers.remove(chosenPlayer.getSelectedItem());
            }catch (RuntimeException e){
                //CHEATER
                logger.log(Level.WARNING, CHEATER, player.getNickname());
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
     * @param info reason for choosing a direction
     * @return the chosen direction
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    public static Square.Direction chooseDirection(Player player, List<Square.Direction> directions, Room room, String info) throws TimeFinishedException {
        RoomController roomController = room.getRoomController();
        List<String> send = roomController
                .everythingToJson(directions);

        AnswerRequest message = new AnswerRequest(send, Message.Content.DIRECTION_REQUEST);
        message.setInfo(info);
        ListResponse direction = (ListResponse) roomController
                .sendAndReceive(player, message);

        try{
            return directions.get(direction.getSelectedItem());
        }catch (RuntimeException e){
            //CHEATER
            logger.log(Level.WARNING, CHEATER, player.getNickname());
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
                .everythingToJson(ammo);

        ListResponse chosenAmmo = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.AMMOCOLOR_REQUEST));

        try{
            return ammo.get(chosenAmmo.getSelectedItem());
        }catch (RuntimeException e){
            //CHEATER
            logger.log(Level.WARNING, CHEATER, player.getNickname());
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
                .everythingToJson(rooms);

        ListResponse chosenRoom = (ListResponse) roomController
                .sendAndReceive(player, new AnswerRequest(send, Message.Content.ROOM_REQUEST));

        try{
            return rooms.get(chosenRoom.getSelectedItem());
        }catch (RuntimeException e){
            //CHEATER
            logger.log(Level.WARNING, CHEATER, player.getNickname());
            return null;
        }
    }

    /**
     * general way to let player choose the cards he wants to use
     * @param cards cards that need to choose
     * @param isOptional if true the player can decide whether to use the card or not
     * @param room wherethe player is playing
     * @param isWeapon indicates if the card is a weapon or a powerup
     * @param reason a string explaining why the player needs to choose a card
     * @return position of card choose in the List
     * @throws TimeFinishedException when the client finishes the time for choosing
     */
    static <T extends Card> T chooseCard(List<T> cards, boolean isOptional, Room room, boolean isWeapon, String reason) throws TimeFinishedException {
        if (cards.isEmpty())
            return null;
        AnswerRequest message = new AnswerRequest(room
                .getRoomController()
                .everythingToJson(cards),
                //send message corresponding to the request
                isWeapon ? Message.Content.WEAPON_REQUEST : Message.Content.POWERUP_REQUEST);

        message.setInfo(reason);

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
                //is a CHEATER
                logger.log(Level.WARNING, CHEATER, room.getCurrentPlayer().getNickname());
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
    static void sendInfo(Player player, String info, Room room){
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
    static void sendAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks, Room room){
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
