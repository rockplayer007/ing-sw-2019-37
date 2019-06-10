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
        LOGIN_RESPONSE, BOARD_REQUEST,

        POWERUP_REQUEST, ACTION_REQUEST, SQUARE_REQUEST, WEAPON_REQUEST,
        EFFECT_REQUEST, PLAYER_REQUEST, DIRECTION_REQUEST, AMMOCOLOR_REQUEST, ROOM_REQUEST,


        //general
        TIMEOUT, INFO, UPDATE, ATTACK, CONNECTION, SCORE,
        //ClientToServer
        LOGIN_REQUEST, BOARD_RESPONSE, CARD_RESPONSE, SQUARE_RESPONSE, EFFECT_RESPOSNSE, PLAYER_RESPONSE,
        DIRECTION_RESPONSE, AMMO_RESPONSE, ROOM_RESPONSE,

    }

}
