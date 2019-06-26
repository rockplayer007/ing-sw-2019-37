package view.GUI;

import network.client.MainClient;
import network.messages.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SquareActionListener implements ActionListener {
    private MapPanel mapPanel;
    private MainClient mainClient;
    private int x;

    public SquareActionListener(MapPanel mapPanel,MainClient mainClient, int x){
        this.mainClient=mainClient;
        this.mapPanel=mapPanel;
        this.x=x;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            mapPanel.resetRooms();
            mainClient.sendSelectedItem(x, Message.Content.SQUARE_RESPONSE);
            mapPanel.addActionInfo("");
    }
}
