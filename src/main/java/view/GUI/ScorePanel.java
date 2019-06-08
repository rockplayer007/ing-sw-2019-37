package view.GUI;

import model.player.Player;

import javax.swing.*;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ScorePanel extends JPanel {

    public ScorePanel(Map<Player,Integer> score){
        JLabel podium;
        this.setLayout(null);
        this.setBackground(Color.CYAN);
        podium= new JLabel();
        podium.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                + "main" + File.separatorChar + "resources" + File.separatorChar + "podium.png"));
        podium.setLocation(0,200);
        podium.setSize(750,283);
        podium.setOpaque(false);
        this.add(podium);

        Point [] points = new Point[3];
        points[0]=new Point(290,45);
        points[1]=new Point(70,120);
        points[2]=new Point(512,140);
        int i=0;
        int y=550;
        for(Map.Entry<Player,Integer> entry: score.entrySet()) {
            Player key = entry.getKey();
            Integer value = entry.getValue();
            if(i<3){
                JLabel playerIcon= new JLabel();
                playerIcon.setSize(160,165);
                playerIcon.setLocation(points[i]);
                playerIcon.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                        + "main" + File.separatorChar + "resources" + File.separatorChar + "heroes"+
                        File.separatorChar+ key.getHero().getName()+ "big.png"));
                this.add(playerIcon);
            }
            JLabel ranking= new JLabel("Ranking");
            Font font=new Font("Phosphate", Font.BOLD, 25);
            ranking.setFont(font);
            ranking.setLocation(300,500);
            ranking.setSize(150,50);
            this.add(ranking);
            JLabel text =new JLabel(i+1+"°");
            text.setSize(50,20);
            text.setLocation(250,y);
            this.add(text);
            JLabel nickname =new JLabel(key.getNickname());
            StyleSheet styleSheet=new StyleSheet();
            nickname.setForeground(styleSheet.stringToColor(key.getColor().toString()));
            nickname.setLocation(300,y);
            nickname.setSize(150,20);
            nickname.setFont(new Font("",Font.BOLD,16));
            this.add(nickname);
            JLabel point =new JLabel("PT.    "+value.toString());
            point.setLocation(450,y);
            point.setSize(100,20);
            this.add(point);
            i++;
            y+=25;
        }

    }
}
