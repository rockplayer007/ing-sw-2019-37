package controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.board.*;
import model.exceptions.NotExecutedException;
import model.exceptions.TimeFinishedException;
import model.exceptions.TooManyPlayerException;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.*;
import network.server.ClientOnServer;
import network.server.Configs;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class that starts the game and sends messages to single or
 * multiple players and receives answers
 */

public class RoomController {

    private List<Player> players;
    private Map<Player, ClientOnServer> connectionToClient;

    private boolean isTest;
    private ClientToServer testMessage;

    private Room room;
    private ClientToServer mockMessage;
    private String expectedReceiver;
    private TurnController turnController;
    private boolean wait;
    private AtomicBoolean powerup;
    private static final int MAX_PLAYERS = Configs.getInstance().getMaximumPlayers();
    private static final int BOARD_TIME = Configs.getInstance().getBoardRequestTime();

    private static final Logger logger = Logger.getLogger(RoomController.class.getName());

    /**
     * Constructor that creats a new {@link Room} and a new {@link RoomController}
     */
    public RoomController() {
        room = new Room(this);
        players = new ArrayList<>();
        connectionToClient = new HashMap<>();
        turnController = new TurnController(this, room);
        wait = true;
        powerup = new AtomicBoolean(true);
        isTest = false;
    }

    /**
     * Method that recives the messages from the clients
     * @param message arrived message from the client
     */
    public void handleMessages(ClientToServer message) {

        if(checkReceiver(message)) {

            mockMessage = message;
            //askingThread.interrupt();
        }
        //otherwhise ignore the message

    }

    /**
     * Allows to add players from the {@link network.server.WaitingRoom}
     * @param client the client that needs to be added
     * @throws TooManyPlayerException checks that the players are not too many
     */
    public void addPlayer(ClientOnServer client) throws TooManyPlayerException {
        Player player = client.getPersonalPlayer();
        if (players.isEmpty()) {
            //room.setStartingPlayer(player);
            room.setCurrentPlayer(player);
        }
        if (players.size() < MAX_PLAYERS) {
            players.add(player);
            connectionToClient.put(player, client);
        } else
            throw new TooManyPlayerException("cant add the 6th player");
    }

    /**
     * Called when the game needs to be started. Asks a board and starts the player's match
     */
    public void matchSetup() {

        //add players to the room
        room.setPlayers(players);
        try {
            askBoard();

            turnController.startPlayerRound();

            //Gson gson = new Gson();

            //Map<Player, Integer> score = new HashMap<>();//room.endScoreboard();
            //HashMap<String, Integer> messageMap = new HashMap<>();
            List<String> scoreMessage = new ArrayList<>(everythingToJson(room.endScoreboard()));

            //score.forEach((x, y) -> messageMap.put(gson.toJson(x), y));

            sendMessageToAll(new AnswerRequest(scoreMessage, Message.Content.SCORE));
            //sendMessageToAll(new ScoreMessage(scoreMessage));

        } catch (NotExecutedException e) {
            sendMessageToAll(new InfoMessage(e.getMessage()));
        }

    }

    /**
     * Method that asks the board to the first player and then to the next once if they disconnect
     * @throws NotExecutedException thrown when there are not enough players to continue
     */
    private void askBoard() throws NotExecutedException {
        ServerToClient boardRequest = new BoardRequest(room.getBoardGenerator().getMaps());
        boolean ask = true;
        ListResponse boardMessage;

        while (ask){
            CountDown timer = new CountDown(BOARD_TIME, () -> {
                stopWaiting();
                logger.log(Level.INFO, "TIMER STOPPED FOR BOARD");
            });
            timer.startTimer();


            try {
                boardMessage = (ListResponse) sendAndReceive(room.getCurrentPlayer(), boardRequest);

                room.createMap(boardMessage.getSelectedItem());

                sendUpdate();

                logger.log(Level.INFO,"board is: {0}", (boardMessage.getSelectedItem()));
                resetReceiver();

                ask = false;

                timer.cancelTimer();

            } catch (TimeFinishedException e) {
                //set the player as disconnected
                room.getCurrentPlayer().setDisconnected();

                //continue as normal
                logger.log(Level.INFO,"player: {0} finished his time", room.getCurrentPlayer().getNickname());
                if(!room.setNextPlayer()){
                    InfoMessage infoMessage = new InfoMessage("You lost time, you lost the game...");
                    infoMessage.setConnection();
                    sendMessage(room.getCurrentPlayer(), infoMessage);

                    throw new NotExecutedException("Sorry " + room.getCurrentPlayer().getNickname()
                            + " just disconnected and there are not enough players anymore");
                }
                else{
                    sendMessage(room.getCurrentPlayer(), new TimeoutMessage());
                    sendMessageToAll(new InfoMessage(room.getCurrentPlayer().getNickname()
                            + " has disconnected (time exceeded)"));
                }
            }
            catch(IllegalStateException i){
                logger.log(Level.INFO,"ooops, timer already stopped, dont worry");
                //nothing, just continue
            }


        }


    }

