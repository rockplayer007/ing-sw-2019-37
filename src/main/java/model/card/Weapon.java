package model.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


<<<<<<< HEAD
public class Weapon extends Card {
=======

public class Weapon  extends Card {
>>>>>>> 85ebbd65123aa92dbb2fd5c5f23ad375fa2dc403
    private Boolean charged;
    private AmmoColor chargeCost;
    private List<AmmoColor> buyCost;
    private Boolean optional;
    private transient Map<Effect,Integer> effects;


    Weapon(String name, String description, AmmoColor chargeCost, List<AmmoColor> buyCost,Boolean optional,Map<Effect,Integer> effects){
        super(name, description);
        this.buyCost = buyCost;
        this.chargeCost = chargeCost;
        charged = true;
        this.optional=optional;
        this.effects=effects;
    }


    public Boolean getCharged(){
        return charged;
    }

    public void setCharged(Boolean state){
        charged=state;
    }

    public List<AmmoColor> getBuyCost() {
        return buyCost;
    }

    public List<AmmoColor> getChargeCost() {
        List<AmmoColor>cost=new ArrayList<>();
        cost.add(chargeCost);
        cost.addAll(buyCost);
        return cost;
    }

    public Boolean getOptional() {
        return optional;
    }

    public Map<Effect, Integer> getEffects() {
        return effects;
    }

    /**
     * @return description of the card
     */
    @Override
    public String getDescription() {
        String description = effects.keySet().stream().map(Effect::getDescription).reduce("",(a,b)->a+b);
        description = description + super.getDescription();
        return description;
    }


}
