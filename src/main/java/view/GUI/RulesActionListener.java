package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class RulesActionListener implements ActionListener {

    public RulesActionListener(){
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
               // File myFile = new File(getClass().getResourceAsStream("/rule.pdf"));
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResourceAsStream("/rule.pdf").toString());

              //  "./src/main/resources/rule.pdf
                Desktop.getDesktop().open(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else JOptionPane.showMessageDialog(null,"Desktop functionality does not work","RULES",JOptionPane.WARNING_MESSAGE);
    }
}
