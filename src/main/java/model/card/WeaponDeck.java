package model.card;

import java.util.ArrayList;
import java.util.List;

public class WeaponDeck extends Deck{

    List<Weapon> weaponDeck = new ArrayList<>();

    public WeaponDeck(){
        String description = "basic effect: Deal 2 damage and 1 mark to 1 target  you can see." +
                " with second lock: Deal 1 mark to a different target  you can see.\n";
        List<AmmoColor> buyCost = new ArrayList<>();
        buyCost.add(AmmoColor.BLUE);
        List<Effect> basicEffect = new ArrayList<>();
        List<Effect> optionalEffect = new ArrayList<>();
        List<Operation> basicOperations = new ArrayList<>();
        List<Operation> extraOperations = new ArrayList<>();

        //basic operations
        basicOperations.add(new VisiblePlayers());
        basicOperations.add(new SelectTargets());
        basicOperations.add(new Damage(2));
        basicOperations.add(new Mark(1));
        //second list of operations for the effect
        extraOperations.add(new SelectTargets());
        extraOperations.add(new Mark(1));

        //setup cost and operations in the effect
        basicEffect.add(new Effect(new ArrayList<>(), basicOperations));

        List<AmmoColor> extraCost = new ArrayList<>();
        extraCost.add(AmmoColor.RED);
        optionalEffect.add(new Effect(extraCost, extraOperations));

        OptionalWeapon lock_rifle = new OptionalWeapon("LOCK RIFLE", description, buyCost, AmmoColor.BLUE,
                basicEffect, optionalEffect);
        //only for test purposes
        //addCard(new Weapon("Whisper", "description",2,10,0,0,false,false));
        //addCard(new Weapon("Hellion", "description",1,10,0,0,false,false));

    }

}
