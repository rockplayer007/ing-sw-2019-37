package network.messages;

public class BoardRequest extends Message {

    private String boards;

    public BoardRequest(String boards){
        super("server", Content.BOARD_REQUEST);
        this.boards = boards;
    }

    public String getBoards() {
        return boards;
    }
}
