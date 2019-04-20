package model.card;

import java.util.ArrayList;
import java.util.List;
import model.player.*;

interface Effect  {
    void applyOn(Player p);
}


public class Weapon extends Card {
    private Boolean charged;
    private List<AmmoColor> buyCost=new ArrayList<>();
    private List<AmmoColor> chargeCost=new ArrayList<>();
    private ArrayList<Effect> effects = new ArrayList<Effect>();
    private ArrayList<Effect> alternativeeffects = new ArrayList<Effect>();

    public Weapon(String jsonName){

        Effect eff1 = (p) -> {
           // p.addWeapon("name");
        };

        /**
         *remove ammo for additional / alternative effects
         */
        Effect eff2 = (p) -> {
           // p.removeAmmo();
        };

        /**
         *change the list of visible targets
         */
        Effect eff3 = (p) -> {
           // p.returnVisibleTarget();
        };

        /**
         * select target from the list
         */
        Effect eff4 = (p) -> {
            // p.selectTarget();
        };

        /**
         * damage the selected target
         */
        Effect eff5 = (p) -> {
            // p.giveDamage();
        };

        /**
         * mark the selected target
         */
        Effect eff6 = (p) -> {
            // p.giveMark();
        };

        /**
         * remove target from the target list
         */
        Effect eff7 = (p) -> {
            // p.removeTarget();
        };

        /**
         * add target to the list of those who received damage
         */
        Effect eff8 = (p) -> {
            // p.addTargetedList();
        };
        /**
         * select target from the targeted list
         */
        Effect eff9 = (p) -> {
            // p.selectTargeted();
        };

        /**
         * move player
         */
        Effect eff10 = (p) -> {
            // p.move();
        };
        /**
         * move target
         */
        Effect eff11 = (p) -> {
            // p.moveTarget();
        };

        /**
         * get list of all targets for attack without visibility
         */
        Effect eff12 = (p) -> {
            // p.getallplayers();
        };





/**
 * read from file and add effects to weapons
 */
       //effects.add(eff1);
       // effects.add(eff2);


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
        cost.addAll(chargeCost);
        cost.addAll(buyCost);
        return cost;
    }
    public ArrayList<Effect> getEffects(){
        return  effects;
    }
    public ArrayList<Effect> getAlternativeEffects(){
        return  alternativeeffects;
    }
}
