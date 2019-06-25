package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RulesPanel  extends JPanel {
    public RulesPanel(){
        this.setLayout(new GridLayout(1,12));
        for(int i=0;i<12;i++){
            JLabel rule=new JLabel();
            rule.setSize(500,700);
            rule.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar
                    + "rules" + File.separatorChar + "rules" +(i+1)+ ".png"));
            rule.setOpaque(true);
            this.add(rule);
        }
    }
}
