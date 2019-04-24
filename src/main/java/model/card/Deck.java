package model.card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public abstract class Deck {

    private Stack<Card> cardDeck = new Stack<>();
    Deck(){}

    public void mixDeck(){
        Collections.shuffle(cardDeck);
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
    public  void addCard(Card card){
        cardDeck.push(card);
    }

}
