package network.messages.serverToClient;

public class TimeoutMessage extends ServerToClient {

    public TimeoutMessage(){
        super(Content.TIMEOUT);
    }
}
