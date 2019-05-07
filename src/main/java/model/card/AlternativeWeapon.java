package model.card;

import java.util.List;


public class AlternativeWeapon extends Weapon {

    private Effect basicMode;
    private Effect alternativeMode;

    public AlternativeWeapon(String name, String description,AmmoColor chargeCost,
                              List<AmmoColor> buyCost, Effect basicMode, Effect alternativeMode){
        super(name, description,chargeCost,buyCost);
        this.basicMode = basicMode;
        this.alternativeMode = alternativeMode;
    }

}
