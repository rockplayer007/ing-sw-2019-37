package view.GUI;

import model.card.Powerup;

import javax.swing.*;
import java.io.File;

public class PowerupButton extends JButton{
private Powerup powerup;

    public PowerupButton(Powerup powerup){
        this.powerup=powerup;
        ImageIcon imageIcon=new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                File.separatorChar + powerup.getName()+powerup.getAmmo()+ ".png");
        this.addActionListener(new CardButtonListener(imageIcon,powerup));
    }

    public Powerup getPower() {
        return powerup;
    }

    public void setPower(Powerup powerup) {
        this.powerup = powerup;
    }
}

