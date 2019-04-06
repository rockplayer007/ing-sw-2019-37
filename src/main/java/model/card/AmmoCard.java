package model.card;

import java.util.ArrayList;

public class AmmoCard {
    public ArrayList<AmmoColor> ammo;
    public boolean powerup;

    public AmmoCard(AmmoColor a,AmmoColor b, AmmoColor c){
        ammo = new ArrayList<AmmoColor>();
        ammo.add(a);
        ammo.add(b);
        ammo.add(c);
        this.powerup=false;
    }
    public AmmoCard(AmmoColor a,AmmoColor b){
        ammo = new ArrayList<AmmoColor>();
        ammo.add(a);
        ammo.add(b);
        powerup=true;
    }

    public ArrayList<AmmoColor> getAmmoList(){ //metodo che ritorna la lista delle munizioni per ogni AmmoCard
        return ammo;
    }

    public boolean getPowerup(){
        return powerup;
    }
}
