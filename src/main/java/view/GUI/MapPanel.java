package view.GUI;

import model.player.Player;
import model.player.Hero;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapPanel extends JLayeredPane{
    private  Image image;
    private JMapButton [] room = new JMapButton[12];
    private List<JPlayerButton> playerIcon = new ArrayList<>() ;

    public MapPanel()  {
        JButton run;
        JButton grab;
        JButton playerboard;
        this.setLayout(null);
        image = Toolkit.getDefaultToolkit().createImage(getUrlMap(3)); //aggiungere numero di mappa ricevuto
        loadImages(image);
        /* colorare bordi bottone
        Border border= BorderFactory.createLineBorder(Color.red);
        mapButtons[0].setBorder (border);
        mapButtons[0].setBorderPainted (true);*/
        int c=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<4;j++) {
                room[c] = new JMapButton(j, i);
                room[c].setOpaque(false);
                room[c].setSize(149,116);
                Border border= BorderFactory.createLineBorder(Color.red);
                room[c].setBorder (border);
                room[c].setBorderPainted (true);
                room[c].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("bottone room premuto");
                    }
                });
                c++;
            }
        }

        room[0].setLocation(166,175);
        room[1].setLocation(352,175);
        room[2].setLocation(532,175);
        room[3].setLocation(700,175);
        room[4].setLocation(166,335);
        room[5].setLocation(352,335);
        room[6].setLocation(532,335);
        room[7].setLocation(700,335);
        room[8].setLocation(166,515);
        room[9].setLocation(352,515);
        room[10].setLocation(532,515);
        room[11].setLocation(700,515);

        for(int i=0;i<12;i++){
            this.add(this.room[i]);
        }
/*
        //test
        ArrayList<Player> players=new ArrayList<>();

        Player p = new Player("antonio");
        p.setHero(new Hero("Violet",""));
        players.add(p);
        Player p2 = new Player("luigi");
        p2.setHero(new Hero("Banshee",""));
        players.add(p2);

        Player p3 = new Player("antonello");
        p3.setHero(new Hero("Di-Struct-Or",""));
        players.add(p3);

        Player p4 = new Player("giovanni");
        p4.setHero(new Hero("Sprog",""));
        players.add(p4);

        Player p5 = new Player("michele");
        p5.setHero(new Hero("Dozer",""));
        players.add(p5);

        createIconPlayers(players);
        //setPlayerIcon();
        //resetRooms();
        movePlayer(3,p);
        movePlayer(3,p2);
        movePlayer(3,p3);
        movePlayer(3,p4);
        movePlayer(3,p5);
      //  playerIcon.get(0).setEnabled(false);

*/

        run =new JButton("RUN");
        run.setSize(250,70);
        run.setLocation(1030,0);
        run.setOpaque(false);
        add(run);

        grab =new JButton("GRAB");
        grab.setSize(250,70);
        grab.setLocation(1030,72);
        grab.setOpaque(false);
        add(grab);

        playerboard =new JButton("SHOW MY PLAYERBOARD");
        playerboard.setSize(250,70);
        playerboard.setLocation(1030,144);
        playerboard.setOpaque(false);
        add(playerboard);

    }
    
    private void createIconPlayers(ArrayList <Player> players){
        for(int i=0;i<players.size();i++){
            Player p=players.get(i);
            JPlayerButton playerButton=new JPlayerButton(p);
            playerButton.setSize(46,68);
            playerButton.setContentAreaFilled(false);
            playerButton.setBorder(null);
            playerButton.setFocusPainted(false);
            playerButton.setIcon(new ImageIcon(getHeroIcon(p.getHero().getName())));
            //playerButton.setPressedIcon(new ImageIcon(p.getHero().getName())));
            playerButton.setOpaque(false);
            playerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("bottone premuto");
                }
            });
            playerButton.setVisible(true);
            playerIcon.add(playerButton);
            this.add(playerIcon.get(i));
        }
    }

    private String getHeroIcon (String name){

        switch (name){
            case "Violet": return "./src/main/resources/violeticon.png";
            case "Banshee" : return "./src/main/resources/bansheeicon.png";
            case "Di-Struct-Or" : return "./src/main/resources/distructoricon.png";
            case "Dozer" : return "./src/main/resources/dozericon.png";
            case "Sprog" : return "./src/main/resources/sprogicon.png";
            default: return null;
        }
    }

    private String getUrlMap(int code){
        /*
        switch (code){
            case 0: return "./src/main/resources/map0.png";
            case 1: return "./src/main/resources/map1.png";
            case 2: return "./src/main/resources/map2.png";
            case 3: return "./src/main/resources/map3.png";
            default: return null;
        }

         */
        return "./src/main/resources/map" + code + ".png";

    }

    public void setEnabledPlayer(ArrayList<Player> players){
                resetPlayersEnabled();
                for (int i=0;i<players.size();i++){
                    for(int j = 0; j< playerIcon.size(); j++){
                        if(players.get(i).getNickname().equals(playerIcon.get(j).getPlayer().getNickname()))
                            playerIcon.get(j).setEnabled(true);
                    }
                }
    }

    private void resetPlayersEnabled(){
        for (int i = 0; i<playerIcon.size(); i++){
            playerIcon.get(i).setEnabled(false);
        }
    }

    public void resetRooms(){
        for(int i=0;i<12;i++) {
            room[i].setEnabled(false);
            room[i].setVisible(false);
        }
    }


    public void movePlayer(int cell, Player player){
        //aggiungere controllo se si muove nella stessa cella
        boolean occupato=false;
        int c=0;
        int k=0;
        int j=0;
        int x=0;
        Point [] positions = new Point[5];
        positions[0]=new Point(room[cell].getX(),room[cell].getY()-14);
        positions[1]=new Point(room[cell].getX()+51,room[cell].getY()-14);
        positions[2]=new Point(room[cell].getX()+101,room[cell].getY()-14);
        positions[3]=new Point(room[cell].getX(),room[cell].getY()+60);
        positions[4]=new Point(room[cell].getX()+51,room[cell].getY()+60);

        for(int i=0;i<playerIcon.size();i++){
          if(player.getNickname().equals(playerIcon.get(i).getPlayer().getNickname())){
              c=i;

              while (x<5 && !occupato) {
                  for (j = 0; j < playerIcon.size(); j++) {
                      if (playerIcon.get(j).getLocation().equals(positions[x]))
                          occupato = true;
                  }
                  if (!occupato){
                      k = x;
                  occupato = true;
              }
                  else occupato=false;
         x++;
          }

              playerIcon.get(c).setLocation(positions[k]);
          }
        }

        }

    private void loadImages(Image imga) {
        try {
            MediaTracker track = new MediaTracker(this);
            track.addImage(imga, 1);
            track.waitForID(1);
        } catch (InterruptedException err) {
            err.printStackTrace();
            //logger.log(Level.WARNING, "Unable to connect to server", e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(image, 0, 0, null);
        super.paintComponent(g);
    }




}
