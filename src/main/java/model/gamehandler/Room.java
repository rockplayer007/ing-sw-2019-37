package model.gamehandler;

import model.board.Board;
import model.player.Player;
import model.exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private List<Player> players;
    private Board board;
    private Player currentPlayer;



    public Room(Board board) {
        this.board=board;
        players=new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) throws TooPlayerException {
        if(players.size()<5)
            players.add(player);
        else
            throw new TooPlayerException("cant add the 6th player");
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
}
