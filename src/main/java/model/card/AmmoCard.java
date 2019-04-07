package model.card;

import java.util.ArrayList;

public class AmmoCard extends Card{
    public ArrayList<AmmoColor> ammo;
    public boolean hasPowerup;

    public AmmoCard(AmmoColor a,AmmoColor b, AmmoColor c){
        ammo = new ArrayList<AmmoColor>();
        ammo.add(a);
        ammo.add(b);
        ammo.add(c);
        this.hasPowerup=false;
    }
    public AmmoCard(AmmoColor a,AmmoColor b){
        ammo = new ArrayList<AmmoColor>();
        ammo.add(a);
        ammo.add(b);
        hasPowerup=true;
    }

    public ArrayList<AmmoColor> getAmmoList(){ //metodo che ritorna la lista delle munizioni per ogni AmmoCard
        return ammo;
    }

    public boolean hasPowerup(){
        return hasPowerup;
    }
}