    /**
     * Sends a single message to a client
     * @param player to send the message
     * @param message the message that needs to be sent
     */
    void sendMessage(Player player, ServerToClient message){
        if(isTest){
            forTestSend(message);
        }
        else {
            try{
                logger.log(Level.INFO, "Sending message to: {0}, for {1}",
                        new String[]{player.getNickname(), String.valueOf(message.getContent())});

                connectionToClient.get(player).getClientInterface()
                        .notifyClient(message);
            } catch (RemoteException e) {
                logger.log(Level.WARNING, "disconnection client: " + player.getNickname());

                if(player.isConnected()){
                    disconnectPlayer(player);
                    //logger.log(Level.WARNING, "Player {0} disconnected", player.getNickname());

                    sendMessageToAll(new InfoMessage(player.getNickname() + " not online anymore"));
                }

            }
        }

    }

    /**
     * Sends a message to all connected players
     * @param message the message tha needs to be sent to everybody
     */
    void sendMessageToAll(ServerToClient message){
        //send the message only to who is connected
        players.stream().filter(Player::isConnected).forEach(x -> sendMessage(x, message));
    }

    /**
     * send an update of the game to all connected players
     */
    public void sendUpdate(){
        //send a message that includes the board and other things

        //sendMessageToAll(new BoardInfo(gson.toJson(room.getBoard().getMap())));
        players.stream().filter(Player::isConnected).forEach(this::sendUpdate);

    }

    /**
     * send an update of the game to one player players
     * @param player who needs to be updated
     */
    public void sendUpdate(Player player){
        //send a message that includes the board and other things
        sendMessage(player, new UpdateMessage(
                    toJsonGameBoard(), everythingToJson(player.getPowerups()), toJsonSkullBoard()));

    }

    /**
     * Checks if the message that arrived from the client is comes from the correct client
     * @param message that contains the sender
     * @return true if the receiver is the same as expected else is false
     */
    private boolean checkReceiver(ClientToServer message) {
        //still no idea how to check this
        if ((message.getSender().equals(expectedReceiver) /*&& message.getContent() == expectedType*/)){
            return true;
        }
        else{
            logger.log(Level.INFO, "Discarted message from {0} expected from {1}", new String[]{message.getSender(), expectedReceiver});
            return false;
        }
    }

    /**
     * Allows to send a message and wait for a response
     * @param player to send the {@link Message}
     * @param message that has to be sent to the {@link Player}
     * @return the message from the client
     * @throws TimeFinishedException when the player finishes the time to choose
     */
    ClientToServer sendAndReceive(Player player, ServerToClient message) throws TimeFinishedException {
        mockMessage = null;
        expectedReceiver = player.getNickname();
        //expectedType = message.getContent();

        //timeout happens before sending
        if(!wait){
            resetReceiver();

            wait = true;

            throw new TimeFinishedException();
        }
        sendMessage(player, message);

        //dont wait for the sending when wait is false
        while (mockMessage == null && wait){
            Thread.onSpinWait();
        }


        //timeout happens after sending
        if (!wait /*wait == false*/){
            resetReceiver();
            wait = true;

            throw new TimeFinishedException();
        }

        ClientToServer answer = mockMessage;
        resetReceiver();

        return answer;
    }

