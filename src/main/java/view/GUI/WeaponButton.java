package view.GUI;

import model.card.Weapon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class WeaponButton extends JButton {
    private Weapon weapon;
    public WeaponButton(Weapon weapon){
        this.weapon=weapon;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JFrame frame=new JFrame(weapon.getName());
                ImageIcon imageIcon=new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                        + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                        File.separatorChar + weapon.getName() + ".png");
                JOptionPane.showMessageDialog(null,weapon.getName()+"\n"+weapon.getDescription(),weapon.getName(),JOptionPane.INFORMATION_MESSAGE,imageIcon);
            }
        });
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
