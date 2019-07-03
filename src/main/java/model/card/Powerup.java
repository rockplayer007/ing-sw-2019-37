package model.card;

public class Powerup extends Card {
    private Effect effect;
    private AmmoColor ammo;

    /**
     * Constuctor
     * @param name name of powerup
     * @param description description of powerup
     * @param effect effect that can use
     * @param ammo can be us as ammo whit which color
     */
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
