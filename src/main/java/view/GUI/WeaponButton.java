package view.GUI;

import model.card.Weapon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeaponButton extends JButton {
    private Weapon weapon;
    public WeaponButton(Weapon weapon){
        this.weapon=weapon;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame=new JFrame(weapon.getName());
                JOptionPane.showMessageDialog(frame,weapon.getName()+"\n"+weapon.getDescription());
                //fare un jdialog
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
