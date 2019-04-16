package model.board;

import model.card.Weapon;
import java.util.List;

public class GenerationSquare extends Square{

    private List<Weapon> weaponDeck;


    public GenerationSquare(int id, Color color, int x, int y, List<Weapon> weaponCards){
        super(id, color, true, x, y);
        this.weaponDeck =  weaponCards;
    }

    public List<Weapon> getWeaponDeck(){
        return weaponDeck;
    }
}
