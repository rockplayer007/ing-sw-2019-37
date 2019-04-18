package model.gameHandler;

import model.board.Board;
import model.player.Player;

import java.lang.Exception;
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

    public void addPlayer(Player player) throws Exception {
        if(players.size()<6)
            players.add(player);
        else
            throw new Exception("cant add the 6 player");
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
