package view.GUI;

import model.card.Card;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardButtonListener implements ActionListener {
    private ImageIcon imageIcon;
    private Card card;


    public CardButtonListener(ImageIcon imageIcon,Card card){
        this.imageIcon=imageIcon;
        this.card=card;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JTextArea textArea = new JTextArea(card.getName()+"\n"+card.getDescription(), 10, 60);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null,textArea,card.getName(),JOptionPane.INFORMATION_MESSAGE,imageIcon);
    }
}
