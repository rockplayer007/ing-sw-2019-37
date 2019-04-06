package model.card;

import java.util.ArrayList;

public class AmmoDeck {
    private ArrayList<AmmoCard> ammoCards;


    public  AmmoDeck(){
         AmmoColor ammoColor= null ;
        ammoCards = new ArrayList<AmmoCard>();
        for (int i = 0; i < 4; i++) {
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.red));
        }
        for (int i=0;i<2;i++){
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.red));
            ammoCards.add(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW));
        }
        for (int i=0;i<3;i++){
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.red, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.red, ammoColor.YELLOW));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.red));
            ammoCards.add(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.red));
            ammoCards.add(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.YELLOW));
        }
    }

    public AmmoCard getAmmocard(){ //pesca carta Ammo dal deck e la elimina
        AmmoCard ammoCard;
        ammoCard = ammoCards.get(0);
        ammoCards.remove(0);
        return ammoCard;
    }
}
