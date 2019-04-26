package network.messages.clientToServer;

import network.messages.Message;

public class BoardResponse extends ClientToServer {

    private int selectedBoard;

    public BoardResponse(String username,String id, int selection){
        super(username, id, Content.BOARD_RESPONSE);
        this.selectedBoard = selection;
    }

    public int getSelectedBoard() {
        return selectedBoard;
    }

}
