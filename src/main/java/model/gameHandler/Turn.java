package model.gameHandler;

import model.board.Board;
import model.player.Player;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;

public class Turn {

    private List<Player> players;
    private Board board;



    public Turn(Board board) {
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
}
