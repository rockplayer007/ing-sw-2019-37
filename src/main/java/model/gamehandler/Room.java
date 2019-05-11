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


    public void createMap(int selection) {
        boardMap.createMap(selection);
        String description = boardMap.getMaps().get(selection);
        board.setMap(boardMap);

        logger.log(Level.INFO, "selected board is {0}", description);

        //TODO add update all message
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

    public BoardMap getBoardMap(){
        return boardMap;
    }
    public Board getBoard() {
        return board;
    }

    public void setStartingPlayer(Player player){
        startingPlayer = player;
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    public void setPlayers(List<Player> player){
        players.addAll(player);
    }
}
