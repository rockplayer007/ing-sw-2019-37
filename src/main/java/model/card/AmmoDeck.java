package model.card;
public class AmmoDeck extends Deck{


    public  AmmoDeck(){
        AmmoColor ammoColor=null;
        for (int i = 0; i < 4; i++) {

            addCard(new AmmoCard(ammoColor.RED, ammoColor.BLUE));
            addCard(new AmmoCard(ammoColor.YELLOW, ammoColor.BLUE));
            addCard(new AmmoCard(ammoColor.YELLOW, ammoColor.RED));
        }
        for (int i = 0; i < 2; i++) {
            addCard(new AmmoCard(ammoColor.RED, ammoColor.RED));
            addCard(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE));
            addCard(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW));
            }
       for (int i = 0; i < 3; i++) {
            addCard(new AmmoCard(ammoColor.RED, ammoColor.RED, ammoColor.BLUE));
            addCard(new AmmoCard(ammoColor.RED, ammoColor.RED, ammoColor.YELLOW));
            addCard(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.BLUE));
            addCard(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.RED));
            addCard(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.RED));
            addCard(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.YELLOW));
            }
    }
/*
    @Override
    public Card getCard(){ // draw Ammo card from the deck and remove it
        AmmoCard ammoCard = ammoCards.pop();
        return ammoCard;
    }
*/
}
