package view.GUI;

import model.card.Weapon;

import javax.swing.*;

public class WeaponButton extends JButton {
    private Weapon weapon;
    public WeaponButton(Weapon weapon){
        this.weapon=weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
