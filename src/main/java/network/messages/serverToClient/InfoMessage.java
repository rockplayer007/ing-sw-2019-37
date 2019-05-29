package network.messages.serverToClient;

import network.messages.Message;

public class InfoMessage extends ServerToClient{

    private String info;
    public InfoMessage(String info){
        super(Message.Content.INFO);
        this.info = info;
    }
    public String getInfo(){
        return info;
    }
}
