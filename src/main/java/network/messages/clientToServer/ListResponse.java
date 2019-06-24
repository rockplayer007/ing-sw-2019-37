package network.messages.clientToServer;

public class ListResponse extends ClientToServer {

    private int selectedItem;

    public ListResponse(String username, String id, int selection, Content content){
        super(username, id, content);
        this.selectedItem = selection;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

}
