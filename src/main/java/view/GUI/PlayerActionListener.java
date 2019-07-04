package view.GUI;

import network.client.MainClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerActionListener implements ActionListener {
    private MapPanel mapPanel;
    private MainClient mainClient;
    private int selected;

    public PlayerActionListener(MapPanel mapPanel, MainClient mainClient,int selected){
        this.mainClient=mainClient;
        this.mapPanel=mapPanel;
        this.selected=selected;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mapPanel.removePlayerActions();
        mainClient.sendSelectedItem(selected);
        mapPanel.enablePlayers();
        if(mapPanel.getAttack()) {
            mapPanel.addSound("shoot");
            mapPanel.setAttack(false);
        }
    }
}
