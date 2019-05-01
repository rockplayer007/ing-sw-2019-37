package network.messages.serverToClient;


public class LoginResponse extends ServerToClient {

    private boolean status;
    private String clientID;

    public LoginResponse(boolean askAgain, String clientID){
        super(Content.LOGIN_RESPONSE);

        //askAgain false the login was successful
        //askAgain true username already exists
        this.status = askAgain;
        this.clientID = clientID;

    }


    public boolean getStatus() {
        return status;
    }

    public String getClientID() {
        return clientID;
    }
}
