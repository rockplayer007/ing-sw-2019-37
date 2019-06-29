package view.GUI;

import model.card.Weapon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeaponButton extends JButton {
    private Weapon weapon;
    private static final Logger logger = Logger.getLogger(WeaponButton.class.getName());
    public WeaponButton(Weapon weapon){
        this.weapon=weapon;
        /*ImageIcon imageIcon=new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                File.separatorChar + weapon.getName() + ".png");*/
        Image img= null;
        try {
            img= ImageIO.read(WeaponButton.class.getResourceAsStream("/powerup/"+weapon.getName()+".png"));
        } catch (IOException e){
            logger.log(Level.WARNING, "Image not loaded correctly", e);
        }
        this.addActionListener(new CardButtonListener(new ImageIcon(img),weapon));
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
