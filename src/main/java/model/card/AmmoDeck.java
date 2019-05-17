package model.card;
public class AmmoDeck extends Deck<AmmoCard>{


    public  AmmoDeck(){
        for (int i = 0; i < 4; i++) {
            addCard(new AmmoCard("RB",AmmoColor.RED, AmmoColor.BLUE));
            addCard(new AmmoCard("YB",AmmoColor.YELLOW, AmmoColor.BLUE));
            addCard(new AmmoCard("YR",AmmoColor.YELLOW, AmmoColor.RED));
        }
        for (int i = 0; i < 2; i++) {
            addCard(new AmmoCard("RR",AmmoColor.RED, AmmoColor.RED));
            addCard(new AmmoCard("BB",AmmoColor.BLUE, AmmoColor.BLUE));
            addCard(new AmmoCard("YY",AmmoColor.YELLOW, AmmoColor.YELLOW));
        }
       for (int i = 0; i < 3; i++) {
            addCard(new AmmoCard("RRB",AmmoColor.RED, AmmoColor.RED, AmmoColor.BLUE));
            addCard(new AmmoCard("RRY",AmmoColor.RED, AmmoColor.RED, AmmoColor.YELLOW));
            addCard(new AmmoCard("YYB",AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.BLUE));
            addCard(new AmmoCard("YYR",AmmoColor.YELLOW, AmmoColor.YELLOW, AmmoColor.RED));
            addCard(new AmmoCard("BBR",AmmoColor.BLUE, AmmoColor.BLUE, AmmoColor.RED));
            addCard(new AmmoCard("BBY",AmmoColor.BLUE, AmmoColor.BLUE, AmmoColor.YELLOW));
       }
       mixDeck();

    }

}
