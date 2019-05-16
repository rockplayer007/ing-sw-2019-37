package view.GUI;

import model.card.Weapon;

import javax.swing.*;

public class JWeaponButton extends JButton {
    private String weapon;
    public JWeaponButton(String weapon){
        this.weapon=weapon;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }
}
