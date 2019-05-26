package view.GUI;

import model.board.*;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;
import model.player.Player;
import network.client.MainClient;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.*;
import javax.swing.text.html.StyleSheet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapPanel extends JLayeredPane{
    private  transient  Image image;
    private List<JMapButton> roomButton = new ArrayList<>();
    private List<JPlayerButton> playerIcon = new ArrayList<>() ;
    private List<WeaponButton> myWeapon = new ArrayList<>();
    private List<WeaponButton> weaponIcon = new ArrayList<>();
    private List<JButton> infoWeapon = new ArrayList<>();
    private List<JButton> powerupButton ;
    private List<JButton> weaponButton ;
    private int [] positionX = new int[4];
    private int [] positionY = new int[3];
    private transient GameBoard board;
    private JFrame chooseCard;
    private JFrame playerboards=new JFrame("BOARDS");
    private List<JLabel> ammocards=new ArrayList<>();
    private WeaponButton [] weaponButtons=new WeaponButton[3];
    //private WeaponButton [] powerupButtons=new WeaponButton[3];
    private List<JButton> actions = new ArrayList<>();
    private Player myplayer=null;
    public MapPanel(GameBoard board)  {
        JButton pBoards;
        this.setLayout(null);
        this.board=board;
        image = Toolkit.getDefaultToolkit().createImage(getUrlMap(board.getId()));
        loadImages(image);
        setRoomCoordinate();
        pBoards =new JButton("SHOW PLAYERS BOARDS");
        pBoards.setSize(250,50);
        pBoards.setLocation(1030,650);
        pBoards.setOpaque(false);
        pBoards.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerboards.setVisible(true);
            }
        });
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

    private String getUrlMap(int code){

        return "."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+
                File.separatorChar +"maps"+File.separatorChar+ "map" + code + ".png";

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
        int i= myWeapon.size();
        if(i>0){
            weaponButton.setLocation(myWeapon.get(i-1).getX()+82,432);}
        else
            weaponButton.setLocation(1030,432);
        weaponButton.setContentAreaFilled(false);
        weaponButton.setBorder(null);
        weaponButton.setFocusPainted(false);
        weaponButton.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                +File.separatorChar+"resources"+File.separatorChar+"weapon"+File.separatorChar + weapon.getName() + ".png"));
        weaponButton.setOpaque(false);
        myWeapon.add(weaponButton);
        this.add(weaponButton);
    }

    public void removeWeapon(){
        for(int i = 0; i< myWeapon.size(); i++){
                myWeapon.get(i).setVisible(false);
                //infoWeapon.get(i).setVisible(false);
            }
    }

    public void resetWeaponIcon(){
        for(int i = 0; i< weaponIcon.size(); i++){
            weaponIcon.get(i).setVisible(false);
        }
    }

    public void updateWeapon(GameBoard board,MainClient mainClient){
        removeWeapon();
        myWeapon =new ArrayList<>();
        for (int i=0;i<board.getPlayersOnMap().size();i++){
            if (board.getPlayersOnMap().get(i).getNickname().equals(mainClient.getUsername())){
                for(int j=0;j<board.getPlayersOnMap().get(i).getWeapons().size();j++) {
                   addWeapon(board.getPlayersOnMap().get(i).getWeapons().get(j));
                }
            }
        }
    }

    public void createPlayerIcon(Player player){
        JPlayerButton playerButton=new JPlayerButton(player);
        playerButton.setSize(46,68);
        playerButton.setContentAreaFilled(false);
        playerButton.setBorder(null);
        playerButton.setFocusPainted(false);
        playerButton.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+
                File.separatorChar +"heroes"+File.separatorChar+ player.getHero().getName()+".png"));
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
            room.setLocation(positionX[board.getSquare(i).getX()],positionY[board.getSquare(i).getY()]);
            roomButton.add(room);
            this.add(room);
        }
    }

    public void resetAmmo(){
        for(int i=0;i<ammocards.size();i++){
            ammocards.get(i).setVisible(false);
            this.remove(ammocards.get(i));
        }
    }

    public void updBoardGui(GameBoard board){
        //this.board=b;
        resetWeaponIcon();
        weaponIcon=new ArrayList<>();
        resetAmmo();
        ammocards=new ArrayList<>();
        for(int i=0;board.getSquare(i)!=null;i++){
            Square square = board.getSquare(i);
            if(board.getSquare(i).isGenerationPoint()){
                GenerationSquare generationSquare = (GenerationSquare) square;
                if(!generationSquare.getWeaponDeck().isEmpty()){
                    for(int j=0;j<generationSquare.getWeaponDeck().size();j++){
                        WeaponButton weaponButton=new WeaponButton(generationSquare.getWeaponDeck().get(j));
                        if(board.getSquare(i).getX()==0){
                            weaponButton.setSize(150,90);
                            weaponButton.setLocation(2,253+110*j);
                            weaponButton.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+
                                    File.separatorChar+"resources"+File.separatorChar +"powerup"+File.separatorChar+"weaponmap"+File.separatorChar
                                    + weaponButton.getWeapon().getName()+" left.png"));

                        }
                        else if (board.getSquare(i).getY()==0){
                            weaponButton.setSize(90,150);
                            weaponButton.setLocation(536+110*j,2);
                            weaponButton.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+
                                    File.separatorChar+"resources"+File.separatorChar +"powerup"+File.separatorChar+"weaponmap"+File.separatorChar
                                    + weaponButton.getWeapon().getName()+".png"));
                        }
                        else if(board.getSquare(i).getY()==2){
                            weaponButton.setSize(150,90);
                            weaponButton.setLocation(884,405+110*j);
                            weaponButton.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+
                                    File.separatorChar+"resources"+File.separatorChar +"powerup"+File.separatorChar+"weaponmap"+File.separatorChar
                                    + weaponButton.getWeapon().getName()+"right.png"));

                        }
                        weaponIcon.add(weaponButton);
                        this.add(weaponButton);
                    }
                }
            }
            else{
                AmmoSquare ammoSquare= (AmmoSquare) square;
                ImageIcon imageIcon=new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"ammocard"+File.separatorChar+ ammoSquare.getAmmoCard().getName() + ".png");
                JLabel ammo= new JLabel(imageIcon);
                ammo.setSize(46,68);
                //   ammo.setContentAreaFilled(false);
                ammo.setBorder(null);
                //  ammo.setFocusPainted(false);
                if(!ammoSquare.getAmmoCard().getAmmoList().isEmpty())
                    ammo.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+
                            File.separatorChar+"resources"+File.separatorChar +"ammocard"+File.separatorChar
                            + ammoSquare.getAmmoCard().getName() + ".png"));

                else
                    ammo.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"
                            +File.separatorChar+"resources"+File.separatorChar +"ammocard"+File.separatorChar+"ammo.png"));
                ammo.setOpaque(false);
                StyleSheet s = new StyleSheet();
                Border border= BorderFactory.createLineBorder(s.stringToColor(square.getColor().toString()));
                ammo.setBorder (border);
                ammo.setLocation(roomButton.get(i).getX()+101,roomButton.get(i).getY()+60);
                ammocards.add(ammo);
                this.add(ammo);
            }

            int c=i;
            square.getPlayersOnSquare().forEach(player -> {
                movePlayer(c,player);
            });

        }
    }

    public void updatePlayerBoard(GameBoard board){
        playerboards.getContentPane().removeAll();
        playerboards.setSize(830,1024);
        PlayerBoardPanel pbl=new PlayerBoardPanel(board.getPlayersOnMap());
        playerboards.getContentPane().add(pbl);
    }

    public void getSquareSelected(List<Square> squares, MainClient mainClient){
        resetRooms();
        for (int i=0;i<squares.size();i++){
            Square square=squares.get(i);
            roomButton.get(square.getId()).setEnabled(true);
            roomButton.get(square.getId()).setVisible(true);
            final int x=i;
            roomButton.get(square.getId()).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedBoard(x);
                    resetRooms();
                }
            });
        }
    }

    private void resetPowerups(){

        for(int i=0;i<powerupButton.size();i++){
            powerupButton.get(i).setVisible(false);
        }
    }

    public void getCardSelected(List<Powerup> powerups, boolean optional, MainClient mainClient) {
        resetRooms();
        powerupButton = new ArrayList<>();
        chooseCard = new JFrame("SELECT POWERUP");
        chooseCard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        chooseCard.setLayout(new GridLayout(1,4));
        for (int j = 0; j < powerups.size(); j++) {
            JButton powerup = new JButton();
            powerup.setSize(196, 264);
            //int i = powerupButton.size();
                /*
                if (i > 0) {
                    powerup.setLocation(powerupButton.get(i - 1).getX() + 200, 10);
                } else
                    powerup.setLocation(600, 10);
                */
            powerup.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                    File.separatorChar + powerups.get(j).getName() + powerups.get(j).getAmmo().toString()+".png"));
            powerup.setOpaque(false);
            powerup.setContentAreaFilled(false);
            powerup.setBorder(null);
            powerup.setFocusPainted(false);
            final int c = j;
            powerup.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chooseCard.setVisible(false);
                    mainClient.sendSelectedCard(c);
                    chooseCard.getContentPane().removeAll();
                    //resetPowerups();
                    //non dare possibilità di selezionare powerup

                }
            });
            powerupButton.add(powerup);
            chooseCard.add(powerup);
        }


        if (optional) {
            JButton opt = new JButton("Dont use any powerup");
            opt.setSize(196, 264);
            opt.setBackground(Color.darkGray);
            final int s = powerups.size();
            opt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedCard(s);
                    chooseCard.setVisible(false);
                    chooseCard.getContentPane().removeAll();
                }
            });
            powerupButton.add(opt);
            chooseCard.add(opt);
            chooseCard.setTitle("Do you want to use a powerup?");
        }
        else
            chooseCard.setTitle("Select powerup to discard");
        chooseCard.pack();
        chooseCard.setLocation(300,50);
        chooseCard.setMinimumSize(new Dimension(600,280));
        chooseCard.setMaximumSize(new Dimension(800,285));
        chooseCard.setVisible(true);
    }


    public void getWeaponSelected(List<Weapon> weapons, boolean optional, MainClient mainClient) {
        resetRooms();
        weaponButton = new ArrayList<>();
        chooseCard = new JFrame("");
        chooseCard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        chooseCard.setLayout(new GridLayout(1,4));
        for (int j = 0; j < weapons.size(); j++) {
            JButton weapon = new JButton();
            weapon.setSize(196, 264);
            //int i = powerupButton.size();
                /*
                if (i > 0) {
                    powerup.setLocation(powerupButton.get(i - 1).getX() + 200, 10);
                } else
                    powerup.setLocation(600, 10);
                */
            weapon.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar + "powerup" +
                    File.separatorChar + weapons.get(j).getName()+".png"));
            weapon.setOpaque(false);
            weapon.setContentAreaFilled(false);
            weapon.setBorder(null);
            weapon.setFocusPainted(false);
            final int c = j;
            weapon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chooseCard.setVisible(false);
                    mainClient.sendSelectedCard(c);
                    chooseCard.getContentPane().removeAll();
                    //resetPowerups();
                    //non dare possibilità di selezionare powerup

                }
            });
            powerupButton.add(weapon);
            chooseCard.add(weapon);
        }


        if (optional) {
            JButton opt = new JButton("Dont use any powerup");
            opt.setSize(196, 264);
            opt.setBackground(Color.darkGray);
            final int s = weapons.size();
            opt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedCard(s);
                    chooseCard.setVisible(false);
                    chooseCard.getContentPane().removeAll();
                }
            });
            weaponButton.add(opt);
            chooseCard.add(opt);
            chooseCard.setTitle("Do you want to use a powerup?");
        }
        else
            chooseCard.setTitle("Select powerup to discard");
        chooseCard.pack();
        chooseCard.setLocation(300,50);
        chooseCard.setMinimumSize(new Dimension(600,280));
        chooseCard.setMaximumSize(new Dimension(800,285));
        chooseCard.setVisible(true);
    }

    public void getAction(List<ActionOption> actionOptions, MainClient mainClient){
        //bloccare altre azioni possibili
        actions=new ArrayList<>();
        JLabel text = new JLabel("Select action:"); //centrare il testo
        text.setSize(250,20);
        text.setLocation(1030,0);
        this.add(text);
        for (int i=0;i<actionOptions.size();i++){
            JButton action = new JButton();
            action.setSize(124, 64);
            action.setOpaque(false);
            action.setContentAreaFilled(false);
            Border border= BorderFactory.createLineBorder(Color.black,2);
            action.setBorder(border);
            action.setFocusPainted(false);
            action.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar
                    + "action" + File.separatorChar + actionOptions.get(i).toString() + ".png"));

            if (actions.isEmpty())
                action.setLocation(1030,30);
            else {
                if (actions.get(i - 1).getX() == 1155)
                    action.setLocation(1030, actions.get(i - 1).getY() + 64);
                else
                    action.setLocation(actions.get(i - 1).getX() + 125, actions.get(i - 1).getY());
            }
            final int x=i;
            action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedBoard(x);
                    resetActions();
                    text.setVisible(false);
                }});
            this.add(action);
            actions.add(action);

        }
    }

    public void resetActions(){
        for (int i=0;i<actions.size();i++){
            actions.get(i).setEnabled(false);
            actions.get(i).setVisible(false);
        }
    }
}