package view.GUI;

import model.player.Player;

import javax.swing.*;

public class JPlayerButton extends JButton {
    private Player player;

    public JPlayerButton(Player player){
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }
}
