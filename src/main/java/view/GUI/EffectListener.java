package view.GUI;

import network.client.MainClient;
import network.messages.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EffectListener implements ActionListener {
    private MainClient mainClient;
    private JFrame jFrame;
    private int x;
    public EffectListener(MainClient mainClient, JFrame jFrame, int x){
        this.jFrame=jFrame;
        this.mainClient=mainClient;
        this.x=x;

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        mainClient.sendSelectedItem(x, Message.Content.EFFECT_RESPOSNSE);
        jFrame.setVisible(false);
    }
}
