package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MapPanel extends JPanel{
    private JButton x0y0;
    private JButton x1y0;
    private JButton x2y0;
    private JButton x3y0;
    private JButton x0y1;
    private JButton x1y1;
    private JButton x2y1;
    private JButton x3y1;
    private JButton x0y2;
    private JButton x1y2;
    private JButton x2y2;
    private JButton x3y2;
    private Image image;
    private JButton run;
    private JButton grab;
    private JButton playerboard;
    private JTextArea shoo;
    private JComboBox comboBox;


    private void build() {
        this.setLayout(null);
        GridBagConstraints gbc = new GridBagConstraints();

        this.x0y0=new JButton("button 1");
        this.x0y0.setSize(260,228);
        this.x0y0.setLocation(2,0);
        this.x0y0.setBorder(null);
        this.x0y0.setOpaque(false);
        x0y0.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.out.println("start");
                //method to launch game
            }});
        this.add(this.x0y0, gbc);

        this.x1y0=new JButton("button 2");
        this.x1y0.setSize(260,228);
        this.x1y0.setLocation(264,0);
        this.x1y0.setBorder(null);
        this.x1y0.setOpaque(false);
        this.add(this.x1y0, gbc);

        this.x2y0=new JButton("but3");
        this.x2y0.setSize(260,228);
        this.x2y0.setLocation(524,0);
        this.x2y0.setBorder(null);
        this.x2y0.setOpaque(false);
        this.add(this.x2y0, gbc);

        this.x3y0=new JButton("but4");
        this.x3y0.setSize(243,228);
        this.x3y0.setLocation(784,0);
        this.x3y0.setBorder(null);
        this.x3y0.setOpaque(false);
        this.add(this.x3y0, gbc);

        this.x0y1=new JButton("but5");
        this.x0y1.setSize(260,228);
        this.x0y1.setLocation(2,238);
        this.x0y1.setBorder(null);
        this.x0y1.setOpaque(false);
        this.add(this.x0y1, gbc);

        this.x1y1=new JButton("but6");
        this.x1y1.setSize(260,228);
        this.x1y1.setLocation(264,238);
        this.x1y1.setBorder(null);
        this.x1y1.setOpaque(false);
        this.add(this.x1y1, gbc);

        this.x2y1=new JButton("but7");
        this.x2y1.setSize(260,228);
        this.x2y1.setLocation(524,238);
        this.x2y1.setBorder(null);
        this.x2y1.setOpaque(false);
        this.add(this.x2y1, gbc);

        this.x3y1=new JButton("but8");
        this.x3y1.setSize(243,228);
        this.x3y1.setLocation(784,238);
        this.x3y1.setBorder(null);
        this.x3y1.setOpaque(false);
        this.add(this.x3y1, gbc);

        this.x0y2=new JButton("but9");
        this.x0y2.setSize(260,228);
        this.x0y2.setLocation(2,476);
        this.x0y2.setBorder(null);
        this.x0y2.setOpaque(false);
        this.add(this.x0y2, gbc);

        this.x1y2=new JButton("but10");
        this.x1y2.setSize(260,228);
        this.x1y2.setLocation(264,476);
        this.x1y2.setBorder(null);
        this.x1y2.setOpaque(false);
        this.add(this.x1y2, gbc);

        this.x2y2=new JButton("but11");
        this.x2y2.setSize(260,228);
        this.x2y2.setLocation(524,476);
        this.x2y2.setBorder(null);
        this.x2y2.setOpaque(false);
        this.add(this.x2y2, gbc);

        this.x3y2=new JButton("but12");
        this.x3y2.setSize(243,228);
        this.x3y2.setLocation(784,476);
        this.x3y2.setBorder(null);
        this.x3y2.setOpaque(false);
        this.add(this.x3y2, gbc);

        this.run =new JButton("RUN");
        this.run.setSize(250,70);
        this.run.setLocation(1030,0);
        this.run.setOpaque(false);
        this.add(this.run, gbc);

        this.grab =new JButton("GRAB");
        this.grab.setSize(250,70);
        this.grab.setLocation(1030,72);
        this.grab.setOpaque(false);
        this.add(this.grab, gbc);

        this.playerboard =new JButton("SHOW MY PLAYERBOARD");
        this.playerboard.setSize(250,70);
        this.playerboard.setLocation(1030,144);
        this.playerboard.setOpaque(false);
        this.add(this.playerboard, gbc);

        this.comboBox=new JComboBox();
        comboBox.addItem("Player1");
        comboBox.addItem("Player2");
        comboBox.addItem("Player3");
        comboBox.addItem("Player4");
        comboBox.setSize(250,70);
        this.comboBox.setLocation(1030,196);
        this.comboBox.setOpaque(false);
        this.add(this.comboBox, gbc);


    }

    private void loadImage(Image img) {
        try {
            MediaTracker track = new MediaTracker(this);
            track.addImage(img, 0);
            track.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(image, 0, 0, null);
        super.paintComponent(g);
    }
    public MapPanel()  {
        this.build();
        image = Toolkit.getDefaultToolkit().createImage("./src/main/resources/map1example.png");
        loadImage(image);
    }


}
