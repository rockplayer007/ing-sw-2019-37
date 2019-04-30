package network.messages.serverToClient;


public class LoginResponse extends ServerToClient {

    private boolean status;

    public LoginResponse(boolean askAgain){
        super(Content.LOGIN_RESPONSE);

        //askAgain false the login was successful
        //askAgain true username already exists
        this.status = askAgain;

    }


    public boolean getStatus() {
        return status;
    }


}
