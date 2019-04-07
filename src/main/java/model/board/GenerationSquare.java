package model.board;

import model.card.AmmoCard;
import model.card.Weapon;

import java.util.ArrayList;

public class GenerationSquare extends Square{

    private ArrayList<Weapon> weaponDeck;


    public GenerationSquare(Color color, String type, int x, int y, ArrayList<Weapon> weaponCards){
        super(color, type, x, y);
        this.weaponDeck = weaponCards;
    }
}
