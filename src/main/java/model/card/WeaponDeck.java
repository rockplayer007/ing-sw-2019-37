package model.card;

import java.lang.reflect.Array;
import java.util.*;

public class WeaponDeck extends Deck{

    List<Weapon> weaponDeck = new ArrayList<>();

    public WeaponDeck(){
        for(int i = 0; i < 20; i++){
            //adding to the local deck (in the Deck class)
            //for test
            addCard(null);

            //mixDeck();
        }
    }

    private void createLockRifle(){

    }
}
