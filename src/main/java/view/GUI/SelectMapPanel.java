package view.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectMapPanel extends JPanel {

    private JRadioButton [] map = new JRadioButton[4];
    private ButtonGroup mapGroup = new ButtonGroup();
    private String mapSelected;
    private JButton [] imMap = new JButton[4];
    private JLabel [] mapDescription = new JLabel[4];
    private int mapSel;
    private static final Logger logger = Logger.getLogger(SelectMapPanel.class.getName());

    public SelectMapPanel(Map<Integer, String> maps){
        JLabel text;
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        text = new JLabel("Select Map");
        Font f = new Font("Phosphate", Font.BOLD, 40);
        text.setForeground(Color.white);
        text.setFont(f);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.insets = new Insets(5, 0, 50, 0);
        this.add(text, gbc);
        int i=0;
        int x=0;
        int y=3;
        for(Map.Entry<Integer,String> entry: maps.entrySet()){
            Integer key = entry.getKey();
            String value = entry.getValue();
            this.map[i]=new JRadioButton("Map "+key);
            gbc.gridx=x;
            gbc.gridy=y;
            gbc.anchor = GridBagConstraints.CENTER;
            map[i].addActionListener(this::actionPerformed);
            map[i].setForeground(Color.white);
            map[i].setBackground(Color.DARK_GRAY);
            map[i].setBorder(null);
            this.add(this.map[i], gbc);
            mapGroup.add(map[i]);
            mapDescription[i]=new JLabel(value);
            gbc.gridx=x;
            gbc.gridy=y-2;
            gbc.insets = new Insets(0, 0, 20, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            mapDescription[i].setForeground(Color.white);
            this.add(this.mapDescription[i], gbc);
            imMap[i]= new JButton();
            gbc.gridx=x;
            gbc.gridy=y-1;
            gbc.anchor = GridBagConstraints.CENTER;
            Image img= null;
            try {
                img= ImageIO.read(SelectCardPanel.class.getResourceAsStream("/maps/map"+key+"icon.png"));
            } catch (IOException e){
                logger.log(Level.WARNING, "Image not loaded correctly", e);
            }
            final int c=i;
            this.imMap[i].setIcon(new ImageIcon(img));
            this.imMap[i].setOpaque(true);
            imMap[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    map[c].setSelected(true);
                    mapSelected=map[c].getText();
                    mapSel =c;
                }});
            this.add(this.imMap[i], gbc);
        i++;
        x++;
        }
        this.map[0].setSelected(true);
    }


    public void actionPerformed(ActionEvent e) {
        mapSelected = e.getActionCommand();
        switch (mapSelected){
            case "Map 0": mapSel =0;break;
            case "Map 1": mapSel =1;break;
            case "Map 2": mapSel =2;break;
            case "Map 3": mapSel =3;break;
            default: mapSel=7;
        }
    }

    public int getMapSelected(){
        return mapSel;
    }

}
