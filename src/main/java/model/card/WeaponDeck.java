package model.card;

import java.util.ArrayList;
import java.util.List;

public class WeaponDeck extends Deck{

    List<Card> weaponDeck = new ArrayList<>();

    public WeaponDeck(){

        //only for test purposes
        for (int i = 0; i< 20; i++){
            addCard(new Weapon(""));
        }
    }

}
