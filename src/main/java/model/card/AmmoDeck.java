package model.card;
public class AmmoDeck extends Deck{


    public  AmmoDeck(){
        for (int i = 0; i < 4; i++) {
            addCard(new AmmoCard(AmmoColor.RED, AmmoColor.BLUE));
            addCard(new AmmoCard(AmmoColor.YELLOW, AmmoColor.BLUE));
            addCard(new AmmoCard(AmmoColor.YELLOW, AmmoColor.RED));
        }
        for (int i = 0; i < 2; i++) {
            addCard(new AmmoCard(AmmoColor.RED, AmmoColor.RED));
            addCard(new AmmoCard(AmmoColor.BLUE, AmmoColor.BLUE));
            addCard(new AmmoCard(AmmoColor.YELLOW, AmmoColor.YELLOW));
            }
       for (int i = 0; i < 3; i++) {
            addCard(new AmmoCard(AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE));
            addCard(new AmmoCard(AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW));
            addCard(new AmmoCard(AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.BLUE));
            addCard(new AmmoCard(AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.RED));
            addCard(new AmmoCard(AmmoColor.BLUE, AmmoColor.BLUE, AmmoColor.RED));
            addCard(new AmmoCard(AmmoColor.BLUE, AmmoColor.BLUE, AmmoColor.YELLOW));
            }
    }

}
