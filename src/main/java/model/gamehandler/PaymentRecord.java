package model.gamehandler;

import model.card.AmmoColor;
import model.card.Powerup;

import java.util.List;

public class PaymentRecord {
    private List<Powerup> usedPowerups;
    private List<AmmoColor> usedAmmo;

    public PaymentRecord(List<Powerup> usedPowerups, List<AmmoColor> usedAmmo){
        this.usedAmmo = usedAmmo;
        this.usedPowerups = usedPowerups;
    }

    public List<Powerup> getUsedPowerups() {
        return usedPowerups;
    }

    public List<AmmoColor> getUsedAmmo() {
        return usedAmmo;
    }

}