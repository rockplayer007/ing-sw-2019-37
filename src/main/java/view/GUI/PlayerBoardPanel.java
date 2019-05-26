package view.GUI;

import model.player.Player;
import model.player.PlayerBoard;

import javax.swing.*;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerBoardPanel extends JPanel{

    public PlayerBoardPanel(List<Player> players){
        this.setLayout(null);
        int y=0;
        for(int i=0;i<players.size();i++){
           PlayerBoard playerBoard=players.get(i).getPlayerBoard();
           int x=12;
           for (int j=0;j<playerBoard.getHp().size();j++){
               JLabel point = new JLabel();
               point.setLocation(x,i*205+74);
               point.setSize(40,52);
               x=x+48;
               StyleSheet s = new StyleSheet();
               point.setBackground(s.stringToColor(playerBoard.getHp().get(j).getColor().toString()));
               this.add(point);
           }
           JButton board = new JButton();
            board.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                    +File.separatorChar+"resources"+File.separatorChar +"heroes"+File.separatorChar+players.get(i).getHero().getName()+"board.png"));
            board.setOpaque(false);
            board.setLocation(0,y);
            board.setSize(820,204);
            this.add(board);
            y=y+205;
        }

    }
}
