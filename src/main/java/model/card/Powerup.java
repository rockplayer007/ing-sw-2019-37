package model.card;

public class Powerup extends Card {
    private Effect effect;
    private AmmoColor ammo;

    public Powerup (String name, String description, Effect effect, AmmoColor ammo){
        super(name,description);
        this.effect=effect;
        this.ammo=ammo;
    }
    public AmmoColor getAmmo(){
        return ammo;
    }

    public Effect getEffect() {
        return effect;
    }

}
