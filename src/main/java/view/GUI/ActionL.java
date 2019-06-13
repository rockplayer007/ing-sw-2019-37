package view.GUI;

import network.client.MainClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionL implements ActionListener {

    private MainClient mainClient;
    private int n;
    private JFrame frame;
    private MapPanel mapPanel;

            public ActionL (MainClient mainClient,int n,JFrame frame,MapPanel mapPanel){
                this.mainClient=mainClient;
                this.n=n;
                this.frame=frame;
                this.mapPanel=mapPanel;
            }

    @Override
    public void actionPerformed(ActionEvent e) {
            mainClient.sendSelectedCard(n);
            frame.setVisible(false);
            mapPanel.addActionInfo("");

    }
}
