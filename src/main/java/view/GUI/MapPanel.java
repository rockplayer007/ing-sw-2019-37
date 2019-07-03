package view.GUI;

import model.board.*;
import model.card.*;
import model.player.ActionOption;
import model.player.Player;
import network.client.MainClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.*;
import javax.swing.text.html.StyleSheet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MapPanel extends JLayeredPane {
    private transient Image image;
    private List<JMapButton> roomButton = new ArrayList<>();
    private List<JPlayerButton> playerIcon = new ArrayList<>();
    private List<WeaponButton> myWeapon = new ArrayList<>();
    private List<PowerupButton> myPowerup = new ArrayList<>();
    private List<WeaponButton> weaponIcon = new ArrayList<>();
    private List<JLabel> ammoColors = new ArrayList<>();
    private int[] positionX = new int[4];
    private int[] positionY = new int[3];
    private transient GameBoard board;
    private JFrame chooseCard;
    private JFrame choose;
    private JFrame selectEffect = new JFrame("Choose Effect");
    private JFrame playerboards = new JFrame("BOARDS");
    private JFrame chooseRoom= new JFrame();
    private List<JLabel> ammocards = new ArrayList<>();
    private List<JButton> actions = new ArrayList<>();
    private List<Weapon> weaponList = new ArrayList<>();
    private List<JLabel> infoWeapon=new ArrayList<>();
    private Weapon weaponSelected;
    private List<JLabel> skullboard=new ArrayList<>();
    private JScrollPane messageScrollPane;
    private JPanel messagePanel;
    private int rows =8;
    private JLabel help;
    private JLabel mypoint= new JLabel("");
    private boolean first=true;
    private static final Logger logger = Logger.getLogger(MapPanel.class.getName());

    public MapPanel(GameBoard board) {
        JButton pBoards;
        this.setLayout(null);
        this.board = board;
       /* image = Toolkit.getDefaultToolkit().createImage("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" +
                File.separatorChar + "maps" + File.separatorChar + "map" + board.getId() + ".png");
       */
       image=getImage("/maps/map"+board.getId()+".png");
        try {
            loadImages(image);
        } catch (Exception ignored) {
            System.out.println("errrrr");
        }
        setRoomCoordinate();
        pBoards = new JButton("SHOW PLAYER BOARDS");
        pBoards.setSize(250, 30);
        pBoards.setLocation(1030, 675);
        pBoards.setOpaque(false);
        pBoards.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerboards.setVisible(true);
            }
        });
        add(pBoards);
        chooseCard = new JFrame();
        chooseCard.setLocation(300, 50);
        chooseCard.setMinimumSize(new Dimension(600, 280));
        chooseCard.setMaximumSize(new Dimension(800, 285));
        chooseCard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        chooseCard.setAlwaysOnTop(true);
        choose = new JFrame();
        choose.setLocation(300, 50);
        choose.setMinimumSize(new Dimension(300, 300));
        choose.setMaximumSize(new Dimension(450, 300));
        choose.setLayout(new GridLayout(2, 2));
        choose.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        messagePanel =new JPanel();
        messagePanel.setLayout(new GridLayout(rows,1));
        messageScrollPane=new JScrollPane(messagePanel);
        setMessageScrollPane();
        this.add(messageScrollPane);
        help=new JLabel();
        help.setLocation(168,107);
        help.setSize(340,34);
        help.setFont(new Font(null,Font.BOLD,12));
        help.setBackground(Color.WHITE);
        help.setOpaque(true);
        help.setBorder(BorderFactory.createLineBorder(Color.black,3));
        this.add(help);
        JLabel myPointText= new JLabel("My points : ");
        myPointText.setLocation(1030, 45);
        myPointText.setFont(new Font(null,Font.BOLD,12));
        myPointText.setSize(150,20);
        this.add(myPointText);
        mypoint.setSize(50,20);
        mypoint.setLocation(1180,45);
        this.add(mypoint);
        JButton rules = new JButton("Game Rules");
        rules.setLocation(200,680);
        rules.setSize(150,20);
        RulesPanel rulesPanel = new RulesPanel();
        JScrollPane scrollPane=new JScrollPane(rulesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JFrame rulesFrame = new JFrame("Game Rules");
        rulesFrame.add(scrollPane);
        rulesFrame.pack();
        rules.addActionListener(new RulesActionListener(rulesFrame));
        this.add(rules);
    }

    private void setRoomCoordinate() {
        positionX[0] = 166;
        positionX[1] = 352;
        positionX[2] = 532;
        positionX[3] = 700;
        positionY[0] = 175;
        positionY[1] = 335;
        positionY[2] = 515;
    }

    public void getPlayer(List<Player> players, MainClient mainClient,String info) {
        resetRooms();
        resetPlayersEnabled();
        StyleSheet styleSheet=new StyleSheet();
        for (int i = 0; i < players.size(); i++) {
            for (JPlayerButton playerButton : playerIcon) {
                if (players.get(i).getNickname().equals(playerButton.getPlayer().getNickname())) {
                    playerButton.setEnabled(true);

                    playerButton.setBorder(BorderFactory.createLineBorder(styleSheet.stringToColor(playerButton.getPlayer().getHero().getColor().name()), 3));
                    int s = i;
                    playerButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            removePlayerActions();
                            mainClient.sendSelectedItem(s);
                            enablePlayers();
                        }
                    });
                }
            }
        }
        addActionInfo(info);
    }


    private void removePlayerActions() {
        for (JPlayerButton playerButton : playerIcon) {
            ActionListener[] actionListener = playerButton.getActionListeners();
            for (ActionListener actionListener1 : actionListener) {
                playerButton.removeActionListener(actionListener1);
            }
        }
    }

    private void resetPlayersEnabled() {
        for (JPlayerButton playerButton : playerIcon) {
            playerButton.setEnabled(false);
        }
    }

    private void enablePlayers() {
        for (JPlayerButton playerButton : playerIcon) {
            playerButton.setEnabled(true);
            playerButton.setBorder(null);
        }
    }

    public void resetRooms() {
        for (JMapButton jMapButton : roomButton) {
            jMapButton.setVisible(false);
        }
    }


    public void addWeapon(Weapon weapon) {
        WeaponButton weaponButton = new WeaponButton(weapon);
        weaponButton.setSize(80, 120);
        int i = myWeapon.size();
        if (i > 0) {
            weaponButton.setLocation(myWeapon.get(i - 1).getX() + 82, 432);
        } else
            weaponButton.setLocation(1030, 432);
        weaponButton.setContentAreaFilled(false);
        weaponButton.setBorder(null);
        weaponButton.setFocusPainted(false);
      /*  weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                + File.separatorChar + "resources" + File.separatorChar + "weapon" + File.separatorChar + weapon.getName() + ".png"));
       */
      weaponButton.setIcon(new ImageIcon(getImage("/weapon/"+weapon.getName()+".png")));
        weaponButton.setOpaque(false);
        myWeapon.add(weaponButton);
        this.add(weaponButton);
        JLabel label=new JLabel();
        label.setSize(80,20);
        if (weapon.getCharged()) {
            label.setText("Charge");
            label.setForeground(Color.green);
        }
        else{
            label.setText("Discharge");
            label.setForeground(Color.red);
        }
        label.setLocation(weaponButton.getX(),weaponButton.getY()-22);
        label.setFont(new Font(null ,Font.BOLD,12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        infoWeapon.add(label);
        this.add(label);

    }

    public void removeMyWeapon() {
        for (int i = 0; i < myWeapon.size(); i++) {
            myWeapon.get(i).setVisible(false);
            this.remove(myWeapon.get(i));
            infoWeapon.get(i).setVisible(false);
            this.remove(infoWeapon.get(i));
        }
    }

    public void resetWeaponIcon() {
        for (WeaponButton weaponButton : weaponIcon) {
            weaponButton.setVisible(false);
            this.remove(weaponButton);
        }
    }

    private void updateAmmo(Map<AmmoColor, Integer> ammos) {
        for (JLabel ammoColor1 : ammoColors) {
            ammoColor1.setVisible(false);
            this.remove(ammoColor1);
        }
        ammoColors = new ArrayList<>();
        for (Map.Entry<AmmoColor, Integer> entry : ammos.entrySet()) {
            AmmoColor ammoColor = entry.getKey();
            Integer n = entry.getValue();
            for (int i = 0; i < n; i++) {
                JLabel ammo = new JLabel();
                ammo.setSize(20, 20);
                StyleSheet s = new StyleSheet();
                ammo.setBackground(s.stringToColor(ammoColor.toString()));
                ammo.setOpaque(true);
                ammo.repaint();
                int size = ammoColors.size();
                if (size > 0)
                    ammo.setLocation(ammoColors.get(size - 1).getX() + 22, 2);
                else
                    ammo.setLocation(1030, 2);
                this.add(ammo);
                ammoColors.add(ammo);
            }
        }
    }


    public void updateWeapon(GameBoard board, MainClient mainClient) {
        removeMyWeapon();
        myWeapon = new ArrayList<>();
        infoWeapon=new ArrayList<>();
        for (int i = 0; i < board.getPlayersOnMap().size(); i++) {
            if (board.getPlayersOnMap().get(i).getNickname().equals(mainClient.getUsername())) {
                updateAmmo(board.getPlayersOnMap().get(i).getAmmo());
                for (int j = 0; j < board.getPlayersOnMap().get(i).getWeapons().size(); j++) {
                    addWeapon(board.getPlayersOnMap().get(i).getWeapons().get(j));
                }
                mypoint.setText(String.valueOf(board.getPlayersOnMap().get(i).getPlayerBoard().getPoints()));
                if(first){
                JLabel heroIcon = new JLabel();
                heroIcon.setSize(150,150);
                heroIcon.setLocation(2,600);
                /*heroIcon.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" +
                        File.separatorChar + "heroes" + File.separatorChar + board.getPlayersOnMap().get(i).getHero().getName() + "big.png"));
                */
                heroIcon.setIcon(new ImageIcon(getImage("/heroes/"+board.getPlayersOnMap().get(i).getHero().getName()+"big.png")));
                this.add(heroIcon);
                first=false;
                }}
        }
    }

    public void removePowerup() {
        for (PowerupButton powerupButton : myPowerup) {
            powerupButton.setVisible(false);
            this.remove(powerupButton);
        }

    }

    public void addPowerup(Powerup powerup) {
        PowerupButton pow = new PowerupButton(powerup);
        pow.setSize(80, 120);
        int i = myPowerup.size();
        if (i > 0)
            pow.setLocation(myPowerup.get(i - 1).getX() + 82, 554);
        else
            pow.setLocation(1030, 554);
        /*pow.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                + File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "pow" + File.separatorChar + powerup.getName() + powerup.getAmmo().toString() + ".png"));
        */pow.setOpaque(false);
        pow.setIcon(new ImageIcon(getImage("/powerup/pow/"+powerup.getName()+powerup.getAmmo().toString()+".png")));
        pow.setContentAreaFilled(false);
        pow.setBorder(null);
        pow.setFocusPainted(false);
        myPowerup.add(pow);
        this.add(pow);
    }

    public void updatePowerup(List<Powerup> mypow) {
        removePowerup();
        myPowerup = new ArrayList<>();
        for (Powerup powerup : mypow) {
            addPowerup(powerup);
        }
    }

    public void createPlayerIcon(Player player) {
        JPlayerButton playerButton = new JPlayerButton(player);
        playerButton.setSize(46, 68);
        playerButton.setContentAreaFilled(false);
        playerButton.setBorder(null);
        playerButton.setFocusPainted(false);
        /*playerButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" +
                File.separatorChar + "heroes" + File.separatorChar + player.getHero().getName() + ".png"));
       */
        playerButton.setIcon(new ImageIcon(getImage("/heroes/"+player.getHero().getName()+".png")));
        playerButton.setOpaque(false);
        playerButton.setVisible(true);
        this.add(playerButton);
        playerIcon.add(playerButton);
    }


    public void movePlayer(int cell, Player player) {
        boolean created = false;
        JButton icon = new JButton();
        for (JPlayerButton playerButton : playerIcon) {
            if (player.getHero().getName().equals(playerButton.getPlayer().getHero().getName())) {
                created = true;
                icon = playerButton;
            }
        }
        if (!created)
            createPlayerIcon(player);
        else
            icon.setVisible(true);
        if( !(icon.getX()>= roomButton.get(cell).getX() && icon.getY() >= roomButton.get(cell).getY()-14 &&
                icon.getX()<=roomButton.get(cell).getX()+roomButton.get(cell).getWidth() &&
                icon.getY() <= roomButton.get(cell).getY()-14+roomButton.get(cell).getHeight())) {
            boolean occupied = false;
            int c;
            int k = 0;
            int j;
            int x = 0;
            Point[] positions = new Point[5];
            positions[0] = new Point(roomButton.get(cell).getX(), roomButton.get(cell).getY() - 14);
            positions[1] = new Point(roomButton.get(cell).getX() + 51, roomButton.get(cell).getY() - 14);
            positions[2] = new Point(roomButton.get(cell).getX() + 101, roomButton.get(cell).getY() - 14);
            positions[3] = new Point(roomButton.get(cell).getX(), roomButton.get(cell).getY() + 60);
            positions[4] = new Point(roomButton.get(cell).getX() + 51, roomButton.get(cell).getY() + 60);

            for (int i = 0; i < playerIcon.size(); i++) {
                if (player.getNickname().equals(playerIcon.get(i).getPlayer().getNickname())) {
                    c = i;

                    while (x < 5 && !occupied) {
                        for (j = 0; j < playerIcon.size(); j++) {
                            if (playerIcon.get(j).getLocation().equals(positions[x]))
                                occupied = true;
                        }
                        if (!occupied) {
                            k = x;
                            occupied = true;
                        } else occupied = false;
                        x++;
                    }

                    playerIcon.get(c).setLocation(positions[k]);
                }
            }

        }
    }

    private void loadImages(Image imga) throws Exception {
        MediaTracker track = new MediaTracker(this);
        track.addImage(imga, 1);
        track.waitForID(1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(image, 0, 0, null);
        super.paintComponent(g);
    }

    public void createMapButton() {
        for (int i = 0; board.getSquare(i) != null; i++) {
            JMapButton room = new JMapButton(board.getSquare(i).getX(), board.getSquare(i).getY(), board.getSquare(i).getColor());
            room.setOpaque(false);
            room.setSize(149, 116);
            StyleSheet styleSheet=new StyleSheet();
            Border border = BorderFactory.createLineBorder(styleSheet.stringToColor(board.getSquare(i).getColor().toString()),3);
            room.setBorder(border);
            room.setBorderPainted(true);
            room.setContentAreaFilled(false);
            room.setLocation(positionX[board.getSquare(i).getX()], positionY[board.getSquare(i).getY()]);
            roomButton.add(room);
            this.add(room);
        }
        resetRooms();
    }

    public void resetAmmo() {
        for (JLabel ammocard : ammocards) {
            ammocard.setVisible(false);
            this.remove(ammocard);
        }
    }

    private void resetPlayersVisible(){
        for (JPlayerButton playerButton : playerIcon) {
            playerButton.setVisible(false);
        }
    }
    public void updBoardGui(GameBoard board) {
        resetWeaponIcon();
        weaponIcon = new ArrayList<>();
        resetAmmo();
        resetPlayersVisible();
        ammocards = new ArrayList<>();
        for (int i = 0; board.getSquare(i) != null; i++) {
            Square square = board.getSquare(i);
            if (board.getSquare(i).isGenerationPoint()) {
                GenerationSquare generationSquare = (GenerationSquare) square;
                if (!generationSquare.getWeaponDeck().isEmpty()) {
                    for (int j = 0; j < generationSquare.getWeaponDeck().size(); j++) {
                        WeaponButton weaponButton = new WeaponButton(generationSquare.getWeaponDeck().get(j));
                        if (board.getSquare(i).getX() == 0) {
                            weaponButton.setSize(148, 90);
                            weaponButton.setLocation(2, 253 + 110 * j);
                           /* weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                                    File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "weaponmap" + File.separatorChar +
                                    "left" + File.separatorChar + weaponButton.getWeapon().getName() + ".png"));
                            */
                           weaponButton.setIcon(new ImageIcon(getImage("/powerup/weaponmap/left/"+weaponButton.getWeapon().getName()+".png")));

                        } else if (board.getSquare(i).getY() == 0) {
                            weaponButton.setSize(90, 148);
                            weaponButton.setLocation(536 + 110 * j, 2);
                           /* weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                                    File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "weaponmap" + File.separatorChar
                                    + weaponButton.getWeapon().getName() + ".png"));*/
                            weaponButton.setIcon(new ImageIcon(getImage("/powerup/weaponmap/"+weaponButton.getWeapon().getName()+".png")));
                        } else if (board.getSquare(i).getY() == 2) {
                            weaponButton.setSize(148, 90);
                            weaponButton.setLocation(880, 405 + 110 * j);
                           /* weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                                    File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "weaponmap" + File.separatorChar +
                                    "right" + File.separatorChar + weaponButton.getWeapon().getName() + ".png"));*/
                            weaponButton.setIcon(new ImageIcon(getImage("/powerup/weaponmap/right/"+weaponButton.getWeapon().getName()+".png")));

                        }
                        weaponIcon.add(weaponButton);
                        this.add(weaponButton);
                    }
                }
            } else {
                AmmoSquare ammoSquare = (AmmoSquare) square;
                JLabel ammo = new JLabel();
                ammo.setSize(46, 68);
                ammo.setBorder(null);
                if (ammoSquare.getAmmoCard() != null)
                    ammo.setIcon(new ImageIcon(getImage("/ammocard/"+ammoSquare.getAmmoCard().getName()+".png")));
                   /*ammo.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                            File.separatorChar + "resources" + File.separatorChar + "ammocard" + File.separatorChar
                            + ammoSquare.getAmmoCard().getName() + ".png"));*/

                else
                   ammo.setIcon(new ImageIcon(getImage("/ammocard/ammo.png")));
                 /* ammo.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                            + File.separatorChar + "resources" + File.separatorChar + "ammocard" + File.separatorChar + "ammo.png"));*/


                ammo.setOpaque(false);
                StyleSheet s = new StyleSheet();
                Border border = BorderFactory.createLineBorder(s.stringToColor(square.getColor().toString()));
                ammo.setBorder(border);
                ammo.setLocation(roomButton.get(i).getX() + 101, roomButton.get(i).getY() + 60);
                ammocards.add(ammo);
                this.add(ammo);
            }

            int c = i;
            square.getPlayersOnSquare().forEach(player -> {
                movePlayer(c, player);
            });

        }
    }

    public void updatePlayerBoard(List<Player> players) {
        int height =players.size()*155 +20;
        playerboards.setSize(830, height);
        playerboards.getContentPane().removeAll();
        PlayerBoardPanel pbl = new PlayerBoardPanel(players);
        playerboards.getContentPane().add(pbl);
        playerboards.repaint();
        playerboards.revalidate();
    }

    public void getSquareSelected(List<Square> squares, MainClient mainClient,String info) {
        resetRooms();
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            roomButton.get(square.getId()).setEnabled(true);
            roomButton.get(square.getId()).setVisible(true);
            //int x = i;
            ActionListener [] actionListener= roomButton.get(square.getId()).getActionListeners();
            for (ActionListener actionListener1 : actionListener) {
                roomButton.get(square.getId()).removeActionListener(actionListener1);
            }
            roomButton.get(square.getId()).addActionListener(new SquareActionListener(this,mainClient,i));
           /* roomButton.get(square.getId()).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedItem(x);
                    resetRooms();
                    addActionInfo("");
                }
            });*/
        }
        addActionInfo(info);
    }


    public void getPowerupSelected(List<Powerup> powerups, boolean optional, MainClient mainClient,String info) {
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < powerups.size(); i++) {
            cards.add(powerups.get(i).getName() + powerups.get(i).getAmmo().toString());
        }
        SelectCardPanel selectCardPanel = new SelectCardPanel(cards, optional, mainClient, chooseCard, this);
        chooseCard.getContentPane().removeAll();
        chooseCard.getContentPane().add(selectCardPanel);
        chooseCard.setTitle(info);
        if(info==null)
            chooseCard.setTitle("Choose Powerup");
        addActionInfo(info);
        chooseCard.pack();
        chooseCard.setVisible(true);
    }

    public void getWeaponSelected(List<Weapon> weapons, boolean optional, MainClient mainClient,String info) {
        List<String> cards = new ArrayList<>();
        for (Weapon weapon : weapons) {
            cards.add(weapon.getName());
        }
        weaponList = weapons;
        chooseCard.getContentPane().removeAll();
        SelectCardPanel selectCardPanel = new SelectCardPanel(cards, optional, mainClient, chooseCard, this);
        chooseCard.getContentPane().add(selectCardPanel);
        chooseCard.setTitle(info);
        addActionInfo(info);
        chooseCard.pack();
        chooseCard.setVisible(true);
    }


    public void getAction(List<ActionOption> actionOptions, MainClient mainClient) {
        resetRooms();
        actions = new ArrayList<>();
        JLabel text = new JLabel("Select one action:");
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setFont(new Font(null ,Font.BOLD,12));
        text.setSize(250, 20);
        text.setLocation(1030, 68);
        this.add(text);
        JLabel borderLabel = new JLabel();
        borderLabel.setSize(250,152);
        borderLabel.setLocation(1030,66);
        borderLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
        this.add(borderLabel);
        for (int i = 0; i < actionOptions.size(); i++) {
            JButton action = new JButton();
            action.setSize(124, 64);
            action.setOpaque(false);
            action.setContentAreaFilled(false);
            Border border = BorderFactory.createLineBorder(Color.black, 2);
            action.setBorder(border);
            action.setFocusPainted(false);
            /*
            action.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar
                    + "action" + File.separatorChar + actionOptions.get(i).toString() + ".png"));*/
            action.setIcon(new ImageIcon(getImage("/action/"+actionOptions.get(i).toString()+".png")));;
            if (actions.isEmpty())
                action.setLocation(1030, 89);
            else {
                if (actions.get(i - 1).getX() == 1155)
                    action.setLocation(1030, actions.get(i - 1).getY() + 64);
                else
                    action.setLocation(actions.get(i - 1).getX() + 125, actions.get(i - 1).getY());
            }

            action.addActionListener(new ActionResponseListener(this,mainClient,i,text,borderLabel));
            /*action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedBoard(x);
                    resetActions();
                    text.setVisible(false);
                    borderLabel.setVisible(false);
                }
            });*/
            this.add(action);
            actions.add(action);


        }
    }

    public void resetActions() {
        for (JButton action : actions) {
            action.setEnabled(false);
            action.setVisible(false);
            this.remove(action);
        }
    }

    public void getDirection(List<Square.Direction> directions, MainClient mainClient, String info) {
        choose.getContentPane().removeAll();
        choose.setLayout(new GridLayout(2,2));
        choose.setLocation(300, 50);
        choose.setSize(300, 300);
        for (int i = 0; i < directions.size(); i++) {
            JButton dir = new JButton();
            dir.setSize(150, 150);
            /*
            dir.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar
                    + "directions" + File.separatorChar + directions.get(i).name() + ".jpg"));*/
            dir.setIcon(new ImageIcon(getImage("/directions/"+directions.get(i).name()+".jpg")));
            dir.addActionListener(new ChooseActionListener(mainClient,choose,i,this));
            choose.add(dir);
        }
        choose.setTitle(info);
        addActionInfo(info);
        choose.pack();
        choose.setVisible(true);
    }

    public void getAmmoColor(List<AmmoColor> ammoColors, MainClient mainClient) {
        choose.getContentPane().removeAll();
        choose.setLayout(new GridLayout(1,3));
        choose.setLocation(300, 50);
        choose.setSize(300, 300);
        for (int i = 0; i < ammoColors.size(); i++) {
            JButton ammoColor = new JButton();
            ammoColor.setSize(150,150);
            StyleSheet styleSheet = new StyleSheet();
            ammoColor.setBackground(styleSheet.stringToColor(ammoColors.get(i).name()));
            ammoColor.setOpaque(true);
            ammoColor.setBorderPainted(false);
            ammoColor.addActionListener(new ChooseActionListener(mainClient,choose,i,this));
            choose.add(ammoColor);
        }
        choose.setTitle("Choose Ammocolor");
        choose.pack();
        choose.setVisible(true);
    }

    public void getEffect(List<Effect> effects, MainClient mainClient,boolean optional) {
        selectEffect.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        selectEffect.setLayout(new BorderLayout());
        selectEffect.setSize(215, 318);
        selectEffect.setLocation(400,100);
        selectEffect.setResizable(false);
        selectEffect.getContentPane().removeAll();
        CardPanel cardPanel = new CardPanel(weaponSelected, effects, mainClient, selectEffect);
        selectEffect.getContentPane().add(cardPanel,BorderLayout.CENTER);
        if(optional){
            JButton opt=new JButton("Don't use effect");
            opt.setSize(220,20);
            opt.addActionListener(new EffectListener(mainClient,selectEffect,effects.size()));
          /*  opt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedItem(effects.size());
                    selectEffect.setVisible(false);
                }
            });*/
            selectEffect.getContentPane().add(opt,BorderLayout.SOUTH);
        }
        selectEffect.setVisible(true);
    }


    public void blockAll() {
        choose.setVisible(false);
        chooseCard.setVisible(false);
        selectEffect.setVisible(false);
        chooseRoom.setVisible(false);
        resetRooms();
        resetActions();
        removePlayerActions();
    }

    public void getRoom(List<model.board.Color> rooms, MainClient mainClient){
        choose.getContentPane().removeAll();
        choose.setLayout(new GridLayout(2,3));
        choose.setSize(450, 300);
        choose.setLocation(300,50);
        choose.setResizable(false);
        StyleSheet s= new StyleSheet();
        for(int i=0;i<rooms.size();i++){
            JButton color =new JButton();
            color.addActionListener(new ChooseActionListener(mainClient,choose,i,this));
            color.setBackground(s.stringToColor(rooms.get(i).name()));
            color.setOpaque(true);
            color.setBorderPainted(false);
            choose.add(color);
        }
        choose.setTitle("Choose Room Color");
        choose.pack();
        choose.setVisible(true);
    }

    public void setWeaponSelected(String name) {
        for (Weapon weapon : weaponList) {
            if (weapon.getName().equals(name)) {
                weaponSelected = weapon;

            }
        }
    }

    private void resetSkullboard(){
        for (JLabel label : skullboard) {
            label.setVisible(false);
            this.remove(label);
        }
    }

    public void updateSkullboard(SkullBoard skullBoard){
        int y=37;
        int x= 79+45*(8-skullBoard.getInitSkulls());
        resetSkullboard();
        skullboard=new ArrayList<>();
        StyleSheet styleSheet=new StyleSheet();
        for(int i=0;i<skullBoard.getCells().size();i++){
            if (skullBoard.getCells().get(i).getPoint()==1) {
                JLabel point = new JLabel();
                point.setSize(37,48);
                point.setLocation(x,y);
                point.setBackground(styleSheet.stringToColor(skullBoard.getCells().get(i).getKillColor().name()));
                point.setOpaque(true);
                this.add(point);
                skullboard.add(point);
            }
            else {
                JLabel point1 =new JLabel();
                point1.setSize(37,35);
                point1.setLocation(x,y-15);
                point1.setBackground(styleSheet.stringToColor(skullBoard.getCells().get(i).getKillColor().name()));
                point1.setOpaque(true);
                this.add(point1);


                JLabel point2 =new JLabel();
                point2.setSize(37,26);
                point2.setLocation(x,y+23);
                point2.setBackground(styleSheet.stringToColor(skullBoard.getCells().get(i).getKillColor().name()));

                point2.setOpaque(true);
                skullboard.add(point1);
                skullboard.add(point2);
                this.add(point2);
            }
            if(i>skullBoard.getInitSkulls())
                x+=22;
            else
                x+=45;
        }
    }

    public void setMessageScrollPane(){
        messageScrollPane.setLocation(1030,240);
        messageScrollPane.setSize(250,170);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setVisible(true);
    }

    public void addAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks){
        this.remove(messageScrollPane);
        messagePanel.setLayout(new GridLayout(rows++,1));
        JLabel text=new JLabel(attacker.getNickname()+ "  attacked : ");
        text.setFont(new Font(null,Font.BOLD,12));
        messagePanel.add(text);

        for(Map.Entry<Player,Integer> entry: hp.entrySet()) {
            messagePanel.setLayout(new GridLayout(rows++,1));
            Player key = entry.getKey();
            Integer value = entry.getValue();
            JLabel att = new JLabel("--->  "+ key.getNickname() + "     Damage : " + value);
            messagePanel.add(att);
        }
        for(Map.Entry<Player,Integer> entry: marks.entrySet()) {
            messagePanel.setLayout(new GridLayout(rows++,1));
            Player key = entry.getKey();
            Integer value = entry.getValue();
            JLabel mark = new JLabel("--->  "+key.getNickname() + "     Marks  : " + value);

            messagePanel.add(mark);
        }
        messageScrollPane =new JScrollPane(messagePanel);
        setMessageScrollPane();
        this.add(messageScrollPane);
    }

    public void addInfo(String message){
        this.remove(messageScrollPane);
        rows+=2;
        messagePanel.setLayout(new GridLayout(rows,1));
        JLabel text=new JLabel("INFO: ");
        text.setFont(new Font(null,Font.BOLD,12));
        JLabel info=new JLabel(message);
        messagePanel.add(text);
        messagePanel.add(info);
        messageScrollPane =new JScrollPane(messagePanel);
        setMessageScrollPane();
        this.add(messageScrollPane);
    }

    public void addActionInfo(String info){
        help.setText("HELP: " + info);
    }

    public Image getImage (String images){
        Image img= null;
        try {
             img= ImageIO.read(MapPanel.class.getResourceAsStream(images));
        } catch (IOException e){
            logger.log(Level.WARNING, "Image not loaded correctly", e);
        }
        return img;
    }
}

