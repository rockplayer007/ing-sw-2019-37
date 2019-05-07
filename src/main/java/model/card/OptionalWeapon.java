package model.card;

import java.util.List;

public class OptionalWeapon extends Weapon {

    List<Effect> basicEffect;
    List<Effect> alternativeMode;

    public OptionalWeapon(String name, String description,AmmoColor chargeCost,
                           List<AmmoColor> buyCost, List<Effect> basicEffect, List<Effect> optionalEffect){
        super(name, description,chargeCost,buyCost);
        this.basicEffect = basicEffect;
        this.alternativeMode = optionalEffect;
    }

}
