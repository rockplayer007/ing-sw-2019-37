package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.board.*;
import model.card.AmmoColor;
import model.card.Card;
import model.card.Effect;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomController {

    private List<Player> players;
    private Map<Player, ClientOnServer> connectionToClient;

    private Room room;
    private ClientToServer mockMessage;
    private String expectedReceiver;
    private Message.Content expectedType;
    private Thread askingThread;
    private TurnController turnController;
    private boolean wait;
    private boolean powerup;
    private static final int MAX_PLAYERS = Configs.getInstance().getMaximumPlayers();
    private static final int BOARD_TIME = Configs.getInstance().getBoardRequestTime();

    private static final Logger logger = Logger.getLogger(RoomController.class.getName());


    public RoomController() {
        room = new Room(this);
        players = new ArrayList<>();
        connectionToClient = new HashMap<>();
        turnController = new TurnController(this, room);
        wait = true;
        powerup = true;
    }

    public void handleMessages(ClientToServer message) {


        if(checkReceiver(message)) {

            mockMessage = message;
            //askingThread.interrupt();
        }
        else {
            System.out.println("CHEATER");
        }

    }


    //needed for starting a new room from waitingRoom
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

    public void matchSetup() {

        //add players to the room
        room.setPlayers(players);
        try {
            askBoard();

            turnController.startPlayerRound();

            Gson gson = new Gson();
            Map<Player, Integer> score = room.endScoreboard();
            HashMap<String, Integer> messageMap = new HashMap<>();
            score.forEach((x, y) -> messageMap.put(gson.toJson(x), y));

            sendMessageToAll(new ScoreMessage(messageMap));

        } catch (NotExecutedException e) {
            sendMessageToAll(new InfoMessage(e.getMessage()));
        }

    }


    private void askBoard() throws NotExecutedException {
        ServerToClient boardRequest = new BoardRequest(room.getBoardGenerator().getMaps());
        boolean ask = true;
        ListResponse boardMessage;

        while (ask){
            CountDown timer = new CountDown(BOARD_TIME, () -> {
                stopWaiting();
                System.out.println("timer stopped");
            });
            timer.startTimer();


            try {
                boardMessage = (ListResponse) sendAndReceive(room.getCurrentPlayer(), boardRequest);

                room.createMap(boardMessage.getSelectedItem());

                sendUpdate();

                logger.log(Level.INFO,"board is: {0}", (boardMessage.getSelectedItem()));
                resetReceiver();

                ask = false;

                try{
                    timer.cancelTimer();
                }catch (IllegalStateException e){
                    System.out.println("ooops, timer already stopped, dont worry");
                    //nothing, just continue
                }
            } catch (TimeFinishedException e) {
                //set the player as disconnected
                room.getCurrentPlayer().setDisconnected();

                //send message
                sendMessage(room.getCurrentPlayer(), new TimeoutMessage());
                sendMessageToAll(new InfoMessage(room.getCurrentPlayer().getNickname() + " has disconnected (time exceeded)"));

                //continue as normal
                logger.log(Level.INFO,"player: {0} finished his time", room.getCurrentPlayer().getNickname());
                if(!room.setNextPlayer()){
                    throw new NotExecutedException("Sorry, not enough players anymore");
                }
            }
        }


    }

        public void sendMessage(Player player, ServerToClient message){
        try{
            logger.log(Level.INFO, "Sending message to: {0}, for {1}",
                    new String[]{player.getNickname(), String.valueOf(message.getContent())});

            connectionToClient.get(player).getClientInterface()
                    .notifyClient(message);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "disconnection", e);

            if(player.isConnected()){
                disconnectPlayer(player);
                //logger.log(Level.WARNING, "Player {0} disconnected", player.getNickname());
                //TODO send message to all others
                sendMessageToAll(new InfoMessage(player.getNickname() + " not online anymore"));
            }

        }

    }


    public void sendMessageToAll(ServerToClient message){
        //send the message only to who is connected
        players.stream().filter(Player::isConnected).forEach(x -> sendMessage(x, message));
    }

    public void sendUpdate(){
        //send a message that includes the board and other things

        //sendMessageToAll(new BoardInfo(gson.toJson(room.getBoard().getMap())));
        players.stream().filter(Player::isConnected).forEach(p ->
                sendMessage(p, new UpdateMessage(
                toJsonGameBoard(), toJsonCardList(p.getPowerups()), toJsonSkullBoard())));

    }

    public boolean checkReceiver(ClientToServer message) {
        //still no idea how to check this
        if ((message.getSender().equals(expectedReceiver) /*&& message.getContent() == expectedType*/)){
            return true;
        }
        else{
            logger.log(Level.INFO, "Discarted message from {0} expected from {1}", new String[]{message.getSender(), expectedReceiver});
            return false;
        }
    }

    public ClientToServer sendAndReceive(Player player, ServerToClient message) throws TimeFinishedException {
        mockMessage = null;
        expectedReceiver = player.getNickname();
        //expectedType = message.getContent();

        //timeout happens before sending
        if(wait == false){
            resetReceiver();

            wait = true;

            throw new TimeFinishedException();
        }
        sendMessage(player, message);

        //dont wait for the sending when wait is false
        while (mockMessage == null && wait && powerup){
            Thread.onSpinWait();
            //System.out.println("waiting");
        }
        if(powerup == false){
            powerup = true;
            return new ListResponse("", "", -1, Message.Content.CARD_RESPONSE);
        }

        //timeout happens after sending
        if (wait == false){
            resetReceiver();
            wait = true;

            throw new TimeFinishedException();
        }

        ClientToServer answer = mockMessage;
        resetReceiver();

        return answer;
    }

    public void stopWaiting(){
        wait = false;
    }

    public void stopPowerup(){
        powerup = false;
    }

    public void resetReceiver(){
        mockMessage = null;
        expectedType = null;
        expectedReceiver = null;
    }

    public void disconnectPlayer(String playerName){
        for(Player player : players){
            if(player.getNickname().equals(playerName)){
                disconnectPlayer(player);
            }
            break;
        }
    }

    public void disconnectPlayer(Player player){
        player.setDisconnected();
        if(expectedReceiver != null && expectedReceiver.equals(player.getNickname())){
            stopWaiting();
        }
        logger.log(Level.WARNING, "Player {0} disconnected", player.getNickname());
    }


    //TODO check this
    //public <T> List<String> toJsonCardList(List<T> cards){
    public List<String> toJsonCardList(List<? extends Card> cards){
        List<String> list = new ArrayList<>();
        Gson gson = new Gson();

        cards.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public List<String> toJsonSquareList(Set<Square> squares){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        squares.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public List<String> toJsonPlayerList(List<Player> players){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        players.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public List<String> toJsonEffectList(List<Effect> effects){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        effects.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public List<String> toJsonDirectionList(List<Square.Direction> directions){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        directions.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public List<String> toJsonAmmoColorList(List<AmmoColor> ammo){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        ammo.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public List<String> toJsonColorList(List<Color> color){
        List<String> list = new ArrayList<>();
        //not making use of the adapter because no need in view
        Gson gson = new Gson();

        color.forEach(x -> list.add(gson.toJson(x)) );

        return list;
    }

    public String toJsonGameBoard(){
        RuntimeTypeAdapterFactory<Square> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Square.class, "Square")
                .registerSubtype(AmmoSquare.class, "AmmoSquare")
                .registerSubtype(GenerationSquare.class, "GenerationSquare");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();
        return gson.toJson(room.getBoard().getMap());
    }

    public String toJsonSkullBoard(){
        Gson gson = new Gson();
        return gson.toJson(room.getBoard().getSkullBoard());
    }

    public Room getRoom(){
        return room;
    }

}
