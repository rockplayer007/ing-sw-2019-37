package model.card;

public class Powerup extends Card {
    private int key; //Targeting Scope=1,Newton=2,Tagback Grenade=3,Teleporter=4
    private AmmoColor ammo;

    public Powerup (String name, String description, AmmoColor ammo, int key){
        super(name,description);
        this.ammo=ammo;
        this.key=key;
    }
    public AmmoColor getAmmoColor(){
        return ammo;
    }

    public int getKey(){
        return key;
    }

    public void operation(){
        switch (key){
            case 1: //it can only be activated when attacking
                    //method for paying ammunition cube
                    //method choose target
                    //method of dealing 1 damage
                    break;
            case 2: //can be used on its own only
                    //method choose target
                    //method move target (1.2 squares in one direction)
                    break;
            case 3: //when you receive player damage that you CAN SEE
                    //damage
                    break;
            case 4: //can be used on its own only
                    //method move target
                    break;
            default:;
        }

    }
}
