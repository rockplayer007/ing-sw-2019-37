package view.GUI;

import model.card.AmmoColor;
import model.player.Player;
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
           int x=52;
           for (int j=0;j<players.get(i).getPlayerBoard().getHpColor().size();j++){
               JLabel point = new JLabel();
               point.setLocation(x,i*142+50);
               point.setSize(27,38);
               StyleSheet s = new StyleSheet();
               point.setBackground(s.stringToColor(players.get(i).getPlayerBoard().getHpColor().get(j).toString()));
               point.setOpaque(true);
               this.add(point);
               x=x+33;
           }
            x=121;
            if(players.get(i).getActionStatus().name().equals("FRENETICACTIONS1") ||
                    players.get(i).getActionStatus().name().equals("FRENETICACTIONS2"))
                x=130;
           for (int k=0;k<players.get(i).getPlayerBoard().getDeathTimes();k++){
               JLabel death = new JLabel();
               death.setLocation(x,i*142+103);
               death.setSize(27,32);
               death.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                       +File.separatorChar + "resources"+File.separatorChar + "heroes" + File.separatorChar+"skull.png"));
               death.setOpaque(false);
               this.add(death);
               x=x+31;
           }

           x=279;
           for (int m=0;m<players.get(i).getPlayerBoard().getMarksColor().size();m++){
               JLabel mark=new JLabel();
               mark.setLocation(x,i*142+3);
               mark.setSize(23,28);
               StyleSheet s = new StyleSheet();
               mark.setBackground(s.stringToColor(players.get(i).getPlayerBoard().getMarksColor().get(m).toString()));
               mark.setOpaque(true);
               this.add(mark);
               x=x+25;
           }

           JLabel board = new JLabel();
            board.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                    +File.separatorChar+"resources"+File.separatorChar +"heroes"+File.separatorChar+players.get(i).getHero().getName()+"board.png"));
            if(players.get(i).getActionStatus().name().equals("FRENETICACTIONS1") ||
                    players.get(i).getActionStatus().name().equals("FRENETICACTIONS2"))
                board.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                        +File.separatorChar+"resources"+File.separatorChar +"heroes"+File.separatorChar+players.get(i).getHero().getName()+"Freneticboard.png"));
            board.setOpaque(false);
            board.setLocation(0,y);
            board.setSize(577,141);
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
                        label.setLocation(ammoColors.get(size-1).getX()+22,ammoColors.get(size-1).getY());
                    else
                        label.setLocation(580,y);
                    this.add(label);
                    ammoColors.add(label);
                }
            }
            weaponButtons=new ArrayList<>();
            for(int c=0;c<players.get(i).getWeapons().size();c++){
                WeaponButton weaponButton= new WeaponButton(players.get(i).getWeapons().get(c));
                weaponButton.setSize(80,120);
                int size= weaponButtons.size();
                if(size>0){
                    weaponButton.setLocation(weaponButtons.get(size-1).getX()+82,board.getY()+21);
                }
                else
                    weaponButton.setLocation(580,board.getY()+21);
                weaponButton.setContentAreaFilled(false);
                weaponButton.setBorder(null);
                weaponButton.setFocusPainted(false);
                weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                        + File.separatorChar + "resources" + File.separatorChar + "weapon" + File.separatorChar +
                        players.get(i).getWeapons().get(c).getName() + ".png"));
                weaponButton.setOpaque(false);
                weaponButtons.add(weaponButton);
                this.add(weaponButton);
            }
            y=y+142;

    }
}
}
