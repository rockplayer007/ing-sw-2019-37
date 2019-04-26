package network.messages.serverToClient;

import network.messages.Message;

import java.util.Map;

public class BoardRequest extends Message {

    private Map<Integer, String> boards;

    public BoardRequest(Map<Integer, String> boards){
        super("server", Content.BOARD_REQUEST);
        this.boards = boards;
    }

    public Map<Integer, String> getBoards() {
        return boards;
    }
}
