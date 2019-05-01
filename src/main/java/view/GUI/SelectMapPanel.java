package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class SelectMapPanel extends JPanel {

    private JLabel text;
    private JRadioButton [] map = new JRadioButton[4];
    private ButtonGroup mapGroup = new ButtonGroup();
    private String mapSelected;
    private JButton []imgmap = new JButton[4];
    private JLabel [] mapDescription = new JLabel[4];
    private int mapSel;


    private void build(Map<Integer, String> maps){
        this.setLayout(new GridBagLayout());
        Color color= new Color(131,105,83);
        this.setBackground(color);
        GridBagConstraints gbc = new GridBagConstraints();
        this.text = new JLabel("Select Map");
        Font f = new Font("Phosphate", Font.BOLD, 40);
        this.text.setFont(f);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.insets = new Insets(5, 0, 50, 0);
        this.add(this.text, gbc);
        int i=0,x=0,y=3;
        String url=null;
        for (int key : maps.keySet()) {
            String value = maps.get(key);
        this.map[i]=new JRadioButton("Map "+key);
            gbc.gridx=x;
            gbc.gridy=y;
            gbc.anchor = GridBagConstraints.CENTER;
            map[i].addActionListener(this::actionPerformed);
            this.add(this.map[i], gbc);
            mapGroup.add(map[i]);
            mapDescription[i]=new JLabel(value);
            gbc.gridx=x;
            gbc.gridy=y-2;
            gbc.insets = new Insets(0, 0, 20, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            this.add(this.mapDescription[i], gbc);
            imgmap[i]= new JButton();
            gbc.gridx=x;
            gbc.gridy=y-1;
            gbc.anchor = GridBagConstraints.CENTER;
            ImageIcon img=null;
            switch (key){
                case 0: img = new ImageIcon("./src/main/resources/map0.png");break;
                case 1: img = new ImageIcon("./src/main/resources/map1.png");break;
                case 2: img = new ImageIcon("./src/main/resources/map2.png");break;
                case 3: img = new ImageIcon("./src/main/resources/map3.png");break;
            }
            final int c=i;
            this.imgmap[i].setIcon(img);
            this.imgmap[i].setOpaque(true);
            //this.imgmap[i].setBorderPainted(false);
            imgmap[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    map[c].setSelected(true);
                    mapSelected=map[c].getText();
                    mapSel =c;
                }});
            this.add(this.imgmap[i], gbc);
        i++;
        x++;
        }
        this.map[0].setSelected(true);
    }

    public SelectMapPanel(Map<Integer, String> maps){
        this.build(maps);
    }


    public void actionPerformed(ActionEvent e) {
        mapSelected = e.getActionCommand();
        switch (mapSelected){
            case "Map 0": mapSel =0;break;
            case "Map 1": mapSel =1;break;
            case "Map 2": mapSel =2;break;
            case "Map 3": mapSel =3;break;
        }
    }

    public int getMapSelected(){
        return mapSel;
    }

}
