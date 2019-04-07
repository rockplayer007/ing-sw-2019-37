package model.player;
import model.board.Square;
import model.card.AmmoCard;
import model.card.Powerup;
import model.card.Weapon;
import java.util.*;

/**
 * 
 */
public class Player {

    public String nickname;
    public Hero hero;
    public Square position;
    public PlayerBoard board;
    public ArrayList<AmmoCard> ammo;
    public ArrayList<Weapon> weapons;
    public ArrayList<Powerup> powerups;
    public Actions actionStatus;

    public Player() {
    }



    public void turn() {
        // TODO implement here
    }

}