package network.messages.serverToClient;

import model.card.Card;
import network.messages.Message;

import java.util.List;

public class GeneralRequest extends ServerToClient{

    List<Card> cards = null;

    public GeneralRequest(Content content){
        super(content);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }

}
