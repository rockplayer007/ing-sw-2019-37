package model.card;
import java.util.*;

public abstract class Deck <T extends Card>{


    private Deque<T> cardDeck = new LinkedList<>();
    private List<T> usedCard = new ArrayList<>();

    /**
     * for mix the Deck
     */
    public void mixDeck(){
        Collections.shuffle((List<?>) cardDeck);
    }

    /**
     * @return take a card form deck
     */
    public T getCard(){
        if(cardDeck.isEmpty()){
            reMix();
            if (cardDeck.isEmpty())//check necessary for the weaponDeck
                return null;
        }
        return cardDeck.pop();
    }

    /**
     * @param n number of card that want take
     * @return take n cards form deck
     */
    public List<T> getCard(int n){
        List<T> cards = new ArrayList<>();
        for (int i = 0; i < n; i++){
            cards.add(getCard());
        }
        return cards;
    }

    /**
     * @param card card that want to add in the deck
     */
    public void addCard(T card){cardDeck.push(card);
    }

    /**
     * @param cards cards that want to add in the deck
     */
    public void addAll(List<T> cards){
        cardDeck.addAll(cards);
    }

    /**
     * when the powerupDeck is empty reshuffle the deck.
     */
    public void reMix(){
        addAll(usedCard);
        usedCard.clear();
        mixDeck();
    }

    /**
     * when the player use the powerup need add that card in usedPwerups for reuse.
     */
    public void usedCard(T card){
        usedCard.add(card);
    }

    public T returnCard(T card){
        if (usedCard.remove(card))
            return card;
        return null;

    }

}
