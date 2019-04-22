package model.card;

import java.util.ArrayList;
import java.util.List;

public class WeaponDeck extends Deck{

    List<Card> weaponDeck = new ArrayList<>();

    public WeaponDeck(){

        //only for test purposes
        addCard(new Weapon("Whisper", "description",2,10,0,0,false,false));
        addCard(new Weapon("Hellion", "description",1,10,0,0,false,false));

    }

}
