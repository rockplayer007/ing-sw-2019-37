package view.GUI;

import model.card.Powerup;

public class PowerupButton {
private Powerup powerup;

    public PowerupButton(Powerup powerup){
        this.powerup=powerup;
    }

    public Powerup getPower() {
        return powerup;
    }

    public void setPower(Powerup powerup) {
        this.powerup = powerup;
    }
}

