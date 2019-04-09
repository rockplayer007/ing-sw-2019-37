package model.board;
import model.card.AmmoCard;


public class AmmoSquare extends Square {

    private AmmoCard ammoCard;


    public AmmoSquare(Color color, boolean type, int x, int y, AmmoCard card){
        super(color, type, x, y);
        this.ammoCard = card;
    }
}
