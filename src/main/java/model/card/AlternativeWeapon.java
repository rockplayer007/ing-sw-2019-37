package model.card;


import java.util.List;

public class AlternativeWeapon extends Weapon {

    private Effect basicMode;
    private Effect alternativeMode;

    public AlternativeWeapon(String name, String description, List<AmmoColor> buyCost,
                             AmmoColor chargeCost, Effect basicMode, Effect alternativeMode){
        super(name, description,buyCost, chargeCost);
        this.basicMode = basicMode;
        this.alternativeMode = alternativeMode;
    }

}
