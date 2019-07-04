package view.GUI;
import model.board.GameBoard;
import model.board.SkullBoard;
import model.board.Square;
import model.card.*;
import model.player.ActionOption;
import model.player.Player;
import network.client.MainClient;
import view.ViewInterface;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.Map;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;


public class GUI implements ViewInterface {


    private MainClient mainClient;
    private JFrame frame = new JFrame("ADRENALINE");
    private boolean first=true;
    private boolean firstUpdate=true;
    private MapPanel mapPanel;
    private JFrame jDialog= new JFrame("TIMEOUT");
    private Clip sound = null;
    private JFrame infof= new JFrame("INFO");



    public GUI(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    @Override
    public void launch(){
        addMusic("intro1");
        logIn(true);
    }

public void addMusic(String name){
    AudioInputStream audio;
    if(sound !=null)
        sound.stop();
    try {ClassLoader classLoader = GUI.class.getClassLoader();
        InputStream is= classLoader.getResourceAsStream(name+".wav");
        audio= AudioSystem.getAudioInputStream(new BufferedInputStream(is));
        sound = AudioSystem.getClip();
        sound.open(audio);
        sound.loop(LOOP_CONTINUOUSLY);
        sound.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {}

}

public void logIn(boolean ask) {
        if (ask) {
            frame.getContentPane().removeAll();
            frame.setSize(1280, 750);

            if(first) {
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        int option = JOptionPane.showConfirmDialog(frame, "Do you want to Exit ?", "Exit Confirmation ", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION)
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        else if (option == JOptionPane.NO_OPTION)
                            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                });
                try {
                    frame.setIconImage(ImageIO.read(MapPanel.class.getResourceAsStream("/logo.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            frame.setBackground(Color.DARK_GRAY);
            LoginPanel loginPanel = new LoginPanel();
            loginPanel.setName("loginPanel");
            frame.getContentPane().add(loginPanel);
            JButton submitButton = new JButton("START THE GAME");
            Font f=new Font("Phosphate", Font.PLAIN, 20);
            submitButton.setFont(f);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx=2;
            gbc.gridy=9;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(50, 0, 0, 3);
            if(!first) {
                loginPanel.setNicknameErr("Please insert another Nickname");
            }
            submitButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {

                    if(loginPanel.getInsNickname().equals("")){
                        loginPanel.setNicknameError(true);
                    }
                    else loginPanel.setNicknameError(false);

                    if (loginPanel.getConnSelected().equals("null")){
                        loginPanel.setConnectionError(true);
                    }  else loginPanel.setConnectionError(false);

                    if (loginPanel.getConnectionErr()&&loginPanel.getNicknameErr()&&!loginPanel.getIp().isEmpty()) {
                        frame.getContentPane().removeAll();
                        LoadingPanel loadingPanel = new LoadingPanel();
                        loadingPanel.setName("loading");
                        frame.getContentPane().add(loadingPanel);
                        MainClient.setSocket(loginPanel.getConnection());
                        if(loginPanel.getIp().equals("127.0.0.1"))
                            MainClient.setServerIp("localhost");
                        else
                            MainClient.setServerIp(loginPanel.getIp());
                        frame.setVisible(true);
                        if(first){
                            try {
                                mainClient.connect();
                            } catch (NotBoundException | IOException ignored) {
                                disconnection();
                            }
                        }
                        first=false;
                        mainClient.setUsername(loginPanel.getInsNickname());
                        mainClient.sendCredentials();
                    }
                }});
            loginPanel.add(submitButton, gbc);
            JButton rules=new JButton("Game Rules");
            gbc.gridy=10;
            gbc.gridx=2;
            if (first) {
                rules.addActionListener(new RulesActionListener());
            }
            loginPanel.add(rules, gbc);
            frame.getContentPane().add(loginPanel);
            frame.setVisible(true);

        } else {
            frame.getContentPane().removeAll();
            LoadingPanel loadingPanel = new LoadingPanel();
            frame.getContentPane().add(loadingPanel);
            frame.setVisible(true);
        }
    }

    @Override
    public void chooseBoard(Map<Integer, String> possibleBoards) {
        frame.setBackground(Color.DARK_GRAY);
        frame.getContentPane().removeAll();
        SelectMapPanel slmp = new SelectMapPanel(possibleBoards);
        slmp.setName("chooseBoard");
        JButton submit = new JButton("USE MAP");
        Font f=new Font("Phosphate", Font.PLAIN, 20);
        submit.setFont(f);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy=4;
        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                mainClient.sendSelectedBoard(slmp.getMapSelected());
                frame.getContentPane().removeAll();
                LoadingPanel loadingPanel = new LoadingPanel();
                loadingPanel.setName("loading");
                frame.getContentPane().add(loadingPanel);
                frame.setVisible(true);

            }});
        slmp.add(submit,gbc);
        frame.getContentPane().add(slmp);
        frame.setVisible(true);
    }



