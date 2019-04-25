package network.messages;

public class BoardResponse extends Message{

    private int selectedBoard;

    public BoardResponse(int selection){
        super("server", Content.BOARD_RESPONSE);
        this.selectedBoard = selection;
    }

    public int getSelectedBoard() {
        return selectedBoard;
    }
}
