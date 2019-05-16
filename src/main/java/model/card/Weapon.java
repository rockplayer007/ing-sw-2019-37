package model.card;

import java.util.ArrayList;
import java.util.List;



public class Weapon extends Card {
    private Boolean charged;
    private AmmoColor chargeCost;
    private List<AmmoColor> buyCost;


    Weapon(String name, String description, AmmoColor chargeCost, List<AmmoColor> buyCost){
        super(name, description);
        this.buyCost = buyCost;
        this.chargeCost = chargeCost;
        charged = true;
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

    public ArrayList<AmmoColor> getChargeCost() {
        ArrayList<AmmoColor>cost=new ArrayList<>();
        cost.add(chargeCost);
        cost.addAll(buyCost);
        return cost;
    }

}
