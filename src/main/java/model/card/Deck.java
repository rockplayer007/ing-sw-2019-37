package model.card;
import java.util.Collections;
import java.util.Stack;

public abstract class Deck {
    static Stack<AmmoCard> ammoCards= new Stack<AmmoCard>();
    static Stack<Powerup> powerups = new Stack<Powerup>();
    Deck(){}

    public void mixDeck(){
        Collections.shuffle(ammoCards);
        Collections.shuffle(powerups);
    }

    public abstract Card getCard();

}
