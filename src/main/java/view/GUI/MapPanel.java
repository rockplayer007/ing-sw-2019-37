package view.GUI;

import model.board.*;
import model.card.*;
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
import java.util.Map;


public class MapPanel extends JLayeredPane {
    private transient Image image;
    private List<JMapButton> roomButton = new ArrayList<>();
    private List<JPlayerButton> playerIcon = new ArrayList<>();
    private List<WeaponButton> myWeapon = new ArrayList<>();
    private List<JButton> myPowerup = new ArrayList<>();
    private List<WeaponButton> weaponIcon = new ArrayList<>();
    private List<JButton> powerupButton = new ArrayList<>();
    private List<JLabel> ammoColors = new ArrayList<>();
    private int[] positionX = new int[4];
    private int[] positionY = new int[3];
    private transient GameBoard board;
    private JFrame chooseCard;
    private JFrame choose;
    JFrame selectEffect = new JFrame("Choose Effect");
    // private JFrame chooseCard = new JFrame();
    private JFrame playerboards = new JFrame("BOARDS");
    private JFrame chooseRoom;
    private List<JLabel> ammocards = new ArrayList<>();
    private List<JButton> actions = new ArrayList<>();
    private List<Weapon> weaponList = new ArrayList<>();
    private List<JLabel> infoWeapon=new ArrayList<>();
    private Player myplayer = null;
    private Weapon weaponSelected;

