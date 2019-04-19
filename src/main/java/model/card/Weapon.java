package model.card;

import java.util.ArrayList;
import java.util.List;

public class Weapon extends Card {
    private Boolean charged;
    private List<AmmoColor> buyCost=new ArrayList<>();
    private List<AmmoColor> chargeCost=new ArrayList<>();



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
        cost.addAll(chargeCost);
        cost.addAll(buyCost);
        return cost;
    }
}
