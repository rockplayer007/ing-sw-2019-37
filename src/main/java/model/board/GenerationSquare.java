package model.board;

import model.card.AmmoCard;
import model.card.Weapon;

import java.util.ArrayList;
import java.util.List;

public class GenerationSquare extends Square{

    private List<Weapon> weaponDeck;


    public GenerationSquare(Color color, boolean type, int x, int y, List<Weapon> weaponCards){
        super(color, type, x, y);
        this.weaponDeck = weaponCards;
    }
}
