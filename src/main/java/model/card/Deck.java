package model.card;
import java.util.*;

public abstract class Deck <T extends Card>{


    private Deque<T> cardDeck = new LinkedList<>();
    private List<T> usedCard = new ArrayList<>();

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

    public void addCard(T card){cardDeck.push(card);
    }

    public void addAll(List<T> cards){
        cardDeck.addAll(cards);
    }

    /**
     * when the powerupDeck is empty reshuffle the deck.
     */
    public void reMix(){
        addAll(usedCard);
        mixDeck();
    }

<<<<<<< HEAD
    /**
     * when the player use the powerup need add that card in usedPwerups for reuse.
     */
    public  void usedPwerups(T card){
        usedCard.add(card);
=======
    public Deque<Card> getDeck() {
        return cardDeck;
>>>>>>> 85ebbd65123aa92dbb2fd5c5f23ad375fa2dc403
    }
}
