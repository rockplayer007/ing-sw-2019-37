package view.GUI;

import model.player.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeroDescriptionListener implements ActionListener {
    private ImageIcon imageIcon;
    private Player player;


    public HeroDescriptionListener(ImageIcon imageIcon,Player player){
        this.imageIcon=imageIcon;
        this.player=player;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JTextArea textArea = new JTextArea(player.getHero().getName()+"  -  "+player.getNickname()+"\n"+player.getHero().getDescription(), 12, 60);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(null,textArea,player.getNickname()+" - "+player.getHero().getName(),JOptionPane.INFORMATION_MESSAGE,imageIcon);
    }
}
