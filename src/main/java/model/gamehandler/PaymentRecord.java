package model.gamehandler;

import model.card.AmmoColor;
import model.card.Powerup;

import java.util.List;

public class PaymentRecord {
    private List<Powerup> usedPowerups;
    private List<AmmoColor> usedAmmo;

    /**
     * Constructor
     * @param usedPowerups powerups used as ammo for payment
     * @param usedAmmo ammo used for payment
     */
    public PaymentRecord(List<Powerup> usedPowerups, List<AmmoColor> usedAmmo){
        this.usedAmmo = usedAmmo;
        this.usedPowerups = usedPowerups;
    }

    List<Powerup> getUsedPowerups() {
        return usedPowerups;
    }

    List<AmmoColor> getUsedAmmo() {
        return usedAmmo;
    }

}
