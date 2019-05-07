package model.gamehandler;

import model.board.Board;
import model.board.BoardMap;
import model.player.Player;
import model.exceptions.*;
import network.messages.clientToServer.BoardResponse;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.BoardRequest;
import network.messages.serverToClient.ServerToClient;
import network.server.ClientOnServer;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {

    private List<Player> players;
    private Map<Player, ClientOnServer> connectionToClient;
    private Board board;
    private BoardMap boardMap;
    private Player currentPlayer;
    private Player startingPlayer;

    private static final Logger logger = Logger.getLogger(Room.class.getName());


    public Room() {
        board = new Board();
        boardMap = new BoardMap(board);
        players = new ArrayList<>();
        connectionToClient = new HashMap<>();
    }

    public void handleMessages(ClientToServer message) {
        switch (message.getContent()) {
            case BOARD_RESPONSE:
                createMap(((BoardResponse) message).getSelectedBoard());
                break;

            default:
                logger.log(Level.WARNING, "Unhandled message");
        }
    }

    public void addPlayer(Player player) throws TooManyPlayerException {
        if (players.isEmpty()) {
            startingPlayer = player;
            currentPlayer = player;
        }
        if (players.size() < 5)
            players.add(player);
        else
            throw new TooManyPlayerException("cant add the 6th player");
    }

    //needed for starting a new room from waitingRoom
    public void addPlayer(ClientOnServer client) {
        if (players.isEmpty()) {
            startingPlayer = client.getPersonalPlayer();
            currentPlayer = client.getPersonalPlayer();
        }
        if (players.size() < 5) {
            players.add(client.getPersonalPlayer());
            connectionToClient.put(client.getPersonalPlayer(), client);
        } else
            throw new TooManyPlayerException("cant add the 6th player");
    }


    public void setNextPlayer() {

        if (players != null) {
            if (currentPlayer == null)
                currentPlayer = players.get(0);
            else if (players.indexOf(currentPlayer) < players.size() - 1)
                currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
            else
                currentPlayer = players.get(0);
        }
    }

    //asks the board
    public void matchSetup() {
        //TODO add controller
        //

        //ask first player
        ServerToClient boardRequest = new BoardRequest(boardMap.getMaps());

        Thread temp = new Thread(() -> sendMessage(currentPlayer, boardRequest));
        temp.start();
        try {

            TimeUnit.SECONDS.sleep(15); //15 seconds
        }catch (InterruptedException e){
            logger.log(Level.WARNING, "timer stopped", e);
        }
        if(board.getMap() == null){
            //send a timeout message to current player
            //set next current player
            matchSetup();
        }
        else {
            //notify all the clients about the chosen board
        }


/*
        while (board.getMap() == null){

            Thread temp = new Thread(() -> sendMessage(currentPlayer, boardRequest));
            temp.start();
            try {
                System.out.println("ciaoooooooooooooo");
                TimeUnit.SECONDS.sleep(15); //15 seconds
            }catch (InterruptedException e){
                logger.log(Level.WARNING, "timer stopped", e);
            }
            temp.interrupt();
            setNextPlayer();
        }

 */

        //TimeUnit.MINUTES.sleep(1);

        //if timer ends or message connection error ask somebody else
    }

    public void createMap(int selection) {
        boardMap.createMap(selection);
        String description = boardMap.getMaps().get(selection);
        board.setMap(boardMap);

        logger.log(Level.INFO, "selected board is {0}", description);

        //TODO add update all message
    }

    public void sendMessage(Player player, ServerToClient message){
        try{
            connectionToClient.get(player).getClientInterface()
                    .notifyClient(message);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "Connection error", e);
        }

    }

    public void sendMessageToAll(ServerToClient message){
        players.forEach(x -> sendMessage(x, message));
    }


    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }
}
