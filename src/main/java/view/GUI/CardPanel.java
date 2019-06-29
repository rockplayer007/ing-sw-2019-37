package view.GUI;

import model.card.Effect;
import model.card.Weapon;
import network.client.MainClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardPanel extends JPanel {

    private List<JButton> buttonEffect;
    private Weapon weapon;
    private static final Logger logger = Logger.getLogger(CardPanel.class.getName());

    public CardPanel(Weapon weapon, List<Effect> effects, MainClient mainClient, JFrame frame){
        this.weapon=weapon;
        this.setLayout(null);
        createButtons();
        for(int i=0;i<effects.size();i++){
            //int x=i;
            buttonEffect.get(effects.get(i).getId()).setVisible(true);
            buttonEffect.get(effects.get(i).getId()).addActionListener(new EffectListener(mainClient,frame,i));
            /*buttonEffect.get(effects.get(i).getId()).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedItem(x);
                    frame.setVisible(false);
                }
            });*/

        }

        JLabel image = new JLabel();
        image.setSize(196,264);
        image.setLocation(0,0);
        image.setBorder(null);
        //image.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"powerup"+File.separatorChar+weapon.getName()+".png"));
        Image img= null;
        try {
            img= ImageIO.read(CardPanel.class.getResourceAsStream("/powerup/"+weapon.getName()+".png"));
        } catch (IOException e){
            logger.log(Level.WARNING, "Image not loaded correctly", e);
        }
        this.add(image);
    }

    private void createButtons(){
        buttonEffect=new ArrayList<>();
        JButton button1=new JButton();
        createButton(button1,193,64,1,117);
        if(weapon.getNumberOfEffect()==2){
           JButton button2=new JButton();
           createButton(button2,193,64,1,187);
        }
        else {
            JButton button3 =new JButton();
            createButton(button3,86,77,8,187);
            JButton button4 =new JButton();
            createButton(button4,86,77,100,187);
        }
    }

    private void createButton(JButton button,int width,int height,int x,int y){
        button.setVisible(false);
        button.setSize(width,height);
        button.setLocation(x,y);
        Border border= BorderFactory.createLineBorder(Color.yellow,3);
        button.setBorder (border);
        button.setBorderPainted (true);
        button.setContentAreaFilled(false);
        buttonEffect.add(button);
        this.add(button);
    }
}
