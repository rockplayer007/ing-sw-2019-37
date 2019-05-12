package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.board.AmmoSquare;
import model.board.GenerationSquare;
import model.board.RuntimeTypeAdapterFactory;
import model.board.Square;
import model.exceptions.TooManyPlayerException;
import model.gamehandler.Room;
import model.player.Player;
import network.messages.clientToServer.BoardResponse;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.BoardInfo;
import network.messages.serverToClient.BoardRequest;
import network.messages.serverToClient.ServerToClient;
import network.messages.serverToClient.TimeoutMessage;
import network.server.ClientOnServer;

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
    private Thread askingThread;

    private static final Logger logger = Logger.getLogger(RoomController.class.getName());


    public RoomController() {
        room = new Room();
        players = new ArrayList<>();
        connectionToClient = new HashMap<>();

    }

    public void handleMessages(ClientToServer message) {
        if(checkReceiver(message)){
            switch (message.getContent()) {
                case BOARD_RESPONSE:
                    mockMessage = message;
                    askingThread.interrupt();
                    break;

                default:
                    logger.log(Level.WARNING, "Unhandled message");
            }
        }
        else {
            logger.log(Level.INFO, "Discarted message from {0} expected from {1}", new String[]{ message.getSender(), expectedReceiver});
        }

    }


    //needed for starting a new room from waitingRoom
    public void addPlayer(ClientOnServer client) {
        Player player = client.getPersonalPlayer();
        if (players.isEmpty()) {
            room.setStartingPlayer(player);
            room.setCurrentPlayer(player);
        }
        if (players.size() < 5) {
            players.add(player);
            connectionToClient.put(player, client);
        } else
            throw new TooManyPlayerException("cant add the 6th player");
    }

    public void matchSetup() {

        //add players to the room
        room.setPlayers(players);

        askBoard();

        System.out.println("next steeeeeeeeeeeep");


    }


    public void sendMessage(Player player, ServerToClient message){
        try{
            connectionToClient.get(player).getClientInterface()
                    .notifyClient(message);
            logger.log(Level.INFO, "Sending message to: {0}", player.getNickname());
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "Connection error", e);
        }

    }

    private void askBoard(){
        ServerToClient boardRequest = new BoardRequest(room.getBoardGenerator().getMaps());

        mockMessage = null;
        askingThread = Thread.currentThread();

        sendMessage(room.getCurrentPlayer(), boardRequest);
        expectedReceiver = room.getCurrentPlayer().getNickname();

        while (mockMessage == null){

            try {
                Thread.sleep(10000);  //10 seconds

            } catch (InterruptedException e) {
                System.out.println("have been interrupted before finishing");
            }
            synchronized (this){
                if(mockMessage == null){
                    //first send timeout message
                    sendMessage(room.getCurrentPlayer(), new TimeoutMessage());
                    room.setNextPlayer();
                    expectedReceiver = room.getCurrentPlayer().getNickname();
                    sendMessage(room.getCurrentPlayer(), boardRequest);
                }

            }

        }

        room.createMap(((BoardResponse) mockMessage).getSelectedBoard());

        RuntimeTypeAdapterFactory<Square> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Square.class, "Square")
                .registerSubtype(AmmoSquare.class, "AmmoSquare")
                .registerSubtype(GenerationSquare.class, "GenerationSquare");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();

        //Gson gson = new Gson();
        sendMessageToAll(new BoardInfo(gson.toJson(room.getBoard().getMap())));

        System.out.println("board is: " + ((BoardResponse) mockMessage).getSelectedBoard());
    }


    public void sendMessageToAll(ServerToClient message){
        players.forEach(x -> sendMessage(x, message));
    }

    public boolean checkReceiver(ClientToServer message){
        return message.getSender().equals(expectedReceiver);
    }


}
