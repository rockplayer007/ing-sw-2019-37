package model.card;
import java.util.*;

public abstract class Deck {

    private Deque<Card> cardDeck = new LinkedList<>();


    public void mixDeck(){
        Collections.shuffle((List<?>) cardDeck);
    }

    public Card getCard(){
        return cardDeck.pop();
    }

    public List<Card> getCard(int n){
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < n; i++){
            cards.add(getCard());
        }
        return cards;
    }
    public void addCard(Card card){
        cardDeck.push(card);
    }

}
