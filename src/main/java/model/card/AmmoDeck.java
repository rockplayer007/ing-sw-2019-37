package model.card;

import java.util.ArrayList;

public class AmmoDeck {
    private ArrayList<AmmoCard> ammoCards;


    public  AmmoDeck(){
        AmmoColor ammoColor= null ;
        ammoCards = new ArrayList<AmmoCard>();
        for (int i = 0; i < 4; i++) {
            ammoCards.add(new AmmoCard(ammoColor.RED, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.RED));
        }
        for (int i=0;i<2;i++){
            ammoCards.add(new AmmoCard(ammoColor.RED, ammoColor.RED));
            ammoCards.add(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW));
        }
        for (int i=0;i<3;i++){
            ammoCards.add(new AmmoCard(ammoColor.RED, ammoColor.RED, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.RED, ammoColor.RED, ammoColor.YELLOW));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.BLUE));
            ammoCards.add(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.RED));
            ammoCards.add(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.RED));
            ammoCards.add(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.YELLOW));
        }
    }

    public AmmoCard getAmmoCard(){ //pesca carta Ammo dal deck e la elimina
        AmmoCard ammoCard;
        ammoCard = ammoCards.get(0);
        ammoCards.remove(0);
        return ammoCard;
    }
}