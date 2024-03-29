package model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Weapon extends Card {

    private boolean charged;
    private AmmoColor chargeCost;
    private List<AmmoColor> buyCost;
    private boolean optional;
    private transient Map<Effect,Integer> effects;
    private int numberOfEffect;


    /**
     * Constructor
     * @param name name of weapon
     * @param description description of weapon
     * @param chargeCost basic cost for charge
     * @param buyCost cost for buy
     * @param optional it is optional or alternative mode for effect
     * @param effects effects that have
     */
    public Weapon(String name, String description, AmmoColor chargeCost, List<AmmoColor> buyCost,Boolean optional,Map<Effect,Integer> effects){
        super(name, description);
        this.buyCost = buyCost;
        this.chargeCost = chargeCost;
        charged = true;
        this.optional=optional;
        this.effects=effects;
        this.numberOfEffect = effects.size();
        int i = 0;
        for (Effect e:this.effects.keySet()) {
            e.setId(i);
            i++;
        }
        String newDescription = effects.keySet().stream().map(Effect::getDescription).reduce("",(a,b)->a+b);
        newDescription = newDescription + super.getDescription();
        super.setDescription(newDescription);
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

    public Boolean getOptional() {
        return optional;
    }

    public Map<Effect, Integer> getEffects() {
        return effects;
    }

    public int getNumberOfEffect(){
        return numberOfEffect;
    }

    /**
     * @return the cost for charge
     */
    public List<AmmoColor> getChargeCost() {
        List<AmmoColor>cost=new ArrayList<>();
        cost.add(chargeCost);
        cost.addAll(buyCost);
        return cost;
    }

    /**
     * @param level level or grad of effect
     * @return all effects that corresponds to level
     */
    public List<Effect> getLevelEffects(int level){
        return effects.entrySet().stream().filter(x->x.getValue()==level).map(Map.Entry::getKey).collect(Collectors.toList());
    }

}
