package network.messages.clientToServer;

import model.card.Card;

import java.util.List;

public class GeneralResponse extends ClientToServer {

    private Card card;
    private Boolean answer;

    public GeneralResponse(String username,String id, Content content){
        super(username, id, content);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card){
        this.card = card;
    }

    public void setAnswer(Boolean answer){
        this.answer = answer;
    }
    public Boolean getAnswer() {
        return answer;
    }
}