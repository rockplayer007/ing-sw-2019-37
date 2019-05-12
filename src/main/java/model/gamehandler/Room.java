package model.gamehandler;

import model.board.Board;
import model.board.BoardGenerator;
import model.board.GameBoard;
import model.player.Player;

import network.server.ClientOnServer;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {

    private List<Player> players;
    private Map<Player, ClientOnServer> connectionToClient;
    private Board board;
    private BoardGenerator boardGenerator;
    private Player currentPlayer;
    private Player startingPlayer;

    private static final Logger logger = Logger.getLogger(Room.class.getName());


    public Room() {
        board = new Board();
        boardGenerator = new BoardGenerator(board);
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
        GameBoard gameBoard = boardGenerator.createMap(selection);
        String description = gameBoard.getDescription();
        board.setMap(gameBoard);

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

    public BoardGenerator getBoardGenerator(){
        return boardGenerator;
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
