package view.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameSetVisibleActionListner implements ActionListener {
    private JFrame jFrame;
    public FrameSetVisibleActionListner(JFrame jFrame){
        this.jFrame=jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jFrame.setVisible(true);
    }
}
