package model.board;
import model.card.AmmoCard;


public class AmmoSquare extends Square {

    private AmmoCard ammoCard;


    public AmmoSquare(int id, Color color, int x, int y, AmmoCard card){
        super(id, color, false, x, y);
        this.ammoCard = card;
    }
}
