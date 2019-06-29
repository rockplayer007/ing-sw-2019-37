package view.GUI;

import network.client.MainClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectCardPanel extends JPanel{

    private static final Logger logger = Logger.getLogger(SelectCardPanel.class.getName());

    public SelectCardPanel(List<String> cards, boolean optional, MainClient mainClient,JFrame frame,MapPanel mapPanel){
        this.setLayout(new GridLayout(1,4));
        for (int i=0;i<cards.size();i++){
            JButton button=new JButton();
            button.setSize(196, 264);
              /*  button.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                        + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                        File.separatorChar + cards.get(i) + ".png"));
                        */
            Image img= null;
            try {
                img= ImageIO.read(SelectCardPanel.class.getResourceAsStream("/powerup/"+cards.get(i)+".png"));
            } catch (IOException e){
                logger.log(Level.WARNING, "Image not loaded correctly", e);
            }
            button.setIcon(new ImageIcon(img));
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorder(null);
            button.setFocusPainted(false);
            button.addActionListener(new ActionWeaponS(mainClient,i,frame,mapPanel,cards.get(i)));
            this.add(button);


        }
        if(optional) {
            JButton opt = new JButton("Don't use");
            opt.setSize(196, 264);
            opt.setBackground(Color.WHITE);
            opt.setOpaque(false);
            int s = cards.size();
            opt.addActionListener(new ActionL(mainClient,s,frame,mapPanel));
            this.add(opt);
        }

    }
}
