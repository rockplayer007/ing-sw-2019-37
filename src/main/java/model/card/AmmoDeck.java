package model.card;
public class AmmoDeck extends Deck{

    public  AmmoDeck(){
        AmmoColor ammoColor=null;
        for (int i = 0; i < 4; i++) {
            ammoCards.push(new AmmoCard(ammoColor.RED, ammoColor.BLUE));
            ammoCards.push(new AmmoCard(ammoColor.YELLOW, ammoColor.BLUE));
            ammoCards.push(new AmmoCard(ammoColor.YELLOW, ammoColor.RED));
        }
        for (int i = 0; i < 2; i++) {
            ammoCards.push(new AmmoCard(ammoColor.RED, ammoColor.RED));
            ammoCards.push(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE));
            ammoCards.push(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW));
            }
       for (int i = 0; i < 3; i++) {
            ammoCards.push(new AmmoCard(ammoColor.RED, ammoColor.RED, ammoColor.BLUE));
            ammoCards.push(new AmmoCard(ammoColor.RED, ammoColor.RED, ammoColor.YELLOW));
            ammoCards.push(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.BLUE));
            ammoCards.push(new AmmoCard(ammoColor.YELLOW, ammoColor.YELLOW, ammoColor.RED));
            ammoCards.push(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.RED));
            ammoCards.push(new AmmoCard(ammoColor.BLUE, ammoColor.BLUE, ammoColor.YELLOW));
            }
    }

    @Override
    public Card getCard(){ // draw Ammo card from the deck and remove it
        AmmoCard ammoCard = ammoCards.pop();
        return ammoCard;
    }

}
