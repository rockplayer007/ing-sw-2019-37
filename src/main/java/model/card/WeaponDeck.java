package model.card;

import java.lang.reflect.Array;
import java.util.*;

public class WeaponDeck extends Deck{

    List<Weapon> weaponDeck = new ArrayList<>();

    public WeaponDeck(){
        for(int i = 0; i < 20; i++){
            //adding to the local deck (in the Deck class)
            //for test
            ArrayList<AmmoColor> cost = new ArrayList<>();
            cost.add(AmmoColor.BLUE);
            cost.add(AmmoColor.YELLOW);
            Weapon weapon = new Weapon("TestName", "lots of effects", AmmoColor.BLUE, cost);

            addCard(weapon);

            //mixDeck();
        }
    }

    private void createLockRifle(){

    }
}
