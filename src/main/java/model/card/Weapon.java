package model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Weapon extends Card {

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

    public List<Effect> getLevelEffects(int level){
        return effects.entrySet().stream().filter(x->x.getValue()==level).map(Map.Entry::getKey).collect(Collectors.toList());
    }

}
