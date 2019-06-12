package view.GUI;

import network.client.MainClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseActionListener implements ActionListener {
    private MainClient mainClient;
    private JFrame frame;
    private int x;
    public ChooseActionListener(MainClient mainClient,JFrame frame,int x){
        this.mainClient=mainClient;
        this.frame=frame;
        this.x=x;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            mainClient.sendSelectedAmmoColor(x);
            frame.setVisible(false);
    }
}
