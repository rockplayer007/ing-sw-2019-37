package network.messages.clientToServer;

public class LoginSocketRequest extends ClientToServer{

    private String clientID;

    public LoginSocketRequest(String username, String clientID){
        super(username, clientID, Content.LOGIN_REQUEST);

        this.clientID = clientID;
    }

    public String getClientID() {
        return clientID;
    }
}
