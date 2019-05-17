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

    public void setContent(Content content) {
        this.content = content;
    }

    public enum Content{
        //ServerToClient
        TIMEOUT, LOGIN_RESPONSE, BOARD_REQUEST, BOARD_INFO, CARD_REQUEST,
        YESNO_REQUEST,
        //ClientToServer
        LOGIN_REQUEST, BOARD_RESPONSE, CARD_RESPONSE,
        YESNO_RESPONSE

    }

}
