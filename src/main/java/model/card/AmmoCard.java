package model.card;
import java.util.ArrayList;
import java.util.List;

public class AmmoCard extends Card{
    private List<AmmoColor> ammo= new ArrayList<>();
    private boolean hasPowerup;

    /**
     * is constructor for AmmoCard of type [Ammo][Ammo][Ammo]
     * @param id id of card
     * @param a type of ammo
     * @param b type of ammo
     * @param c type of ammo
     */
    public AmmoCard(String id,AmmoColor a,AmmoColor b, AmmoColor c){
        super(id, "");
        ammo.add(a);
        ammo.add(b);
        ammo.add(c);
        this.hasPowerup=false;
    }

    /**
     * is constructor for AmmoCard of type [Ammo][Ammo][Powerup]
     * @param id id of card
     * @param a type of ammo
     * @param b type of ammo
     */
    public AmmoCard(String id, AmmoColor a,AmmoColor b){
        super(id, "");
        ammo.add(a);
        ammo.add(b);
        hasPowerup=true;
    }

    public List<AmmoColor> getAmmoList(){ //metodo che ritorna la lista delle munizioni per ogni AmmoCard
        return ammo;
    }

    public boolean hasPowerup(){
        return hasPowerup;
    }

}
