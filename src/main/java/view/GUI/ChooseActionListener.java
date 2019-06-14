package view.GUI;

import network.client.MainClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseActionListener implements ActionListener {
    private MainClient mainClient;
    private JFrame frame;
    private int x;
    private MapPanel mapPanel;
    public ChooseActionListener(MainClient mainClient,JFrame frame,int x,MapPanel mapPanel){
        this.mainClient=mainClient;
        this.frame=frame;
        this.x=x;
        this.mapPanel=mapPanel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            mainClient.sendSelectedAmmoColor(x);
            frame.setVisible(false);
            mapPanel.addActionInfo("");
    }
}