    /**
     * Sends a message for using the TAGBACK GRANADE
     * @param player who has to use the card
     * @param message with the possible cards to use
     * @return the selected card and if the waiting parameter is triggered returns a special response
     */
    ListResponse tagBack(Player player, ServerToClient message){
        mockMessage = null;
        expectedReceiver = player.getNickname();
        powerup.set(true);
        sendMessage(player, message);
        while (mockMessage == null && powerup.get()){
            Thread.onSpinWait();
        }

        //if it has to stop waiting for the powerup set it back to true and return this response
        if(powerup.compareAndSet(false, true) /*powerup == false*/){
            //powerup = true;
            return new ListResponse("", "", -1, Message.Content.CARD_RESPONSE);
        }

        ListResponse answer = (ListResponse) mockMessage;
        resetReceiver();

        return answer;
    }

    /**
     * If there was a waiting, it will be stopped
     */
    void stopWaiting(){
        wait = false;
    }

    /**
     * If there was a waiting for the powerup, it will be stopped
     */
    void stopPowerup(){
        powerup.set(false);
        //powerup = false;
    }

    /**
     * resets the arriving message and the receiver
     */
    private void resetReceiver(){
        mockMessage = null;
        //not used for now
        //expectedType = null;
        expectedReceiver = null;
    }

    /**
     * Disconnects the player by name
     * @param playerName name of the player that needs to be disconnected
     */
    public void disconnectPlayer(String playerName){
        for(Player player : players){
            if(player.getNickname().equals(playerName)){
                disconnectPlayer(player);
                break;
            }
        }
    }

    /**
     * Disconnects a given {@link Player}
     * @param player that will be disconnected
     */
    private void disconnectPlayer(Player player){
        player.setDisconnected();
        if(expectedReceiver != null && expectedReceiver.equals(player.getNickname())){
            stopWaiting();
        }
        logger.log(Level.WARNING, "Player {0} disconnected", player.getNickname());
    }

    /**
     * General method to transform a list into a string list
     * @param elements every kind of list that needs to be transformed into a json list
     * @param <T> general parameter
     * @return a list with json elements
     */
    <T> List<String> everythingToJson(List<T> elements){
    //public List<String> everythingToJson(List<? extends Card> cards){
        List<String> list = new ArrayList<>();
        Gson gson = new Gson();

        elements.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    /**
     * method to transform a square set into a json list
     * @param squares that needs to be transformed
     * @return list of json elements
     */
    List<String> everythingToJson(Set<Square> squares){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        squares.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    /**
     * Allows to to create a special json without breaking internal calls
     * @return json element
     */
    private String toJsonGameBoard(){
        RuntimeTypeAdapterFactory<Square> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Square.class, "Square")
                .registerSubtype(AmmoSquare.class, "AmmoSquare")
                .registerSubtype(GenerationSquare.class, "GenerationSquare");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();
        return gson.toJson(room.getBoard().getMap());
    }

    /**
     * Method to transform the {@link SkullBoard} into a json element
     * @return a json element
     */
    private String toJsonSkullBoard(){
        Gson gson = new Gson();
        return gson.toJson(room.getBoard().getSkullBoard());
    }

    /**
     * gives the current {@link Room}
     * @return the room where the game is playing
     */
    public Room getRoom(){
        return room;
    }


    ////////classes for making tests possible/////////////////
    private void forTestSend(ServerToClient message){
        //stopPowerup();
        //stopWaiting();
        if(isTest){
            int size;
            if(room.getBoard().getMap() == null){
                size = 1;
                mockMessage = new ListResponse(
                        "", "", new Random().
                        nextInt(size), Message.Content.CARD_RESPONSE );
            }
            else {
                if(testMessage != null){
                    mockMessage = testMessage;
                }
                else {
                    stopWaiting();
                    stopPowerup();
                }

            }


        }


    }

    public void setTestMockMessage(int i){
        testMessage = new ListResponse(
                "", "",
                i, Message.Content.CARD_RESPONSE );
    }

    void setTest(){
        isTest = true;
    }

    TurnController getTurnController(){
        return turnController;
    }

}