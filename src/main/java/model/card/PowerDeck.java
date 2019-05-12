package model.card;
import java.util.*;

public class PowerDeck extends Deck{

    public PowerDeck(){
        ArrayList<String> na= new ArrayList<>();
        na.add("Targeting Scope");
        na.add("Newton");
        na.add("Tagback Grenade");
        na.add("Teleporter");
        String des = "...";
        for(int i=0;i<2;i++) {
            for (int j = 0; j < na.size(); j++) {
                addCard(new Powerup(na.get(j), des, AmmoColor.RED, j + 1));
                addCard(new Powerup(na.get(j), des, AmmoColor.BLUE, j + 1));
                addCard(new Powerup(na.get(j), des, AmmoColor.YELLOW, j + 1));
            }
        }
        mixDeck();
    }

}
