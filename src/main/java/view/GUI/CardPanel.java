package view.GUI;

import model.card.Effect;
import model.card.Weapon;
import network.client.MainClient;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardPanel extends JPanel {

    private Image imageCard;
    private List<JButton> buttonEffect=new ArrayList<>();
    private Weapon weapon;

    public CardPanel(Weapon weapon, List<Effect> effects, MainClient mainClient, JFrame frame){
        this.weapon=weapon;
        this.setLayout(null);
        createButtons();
        for(int i=0;i<effects.size();i++){
            int x=i;
            buttonEffect.get(effects.get(i).getId()).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedEffect(x);
                    frame.setVisible(false);
                }
            });
            buttonEffect.get(effects.get(i).getId()).setVisible(true);
        }

        JLabel image = new JLabel();
        image.setSize(196,264);
        image.setLocation(0,0);
        image.setBorder(null);
        image.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"powerup"+File.separatorChar+weapon.getName()+".png"));
        this.add(image);
    }

    private void createButtons(){
        JButton jButton=new JButton();
        jButton.setSize(193,64);
        jButton.setLocation(1,117);
        Border border= BorderFactory.createLineBorder(Color.yellow,3);
        jButton.setBorder (border);
        jButton.setBorderPainted (true);
        jButton.setContentAreaFilled(false);
        jButton.setVisible(false);
        buttonEffect.add(jButton);
        this.add(jButton);
        if(weapon.getNumberOfEffect()==2){
            jButton.setLocation(1,195);
            buttonEffect.add(jButton);
            this.add(jButton);
        }
        else {
            jButton.setSize(86,77);
            jButton.setLocation(8,187);
            buttonEffect.add(jButton);
            this.add(jButton);
            jButton.setLocation(100,187);
            buttonEffect.add(jButton);
            this.add(jButton);
        }
    }

}
