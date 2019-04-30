package network.messages.serverToClient;

import java.util.Map;

public class BoardRequest extends ServerToClient {

    private Map<Integer, String> boards;

    public BoardRequest(Map<Integer, String> boards){
        super(Content.BOARD_REQUEST);
        this.boards = boards;
    }

    public Map<Integer, String> getBoards() {
        return boards;
    }
}
