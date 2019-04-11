package model.card;
import java.util.ArrayList;
import java.util.List;

public class AmmoCard extends Card{
    private List<AmmoColor> ammo= new ArrayList<>();
    private boolean hasPowerup;

    public AmmoCard(AmmoColor a,AmmoColor b, AmmoColor c){
        ammo.add(a);
        ammo.add(b);
        ammo.add(c);
        this.hasPowerup=false;
    }
    public AmmoCard(AmmoColor a,AmmoColor b){
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
