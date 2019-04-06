package model.card;

import model.card.AmmoCard;
import model.card.AmmoColor;

import java.util.ArrayList;

public class AmmoDeck {
    private ArrayList<AmmoCard> ammoCards;


    public  AmmoDeck(){
         AmmoColor ammoColor= null ;
        ammoCards = new ArrayList<AmmoCard>();
        for (int i = 0; i < 4; i++) {
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.blue));
            ammoCards.add(new AmmoCard(ammoColor.yellow, ammoColor.blue));
            ammoCards.add(new AmmoCard(ammoColor.yellow, ammoColor.red));
        }
        for (int i=0;i<2;i++){
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.red));
            ammoCards.add(new AmmoCard(ammoColor.blue, ammoColor.blue));
            ammoCards.add(new AmmoCard(ammoColor.yellow, ammoColor.yellow));
        }
        for (int i=0;i<3;i++){
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.red, ammoColor.blue));
            ammoCards.add(new AmmoCard(ammoColor.red, ammoColor.red, ammoColor.yellow));
            ammoCards.add(new AmmoCard(ammoColor.yellow, ammoColor.yellow, ammoColor.blue));
            ammoCards.add(new AmmoCard(ammoColor.yellow, ammoColor.yellow, ammoColor.red));
            ammoCards.add(new AmmoCard(ammoColor.blue, ammoColor.blue, ammoColor.red));
            ammoCards.add(new AmmoCard(ammoColor.blue, ammoColor.blue, ammoColor.yellow));
        }
    }

    public AmmoCard getAmmocard(){ //pesca carta Ammo dal deck e la elimina
        AmmoCard ammoCard;
        ammoCard = ammoCards.get(0);
        ammoCards.remove(0);
        return ammoCard;
    }
}
