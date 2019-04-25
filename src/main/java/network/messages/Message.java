package network.messages;

import java.io.Serializable;

public class Message implements Serializable{

    private String sender;
    private Content content;

    public Message(String sender, Content content){
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public Content getContent(){
        return content;
    }

    public enum Content{
        LOGIN_REQUEST, BOARD_REQUEST, BOARD_RESPONSE
    }
}
