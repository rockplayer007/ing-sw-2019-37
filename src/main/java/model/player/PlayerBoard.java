package model.player;
import java.util.*;

/**
 * 
 */
public class PlayerBoard {

    private Player[] hp;
    private int[] points;
    private int deathPoint;
    private List<Player> marks;

    public PlayerBoard() {
        this.hp=new Player[12];
        this.points=new int[6];
        this.deathPoint=0;
    }
    
    public void setDeathPoint(int deathPoint) {
        this.deathPoint = deathPoint;
    }

    public int getDeathPoint() {
        return deathPoint;
    }

    /**
     * Sets the player that applied the damage on the board
     * @param player
     */
    public void addDamage(Player player){
        //TODO
    }

    public void addMark(Player player){
        //TODO
    }


}