package network.messages.serverToClient;

import model.board.Board;
import model.card.Card;
import model.card.Powerup;
import network.messages.Message;

import java.util.List;

public class UpdateMessage extends ServerToClient{

    //string format because its in json
    private List<String> playerPowerups = null;
    private String board = null;
    private String skullBoard = null;


    public UpdateMessage(String board, List<String> playerPowerups, String skullBoard){
        super(Content.UPDATE);
        this.board = board;
        this.playerPowerups = playerPowerups;
        this.skullBoard = skullBoard;
    }

    public List<String> getPlayerPowerups() {
        return playerPowerups;
    }

    public String getBoard() {
        return board;
    }

    public String getSkullBoard() {
        return skullBoard;
    }
}
