package model.card;

import java.util.List;

public class OptionalWeapon extends Weapon {

    List<Effect> basicEffect;
    List<Effect> alternativeMode;

    public OptionalWeapon(String name, String description, List<AmmoColor> buyCost,
                          AmmoColor chargeCost, List<Effect> basicEffect, List<Effect> optionalEffect){
        super(name, description,buyCost, chargeCost);
        this.basicEffect = basicEffect;
        this.alternativeMode = optionalEffect;
    }
}
