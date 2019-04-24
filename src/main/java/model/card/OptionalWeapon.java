package model.card;

import java.util.List;

public class OptionalWeapon extends Weapon {

    List<Effect> basicMode;
    List<Effect> alternativeMode;

    public OptionalWeapon(String name, String description, List<AmmoColor> buyCost,
                          AmmoColor chargeCost, List<Effect> basicMode, List<Effect> alternativeMode){
        super(name, description,buyCost, chargeCost);
        this.basicMode = basicMode;
        this.alternativeMode = alternativeMode;
    }
}
