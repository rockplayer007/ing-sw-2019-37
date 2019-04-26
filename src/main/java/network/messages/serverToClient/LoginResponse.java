package network.messages.serverToClient;

import network.messages.Message;

public class LoginResponse extends Message {

    private boolean status;

    public LoginResponse(boolean askAgain){
        super("server", Content.LOGIN_RESPONSE);

        //askAgain false the login was successful
        //askAgain true username already exists
        this.status = askAgain;

    }


    public boolean getStatus() {
        return status;
    }


}