    @Override
    public void timeout() {
        jDialog.getContentPane().removeAll();
        JLabel label = new JLabel("The time to perform the action has expired.");
        jDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jDialog.setSize(300, 100);
        jDialog.setLocation(500,10);
        jDialog.setLayout(new BorderLayout());
        jDialog.add(label,BorderLayout.CENTER);
        JButton reconnect = new JButton("Reconnect");
        reconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainClient.resetTimout();
                mainClient.sendCredentials();
                jDialog.setVisible(false);
            }
        });
        jDialog.add(reconnect,BorderLayout.SOUTH);
        jDialog.setVisible(true);
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.getName().equals("chooseBoard")) {
                frame.getContentPane().removeAll();
                LoadingPanel loadingPanel = new LoadingPanel();
                frame.getContentPane().add(loadingPanel);
                frame.setVisible(true);
            }
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.blockAll();
            }
        }

    }

    @Override
    public void updateAll(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard) {
        if(firstUpdate){
            frame.getContentPane().removeAll();
            frame.setSize(1300,750);
            frame.setResizable(false);
            mapPanel = new MapPanel(board);
            mapPanel.setName("mapPanel");
            mapPanel.createMapButton();
            frame.getContentPane().add(mapPanel);
            frame.setVisible(true);
            addMusic("mapmusic");
            firstUpdate=false;
        }

        mapPanel.updBoardGui(board);
        mapPanel.updatePlayerBoard(board.getPlayersOnMap());
        mapPanel.updateWeapon(board,mainClient);
        mapPanel.updatePowerup(myPowerups);
        mapPanel.updateSkullboard(skullBoard);

    }

    @Override
    public void choosePowerup(List<Powerup> powerups, boolean optional, String info) {
        if(frame.getContentPane().getComponents().length>0){
            Component component = frame.getContentPane().getComponent(0);
        if(component.equals(mapPanel)){
              MapPanel mpl = (MapPanel) component;
                mpl.getPowerupSelected(powerups, optional, mainClient,info);
              jDialog.setVisible(false);
          }
        }
    }

    @Override
    public void chooseWeapon(List<Weapon> weapons, boolean optional, String info) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getWeaponSelected(weapons, optional, mainClient, info);
            }
        }
    }

    @Override
    public void chooseEffect(List<Effect> effects, boolean optional) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getEffect(effects, mainClient, optional);
            }
        }
    }

    @Override
    public void choosePlayer(List<Player> players, String info) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getPlayer(players, mainClient, info);
            }
        }
    }

    @Override
    public void chooseDirection(List<Square.Direction> directions, String info) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getDirection(directions, mainClient, info);
            }
        }
    }

    @Override
    public void chooseAmmoColor(List<AmmoColor> ammoColors) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getAmmoColor(ammoColors, mainClient);
            }
        }
    }

    @Override
    public void chooseRoom(List<model.board.Color> rooms) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getRoom(rooms, mainClient);
            }
        }
    }

    @Override
    public void showAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.addAttack(attacker, hp, marks);
            }
        }
    }

    @Override
    public void showInfo(String info) {
        if(frame.getContentPane().getComponents().length>0) {
        Component component = frame.getContentPane().getComponent(0);
        if(component.equals(mapPanel)){
            MapPanel mpl = (MapPanel) component;
            mpl.addInfo(info);
        }
        else{
            JLabel message = new JLabel(info);
            infof.setLayout(new BorderLayout());
            infof.getContentPane().removeAll();
            infof.add(message,BorderLayout.CENTER);
            infof.pack();
            infof.setVisible(true);
        }
        }
    }

    @Override
    public void showScore(List<Player> score) {

        frame.getContentPane().removeAll();
        frame.setSize(750,800);
        frame.setLocation(300,0);
        frame.setResizable(false);
        ScorePanel scorePanel= new ScorePanel(score);
        frame.getContentPane().add(scorePanel);
        frame.setVisible(true);
        jDialog.setVisible(false);

    }

    @Override
    public void chooseAction(List<ActionOption> actions) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getAction(actions, mainClient);
                jDialog.setVisible(false);
            }
        }
    }

    @Override
    public void chooseSquare(List<Square> squares, String info) {
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if (component.equals(mapPanel)) {
                MapPanel mpl = (MapPanel) component;
                mpl.getSquareSelected(squares, mainClient, info);
            }
        }
    }

    @Override
    public void disconnection(){
        if(frame.getContentPane().getComponents().length>0) {
            Component component = frame.getContentPane().getComponent(0);
            if(component.equals(mapPanel)){
                MapPanel mpl = (MapPanel) component;
                mpl.blockAll();
            }
        }
        JOptionPane.showMessageDialog(frame,"Check your connection. Then restart the game.","DISCONNECTION",JOptionPane.WARNING_MESSAGE);
        first=true;
        firstUpdate=true;
        logIn(true);
    }
}


