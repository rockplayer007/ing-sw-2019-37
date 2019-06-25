package view.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RulesActionListener implements ActionListener {
    private JFrame frame;
    public RulesActionListener(JFrame frame){
        this.frame= frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            frame.pack();
            frame.setVisible(true);
    }
}
