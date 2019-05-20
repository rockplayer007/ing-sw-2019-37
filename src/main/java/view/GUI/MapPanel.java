package view.GUI;

import model.board.AmmoSquare;
import model.board.GameBoard;
import model.board.GenerationSquare;
import model.board.Square;
import model.card.Weapon;
import model.player.Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapPanel extends JLayeredPane{
    private  transient  Image image;
    private List<JMapButton> roomButton = new ArrayList<>();
    private List<JPlayerButton> playerIcon = new ArrayList<>() ;
    private List<WeaponButton> weaponIcon = new ArrayList<>();
    private List<JButton> infoWeapon = new ArrayList<>();
    private int [] positionX = new int[4];
    private int [] positionY = new int[3];
    private GameBoard board;
    public MapPanel(GameBoard board)  {
        JButton run;
        JButton grab;
        JButton pBoards;
        JButton shoot;
        JButton adrenaline1;
        JButton adrenaline2;
        this.setLayout(null);
        this.board=board;
        image = Toolkit.getDefaultToolkit().createImage(getUrlMap(board.getId())); //aggiungere numero di mappa ricevuto
        loadImages(image);
        setRoomCoordinate();

        JButton button = new JButton();
        button.setSize(200,100);


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

        shoot= new JButton("SHOOT");
        shoot.setSize(250,70);
        shoot.setLocation(1030,144);
        shoot.setOpaque(false);
        add(shoot);

        adrenaline1=new JButton("MOVE AND GRAB ");
        adrenaline1.setSize(250,70);
        adrenaline1.setLocation(1030,216);
        adrenaline1.setOpaque(false);
        add(adrenaline1);

        adrenaline2=new JButton("MOVE AND SHOOT ");
        adrenaline2.setSize(250,70);
        adrenaline2.setLocation(1030,288);
        adrenaline2.setOpaque(false);
        add(adrenaline2);

        pBoards =new JButton("SHOW PLAYERS BOARDS");
        pBoards.setSize(250,70);
        pBoards.setLocation(1030,360);
        pBoards.setOpaque(false);
        add(pBoards);

    }

    private void setRoomCoordinate(){
        positionX [0]=166;
        positionX [1]=352;
        positionX [2]=532;
        positionX [3]=700;
        positionY [0]=175;
        positionY [1]=335;
        positionY [2]=515;
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

        return "."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"maps"+File.separatorChar+ "map" + code + ".png";

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
        for(int i=0;i<roomButton.size();i++) {
            roomButton.get(i).setEnabled(false);
            roomButton.get(i).setVisible(false);
        }
    }

    public void addWeapon(Weapon weapon){
        WeaponButton weaponButton= new WeaponButton(weapon);
        weaponButton.setSize(80,120);
        int i=weaponIcon.size();
        if(i>0){
            weaponButton.setLocation(weaponIcon.get(i-1).getX()+82,432);}
        else
            weaponButton.setLocation(1030,432);
        weaponButton.setContentAreaFilled(false);
        weaponButton.setBorder(null);
        weaponButton.setFocusPainted(false);
        weaponButton.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar + weapon.getName() + ".png"));
        weaponButton.setOpaque(false);
        weaponIcon.add(weaponButton);
        this.add(weaponButton);
        JButton info = new JButton("info");
        info.setSize(80,15);
        info.setLocation(weaponButton.getX(),weaponButton.getY()+122);
        info.setOpaque(false);
        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame dialog = new JFrame();
                dialog.setSize(300,300);
                JDialog jDialog= new JDialog(dialog,weapon.getName());
                JLabel l = new JLabel(weapon.getDescription());
                jDialog.add(l);
                jDialog.setSize(300, 300);
                jDialog.setVisible(true);
            }
        });
        infoWeapon.add(info);
        this.add(info);
    }

    public void removeWeapon(Weapon weapon){
        for(int i=0;i<weaponIcon.size();i++){
            if(weaponIcon.get(i).getWeapon().getName().equals(weapon.getName())){
                weaponIcon.get(i).setVisible(false);
                infoWeapon.get(i).setVisible(false);
            }

        }
    }

    public void createPlayerIcon(Player player){
        JPlayerButton playerButton=new JPlayerButton(player);
        playerButton.setSize(46,68);
        playerButton.setContentAreaFilled(false);
        playerButton.setBorder(null);
        playerButton.setFocusPainted(false);
        playerButton.setIcon(new ImageIcon(getHeroIcon(player.getHero().getName())));
        //playerButton.setPressedIcon(new ImageIcon(p.getHero().getName())));
        playerButton.setOpaque(false);
        playerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("bottone premuto");
            }
        });
        playerButton.setVisible(true);
        this.add(playerButton);
        playerIcon.add(playerButton);
    }


    public void movePlayer(int cell, Player player){
        //aggiungere controllo se si muove nella stessa cella
        boolean created=false;
        for (int i=0;i<playerIcon.size();i++){
            if(player.getHero().getName().equals(playerIcon.get(i).getPlayer().getHero().getName()))
                created=true;
        }
        if (!created)
            createPlayerIcon(player);

        boolean occupied=false;
        int c;
        int k=0;
        int j;
        int x=0;
        Point [] positions = new Point[5];
        positions[0]=new Point(roomButton.get(cell).getX(),roomButton.get(cell).getY()-14);
        positions[1]=new Point(roomButton.get(cell).getX()+51,roomButton.get(cell).getY()-14);
        positions[2]=new Point(roomButton.get(cell).getX()+101,roomButton.get(cell).getY()-14);
        positions[3]=new Point(roomButton.get(cell).getX(),roomButton.get(cell).getY()+60);
        positions[4]=new Point(roomButton.get(cell).getX()+51,roomButton.get(cell).getY()+60);

        for(int i=0;i<playerIcon.size();i++){
          if(player.getNickname().equals(playerIcon.get(i).getPlayer().getNickname())){
              c=i;

              while (x<5 && !occupied) {
                  for (j = 0; j < playerIcon.size(); j++) {
                      if (playerIcon.get(j).getLocation().equals(positions[x]))
                          occupied = true;
                  }
                  if (!occupied){
                      k = x;
                  occupied = true;
              }
                  else occupied=false;
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

    public void createMapButton(){
    for(int i=0;board.getSquare(i)!=null;i++){
        JMapButton room = new JMapButton(board.getSquare(i).getX(),board.getSquare(i).getY());
        room.setOpaque(false);
        room.setSize(149,116);
        Border border= BorderFactory.createLineBorder(Color.red);
        room.setBorder (border);
        room.setBorderPainted (true);
        room.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("bottone room premuto "+room.getPosx()+ " "+room.getPosy());
            }
        });
        room.setLocation(positionX[board.getSquare(i).getX()],positionY[board.getSquare(i).getY()]);
        roomButton.add(room);
        this.add(roomButton.get(i));
    }
    }

    public void updBoardGui(GameBoard b){
        this.board=b;
        for(int i=0;board.getSquare(i)!=null;i++){
            Square square = board.getSquare(i);
            if(board.getSquare(i).isGenerationPoint()){
                GenerationSquare generationSquare = (GenerationSquare) square;
                if(generationSquare.getWeaponDeck().isEmpty()){
                    for(int j=0;i<generationSquare.getWeaponDeck().size();j++){
                    Weapon weapon=generationSquare.getWeaponDeck().get(j);
                    // jbutton per ogni arma
                }
                }
            }
            else{
                AmmoSquare ammoSquare= (AmmoSquare) square;
                JButton ammo= new JButton();
                ammo.setSize(46,68);
                ammo.setContentAreaFilled(false);
                ammo.setBorder(null);
                ammo.setFocusPainted(false);
                if(ammoSquare.getAmmoCard().getAmmoList().isEmpty())
                    ammo.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"ammocard"+File.separatorChar+ ammoSquare.getAmmoCard().getName() + ".png"));

                else
                    ammo.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"ammocard"+File.separatorChar+"ammo.png"));
                ammo.setOpaque(false);
                ammo.setLocation(roomButton.get(i).getX()+101,roomButton.get(i).getY()+60);
                this.add(ammo);
            }
            int c=i;
            square.getPlayersOnSquare().forEach(player -> {
                movePlayer(c,player);
            });

        }
    }


}
