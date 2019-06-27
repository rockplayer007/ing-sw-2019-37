package network.messages.serverToClient;

import network.messages.Message;

public class InfoMessage extends ServerToClient{

    private String info;
    private boolean connection;
    public InfoMessage(String info){
        super(Message.Content.INFO);
        this.info = info;
        connection = false;
    }
    public String getInfo(){
        return info;
    }
    public boolean getConnection(){
        return connection;
    }
    public void setConnection(){
        connection = true;
    }
}