    public MapPanel(GameBoard board) {
        JButton pBoards;
        this.setLayout(null);
        this.board = board;
        image = Toolkit.getDefaultToolkit().createImage(getUrlMap(board.getId()));
        loadImages(image);
        setRoomCoordinate();
        pBoards = new JButton("SHOW PLAYERS BOARDS");
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
        choose = new JFrame();
        choose.setLocation(300, 50);
        choose.setMinimumSize(new Dimension(300, 300));
        choose.setMaximumSize(new Dimension(300, 300));
        choose.setLayout(new GridLayout(2, 2));
        choose.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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

    private String getUrlMap(int code) {

        return "." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" +
                File.separatorChar + "maps" + File.separatorChar + "map" + code + ".png";

    }

    public void getPlayer(List<Player> players, MainClient mainClient) {
        resetRooms();
        resetPlayersEnabled();
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < playerIcon.size(); j++) {
                if (players.get(i).getNickname().equals(playerIcon.get(j).getPlayer().getNickname())) {
                    playerIcon.get(j).setEnabled(true);
                    /*
                    ActionListener actionListener[]=playerIcon.get(j).getActionListeners();
                    for (int k=0;k<actionListener.length;k++){
                        playerIcon.get(j).removeActionListener(actionListener[k]);
                    }*/
                    int s = i;
                    playerIcon.get(j).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            removePlayerActions();
                            mainClient.sendSelectedPlayer(s);
                            enablePlayers();
                            //setVisibleRooms();
                        }
                    });
                }
            }
        }
    }

    private void removePlayerActions() {
        for (int i = 0; i < playerIcon.size(); i++) {
            ActionListener actionListener[] = playerIcon.get(i).getActionListeners();
            for (int k = 0; k < actionListener.length; k++) {
                playerIcon.get(i).removeActionListener(actionListener[k]);
            }
        }
    }

    private void resetPlayersEnabled() {
        for (int i = 0; i < playerIcon.size(); i++) {
            playerIcon.get(i).setEnabled(false);
        }
    }

    private void enablePlayers() {
        for (int i = 0; i < playerIcon.size(); i++) {
            playerIcon.get(i).setEnabled(true);
        }
    }

    public void resetRooms() {
        for (int i = 0; i < roomButton.size(); i++) {
            //roomButton.get(i).setEnabled(false);
            roomButton.get(i).setVisible(false);
        }
    }

    private void setVisibleRooms() {
        for (int i = 0; i < roomButton.size(); i++) {
            roomButton.get(i).setVisible(true);
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
        weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                + File.separatorChar + "resources" + File.separatorChar + "weapon" + File.separatorChar + weapon.getName() + ".png"));
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
        infoWeapon.add(label);
        this.add(label);

    }

    public void removeWeapon() {
        for (int i = 0; i < myWeapon.size(); i++) {
            myWeapon.get(i).setVisible(false);
            this.remove(myWeapon.get(i));
            infoWeapon.get(i).setVisible(false);
            this.remove(infoWeapon.get(i));
        }
    }

    public void resetWeaponIcon() {
        for (int i = 0; i < weaponIcon.size(); i++) {
            this.remove(weaponIcon.get(i));
        }
    }

    private void updateAmmo(Map<AmmoColor, Integer> ammos) {
        for (int i = 0; i < ammoColors.size(); i++)
            this.remove(ammoColors.get(i));

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
        removeWeapon();
        myWeapon = new ArrayList<>();
        infoWeapon=new ArrayList<>();
        for (int i = 0; i < board.getPlayersOnMap().size(); i++) {
            if (board.getPlayersOnMap().get(i).getNickname().equals(mainClient.getUsername())) {
                updateAmmo(board.getPlayersOnMap().get(i).getAmmo());
                for (int j = 0; j < board.getPlayersOnMap().get(i).getWeapons().size(); j++) {
                    addWeapon(board.getPlayersOnMap().get(i).getWeapons().get(j));
                }
            }
        }
    }

    public void removePowerup() {
        for (int i = 0; i < myPowerup.size(); i++) {
            myPowerup.get(i).setVisible(false);
            this.remove(myPowerup.get(i));
            //infoWeapon.get(i).setVisible(false);
        }

    }

    public void addPowerup(Powerup powerup) {
        JButton pow = new JButton();
        pow.setSize(80, 120);
        int i = myPowerup.size();
        if (i > 0)
            pow.setLocation(myPowerup.get(i - 1).getX() + 82, 554);
        else
            pow.setLocation(1030, 554);
        pow.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                + File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "pow" + File.separatorChar + powerup.getName() + powerup.getAmmo().toString() + ".png"));
        pow.setOpaque(false);
        pow.setContentAreaFilled(false);
        pow.setBorder(null);
        pow.setFocusPainted(false);
        myPowerup.add(pow);
        this.add(pow);
    }

    public void updatePowerup(List<Powerup> mypow) {
        removePowerup();
        myPowerup = new ArrayList<>();
        for (int i = 0; i < mypow.size(); i++) {
            addPowerup(mypow.get(i));
        }
    }

    public void createPlayerIcon(Player player) {
        JPlayerButton playerButton = new JPlayerButton(player);
        playerButton.setSize(46, 68);
        playerButton.setContentAreaFilled(false);
        playerButton.setBorder(null);
        playerButton.setFocusPainted(false);
        playerButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" +
                File.separatorChar + "heroes" + File.separatorChar + player.getHero().getName() + ".png"));
        playerButton.setOpaque(false);
        playerButton.setVisible(true);
        this.add(playerButton);
        playerIcon.add(playerButton);
    }


    public void movePlayer(int cell, Player player) {
        //aggiungere controllo se si muove nella stessa cella
        boolean created = false;
        JButton icon = new JButton();
        for (int i = 0; i < playerIcon.size(); i++) {
            if (player.getHero().getName().equals(playerIcon.get(i).getPlayer().getHero().getName())) {
                created = true;
                icon = playerIcon.get(i);
            }
        }
        if (!created)
            createPlayerIcon(player);
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

    public void createMapButton() {
        for (int i = 0; board.getSquare(i) != null; i++) {
            JMapButton room = new JMapButton(board.getSquare(i).getX(), board.getSquare(i).getY(), board.getSquare(i).getColor());
            room.setOpaque(false);
            room.setSize(149, 116);
            Border border = BorderFactory.createLineBorder(Color.red);
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
        for (int i = 0; i < ammocards.size(); i++) {
            //ammocards.get(i).setVisible(false);
            this.remove(ammocards.get(i));
        }
    }

    public void updBoardGui(GameBoard board, MainClient mainClient) {
        //this.board=b;
        //this.mainClient=mainClient;
        resetWeaponIcon();
        weaponIcon = new ArrayList<>();
        resetAmmo();
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
                            weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                                    File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "weaponmap" + File.separatorChar +
                                    "left" + File.separatorChar + weaponButton.getWeapon().getName() + ".png"));

                        } else if (board.getSquare(i).getY() == 0) {
                            weaponButton.setSize(90, 148);
                            weaponButton.setLocation(536 + 110 * j, 2);
                            weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                                    File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "weaponmap" + File.separatorChar
                                    + weaponButton.getWeapon().getName() + ".png"));
                        } else if (board.getSquare(i).getY() == 2) {
                            weaponButton.setSize(148, 90);
                            weaponButton.setLocation(880, 405 + 110 * j);
                            weaponButton.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                                    File.separatorChar + "resources" + File.separatorChar + "powerup" + File.separatorChar + "weaponmap" + File.separatorChar +
                                    "right" + File.separatorChar + weaponButton.getWeapon().getName() + ".png"));

                        }
                        weaponIcon.add(weaponButton);
                        this.add(weaponButton);
                    }
                }
            } else {
                AmmoSquare ammoSquare = (AmmoSquare) square;
                // ImageIcon imageIcon=new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"ammocard"+File.separatorChar+ ammoSquare.getAmmoCard().getName() + ".png");
                JLabel ammo = new JLabel();
                ammo.setSize(46, 68);
                //   ammo.setContentAreaFilled(false);
                ammo.setBorder(null);
                //  ammo.setFocusPainted(false);
                if (ammoSquare.getAmmoCard() != null)
                    ammo.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" +
                            File.separatorChar + "resources" + File.separatorChar + "ammocard" + File.separatorChar
                            + ammoSquare.getAmmoCard().getName() + ".png"));

                else
                    ammo.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main"
                            + File.separatorChar + "resources" + File.separatorChar + "ammocard" + File.separatorChar + "ammo.png"));
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

    public void updatePlayerBoard(GameBoard board) {
        playerboards.getContentPane().removeAll();
        playerboards.setSize(1280, 1024);
        PlayerBoardPanel pbl = new PlayerBoardPanel(board.getPlayersOnMap());
        playerboards.getContentPane().add(pbl);
        playerboards.repaint();
    }


    public void getSquareSelected(List<Square> squares, MainClient mainClient) {
        resetRooms();
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            roomButton.get(square.getId()).setEnabled(true);
            roomButton.get(square.getId()).setVisible(true);
            int x = i;
            ActionListener actionListener[] = roomButton.get(square.getId()).getActionListeners();
            for (int j = 0; j < actionListener.length; j++) {
                roomButton.get(square.getId()).removeActionListener(actionListener[j]);
            }
            roomButton.get(square.getId()).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedSquare(x);
                    resetRooms();
                }
            });
        }
    }

    private void resetPowerups() {

        for (int i = 0; i < powerupButton.size(); i++) {
            powerupButton.get(i).setVisible(false);
            powerupButton.get(i).setEnabled(false);
        }
    }


    public void getPowerupSelected(List<Powerup> powerups, boolean optional, MainClient mainClient) {
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < powerups.size(); i++) {
            cards.add(powerups.get(i).getName() + powerups.get(i).getAmmo().toString());
        }
        SelectCardPanel selectCardPanel = new SelectCardPanel(cards, optional, mainClient, chooseCard, this);
        chooseCard.getContentPane().removeAll();
        chooseCard.getContentPane().add(selectCardPanel);
        chooseCard.pack();
        chooseCard.setVisible(true);
    }

    public void getWeaponSelected(List<Weapon> weapons, boolean optional, MainClient mainClient) {
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < weapons.size(); i++) {
            cards.add(weapons.get(i).getName());
        }
        weaponList = weapons;
        chooseCard.getContentPane().removeAll();
        SelectCardPanel selectCardPanel = new SelectCardPanel(cards, optional, mainClient, chooseCard, this);
        chooseCard.getContentPane().add(selectCardPanel);
        chooseCard.pack();
        chooseCard.setVisible(true);

    }


    public void getAction(List<ActionOption> actionOptions, MainClient mainClient) {
        //bloccare altre azioni possibili
        resetRooms();
        actions = new ArrayList<>();
        JLabel text = new JLabel("Select action:"); //centrare il testo
        text.setSize(250, 20);
        text.setLocation(1030, 24);
        this.add(text);
        for (int i = 0; i < actionOptions.size(); i++) {
            JButton action = new JButton();
            action.setSize(124, 64);
            action.setOpaque(false);
            action.setContentAreaFilled(false);
            Border border = BorderFactory.createLineBorder(Color.black, 2);
            action.setBorder(border);
            action.setFocusPainted(false);
            action.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar
                    + "action" + File.separatorChar + actionOptions.get(i).toString() + ".png"));

            if (actions.isEmpty())
                action.setLocation(1030, 45);
            else {
                if (actions.get(i - 1).getX() == 1155)
                    action.setLocation(1030, actions.get(i - 1).getY() + 64);
                else
                    action.setLocation(actions.get(i - 1).getX() + 125, actions.get(i - 1).getY());
            }
            final int x = i;
            action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedBoard(x);
                    resetActions();
                    text.setVisible(false);
                }
            });
            this.add(action);
            actions.add(action);

        }
    }

    public void resetActions() {
        for (int i = 0; i < actions.size(); i++) {
            actions.get(i).setEnabled(false);
            actions.get(i).setVisible(false);
            this.remove(actions.get(i));
        }
    }

    public void getDirection(List<Square.Direction> directions, MainClient mainClient) {
        choose.getContentPane().removeAll();
        for (int i = 0; i < directions.size(); i++) {
            JButton dir = new JButton();
            dir.setSize(150, 150);
            int s = i;
            dir.setIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar
                    + "main" + File.separatorChar + "resources" + File.separatorChar
                    + "directions" + File.separatorChar + directions.get(i).toString() + ".jpg"));
            dir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainClient.sendSelectedDirection(s);
                    choose.setVisible(false);
                }
            });

            choose.add(dir);
        }
        choose.pack();
        choose.setVisible(true);
    }

    public void getAmmoColor(List<AmmoColor> ammoColors, MainClient mainClient) {
        choose.getContentPane().removeAll();
        for (int i = 0; i < ammoColors.size(); i++) {
            JButton ammoColor = new JButton();
            int x = i;
            StyleSheet styleSheet = new StyleSheet();
            ammoColor.setBackground(styleSheet.stringToColor(ammoColors.get(i).toString()));
            ammoColor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    choose.setVisible(false);
                    mainClient.sendSelectedAmmoColor(x);
                }
            });
            choose.add(ammoColor);
        }
        choose.pack();
        choose.setVisible(true);
    }

    public void getEffect(List<Effect> effects, MainClient mainClient) {
        selectEffect.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        selectEffect.setSize(220, 300);
        selectEffect.setLocation(300,50);
        selectEffect.setResizable(false);
        selectEffect.getContentPane().removeAll();
        CardPanel cardPanel = new CardPanel(weaponSelected, effects, mainClient, selectEffect);
        selectEffect.getContentPane().add(cardPanel);
        selectEffect.setVisible(true);
    }


    public void blockAll() {
        choose.setVisible(false);
        chooseCard.setVisible(false);
        selectEffect.setVisible(false);
        chooseRoom.setVisible(false);
        resetRooms();
        //setVisibleRooms();
        resetActions();
        removePlayerActions();
    }

    public void getRoom(List<model.board.Color> rooms, MainClient mainClient){
        chooseRoom=new JFrame();
        chooseRoom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        chooseRoom.setSize(450, 300);
        chooseRoom.setLocation(300,50);
        chooseRoom.setResizable(false);
        chooseRoom.setLayout(new GridLayout(2,3));
        StyleSheet s= new StyleSheet();
        for(int i=0;i<rooms.size();i++){
            JLabel color =new JLabel();
            color.setBackground(s.stringToColor(rooms.get(i).toString()));
            color.setOpaque(false);
            chooseRoom.add(color);
        }
        chooseRoom.setVisible(true);
    }

    public void setWeaponSelected(String name) {
        for (int k = 0; k < weaponList.size(); k++) {
            if (weaponList.get(k).getName().equals(name)) {
                weaponSelected = weaponList.get(k);

            }
        }
    }
}

