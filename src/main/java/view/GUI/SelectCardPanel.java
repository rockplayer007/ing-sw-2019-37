package view.GUI;

import network.client.MainClient;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class SelectCardPanel extends JPanel{


    public SelectCardPanel(List<String> cards, boolean optional, MainClient mainClient,JFrame frame,MapPanel mapPanel){
        this.setLayout(new GridLayout(1,4));
        for (int i=0;i<cards.size();i++){
            JButton button=new JButton();
            button.setSize(196, 264);
                button.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                        + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                        File.separatorChar + cards.get(i) + ".png"));
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
            opt.setBackground(Color.darkGray);
            int s = cards.size();
            opt.addActionListener(new ActionL(mainClient,s,frame));
            this.add(opt);
        }

    }
}
