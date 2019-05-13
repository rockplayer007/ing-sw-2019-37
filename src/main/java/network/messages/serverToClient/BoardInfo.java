package network.messages.serverToClient;



public class BoardInfo extends ServerToClient {

    private String map;

    public BoardInfo(String map){
        super(Content.BOARD_INFO);
        this.map = map;
    }

    public String getBoard() {
        return map;
    }

}
