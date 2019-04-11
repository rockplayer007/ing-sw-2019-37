package model.card;

import java.util.ArrayList;
import java.util.List;

public class WeaponDeck implements Deck{

    List<Card> weaponDeck = new ArrayList<>();
    @Override
    public Card getCard() {
        return null;
    }

    // da cambiare e mettere in Deck
    public List<Card> getCard(int n){
        List<Card> cards = new ArrayList<>();
        for(int i = 0; i < n; i++){
            cards.add(getCard());
        }
        return cards;
    }

    @Override

    public void CreateDeck() {
        //only for test purposes
        for (int i = 0; i< 20; i++){
            weaponDeck.add(new Weapon());
        }
    }
}
