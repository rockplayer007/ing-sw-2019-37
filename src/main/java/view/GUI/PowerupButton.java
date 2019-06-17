package view.GUI;

import model.card.Powerup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PowerupButton extends JButton{
private Powerup powerup;

    public PowerupButton(Powerup powerup){
        this.powerup=powerup;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon imageIcon=new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                        + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                        File.separatorChar + powerup.getName()+powerup.getAmmo()+ ".png");
                JOptionPane.showMessageDialog(null,powerup.getName()+"\n"+powerup.getDescription(),powerup.getName(),JOptionPane.INFORMATION_MESSAGE,imageIcon);
            }
        });
    }

    public Powerup getPower() {
        return powerup;
    }

    public void setPower(Powerup powerup) {
        this.powerup = powerup;
    }
}

