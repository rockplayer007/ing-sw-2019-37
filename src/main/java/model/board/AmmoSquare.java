package model.board;
import model.card.AmmoCard;

/**
 * Square that contains an ammoCard
 */
public class AmmoSquare extends Square {

    private AmmoCard ammoCard;

    /**
     * Constructor of the class
     * @param id Number of the square
     * @param color Color of the square
     * @param x x coordinate of the square
     * @param y y coordinate of the square
     * @param card AmmoCard that has to be placed
     */
    public AmmoSquare(int id, Color color, int x, int y, AmmoCard card){
        super(id, color, false, x, y);
        this.ammoCard = card;
    }

    /**
     * Gives the AmmoCard on the square
     * @return AmmoCard on the square
     */
    public AmmoCard getAmmoCard(){
        return ammoCard;
    }

    /**
     * Allows to remove the card that is in that square
     */
    public void removeAmmoCard(){
        ammoCard = null;
    }

    /**
     * Sets a given ammo card in the square
     * @param ammoCard the card that has to be put on the square
     */
    void setAmmoCard(AmmoCard ammoCard){
        this.ammoCard = ammoCard;
    }
}
