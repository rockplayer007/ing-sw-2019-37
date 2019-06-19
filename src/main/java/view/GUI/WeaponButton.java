package view.GUI;

import model.card.Weapon;

import javax.swing.*;
import java.io.File;

public class WeaponButton extends JButton {
    private Weapon weapon;
    public WeaponButton(Weapon weapon){
        this.weapon=weapon;
        ImageIcon imageIcon=new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                File.separatorChar + weapon.getName() + ".png");
        this.addActionListener(new CardButtonListener(imageIcon,weapon));
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
