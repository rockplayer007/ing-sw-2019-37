package model.gamehandler;

import model.board.Board;
import model.player.Player;
import model.exceptions.*;
import network.messages.serverToClient.BoardRequest;
import network.messages.Message;
import network.messages.serverToClient.ServerToClient;
import network.server.ClientOnServer;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {

    private List<Player> players;
    private Map<Player, ClientOnServer> connectionToClient = new HashMap<>();
    private Board board;
    private Player currentPlayer;
    private Player startingPlayer;

    private static final Logger logger = Logger.getLogger(Room.class.getName());


    public Room(Board board) {
        super();
        this.board = board;
        this.players = new ArrayList<>(5);
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


    public void nextPlayer() {
        if (players != null) {
            if (currentPlayer == null)
                currentPlayer = players.get(0);
            else if (players.indexOf(currentPlayer) < players.size())
                currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
            else
                currentPlayer = players.get(0);
        }
    }

    public void startMatch() {
        //TODO add controller
        //

        //ask first player
        ServerToClient boardRequest = new BoardRequest(board.getMap().getMaps());
        sendMessage(startingPlayer, boardRequest);
    }

    public void createMap(int selection) {
        board.getMap().createMap(selection);
        String description = board.getMap().getMaps().get(selection);

        logger.log(Level.INFO, "selected board is {0}", description);
    }

    //move this somewhere else if needed (better controller probably)

    public void sendMessage(Player player, ServerToClient message){
        try{
            connectionToClient.get(player).getClientInterface()
                    .notifyClient(message);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "Connection error", e);
        }

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

