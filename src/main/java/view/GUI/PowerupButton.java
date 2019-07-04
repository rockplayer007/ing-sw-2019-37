package view.GUI;

import model.card.Powerup;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerupButton extends JButton{
private Powerup powerup;
    private static final Logger logger = Logger.getLogger(PowerupButton.class.getName());
    public PowerupButton(Powerup powerup){
        this.powerup=powerup;
        Image img= null;
        try {
            img= ImageIO.read(PowerupButton.class.getResourceAsStream("/powerup/"+powerup.getName()+powerup.getAmmo()+".png"));
        } catch (IOException e){
            logger.log(Level.WARNING, "Image not loaded correctly", e);
        }
        this.addActionListener(new CardButtonListener(new ImageIcon(img),powerup));
    }

    public Powerup getPower() {
        return powerup;
    }

    public void setPower(Powerup powerup) {
        this.powerup = powerup;
    }
}

