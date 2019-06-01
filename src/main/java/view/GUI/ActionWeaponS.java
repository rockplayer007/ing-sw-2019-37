package view.GUI;

import network.client.MainClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class ActionWeaponS implements ActionListener {

        private MainClient mainClient;
        private int n;
        private JFrame frame;
        private MapPanel mapPanel;
        private String name;

        public ActionWeaponS (MainClient mainClient,int n,JFrame frame,MapPanel mapPanel,String name){
            this.mainClient=mainClient;
            this.n=n;
            this.frame=frame;
            this.mapPanel=mapPanel;
            this.name=name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mainClient.sendSelectedCard(n);
            frame.setVisible(false);
            mapPanel.setWeaponSelected(name);
        }
    }

