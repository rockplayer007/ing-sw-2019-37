package view.GUI;

import model.card.AmmoColor;
import model.player.Player;
import model.player.PlayerBoard;
import javax.swing.*;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerBoardPanel extends JPanel{

    private List<JLabel> ammoColors = new ArrayList<>();
    private List<WeaponButton> weaponButtons = new ArrayList<>();
    public PlayerBoardPanel(List<Player> players) {
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
            board.setSize(771,204);
            this.add(board);

            ammoColors=new ArrayList<>();
            Map<AmmoColor,Integer> ammos = players.get(i).getAmmo();
            for (Map.Entry<AmmoColor, Integer> entry : ammos.entrySet()) {
                AmmoColor ammoColor = entry.getKey();
                Integer n = entry.getValue();
                for(int c=0;c<n;c++){
                    JLabel label= new JLabel();
                    label.setSize(20,20);
                    StyleSheet s = new StyleSheet();
                    label.setBackground(s.stringToColor(ammoColor.toString()));
                    label.setOpaque(true);
                    label.repaint();
                    int size=ammoColors.size();
                    if(size>0)
                        label.setLocation(ammoColors.get(size-1).getX(),ammoColors.get(size-1).getY()+22);
                    else
                        label.setLocation(775,y+2);
                    this.add(label);
                    ammoColors.add(label);
                }
            }
            for(int c=0;c<players.get(i).getWeapons().size();c++){
                WeaponButton weaponButton= new WeaponButton(players.get(i).getWeapons().get(c));
                weaponButton.setSize(150,205);
                int size= weaponButtons.size();
                if(size>0){
                    weaponButton.setLocation(weaponButtons.get(size-1).getX()+152,y);}
                else
                    weaponButton.setLocation(800,y);
                weaponButton.setContentAreaFilled(false);
                weaponButton.setBorder(null);
                weaponButton.setFocusPainted(false);
                ImageIcon imageIcon = new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                        +File.separatorChar+"resources"+File.separatorChar+"powerup"+File.separatorChar + players.get(i).getWeapons().get(c).getName() + ".png");
                weaponButton.setIcon(imageIcon);
                weaponButton.setOpaque(false);
                weaponButtons.add(weaponButton);
                this.add(weaponButton);
            }
            y=y+205;

    }
}
}
