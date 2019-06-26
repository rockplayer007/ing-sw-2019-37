package view.GUI;

import network.client.MainClient;
import network.messages.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionResponseListener implements ActionListener {
    private MapPanel mapPanel;
    private MainClient mainClient;
    private int x;
    private JLabel jLabel;
    private JLabel jLabel2;

    public  ActionResponseListener(MapPanel mapPanel, MainClient mainClient, int x, JLabel jLabel, JLabel jLabel2){
        this.mainClient=mainClient;
        this.mapPanel=mapPanel;
        this.x=x;
        this.jLabel=jLabel;
        this.jLabel2=jLabel2;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            mapPanel.resetActions();
            mainClient.sendSelectedItem(x, Message.Content.ACTION_REQUEST);//vedere messaggio giusto
            jLabel.setVisible(false);
            jLabel2.setVisible(false);
    }
}
