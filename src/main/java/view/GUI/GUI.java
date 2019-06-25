package view.GUI;
import model.board.GameBoard;
import model.board.SkullBoard;
import model.board.Square;
import model.card.*;
import model.player.ActionOption;
import model.player.Player;
import network.client.MainClient;
import view.ViewInterface;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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


    public GUI(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    @Override
    public void launch() throws NotBoundException, IOException {
        addMusic("intro1");
        logIn(true);
    }

public void addMusic(String name){
    AudioInputStream audio;
    if(sound !=null)
        sound.stop();
    try {
        audio = AudioSystem.getAudioInputStream(new File("." + File.separatorChar + "src" + File.separatorChar
                + "main" + File.separatorChar + "resources" + File.separatorChar + name +".wav"));
         sound = AudioSystem.getClip();
        sound.open(audio);
        sound.loop(LOOP_CONTINUOUSLY);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {}



}
    public void logIn(boolean ask) {
        if (ask) {
            frame.getContentPane().removeAll();
            frame.setSize(1280, 1024);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.DARK_GRAY);
            LoginPanel loginPanel = new LoginPanel();
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
                            } catch (NotBoundException e1) {
                                //e1.printStackTrace();
                            } catch (IOException e1) {
                                //e1.printStackTrace();
                            }
                        }
                        first=false;
                        mainClient.setUsername(loginPanel.getInsNickname());
                        mainClient.sendCredentials();
                    }
                }});
            loginPanel.add(submitButton, gbc);
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
                mainClient.sendCredentials();
                jDialog.setVisible(false);
            }
        });
        jDialog.add(reconnect,BorderLayout.SOUTH);
        jDialog.setVisible(true);
        Component component =frame.getContentPane().getComponent(0);
        if (component.getName().equals("chooseBoard")){
            frame.getContentPane().removeAll();
            LoadingPanel loadingPanel= new LoadingPanel();
            frame.getContentPane().add(loadingPanel);
            frame.setVisible(true);
        }
        if ((component.getName().equals("mapPanel"))){
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.blockAll();
        }

    }

    @Override
    public void updateAll(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard) {
        if(firstUpdate){
            frame.getContentPane().removeAll();
            frame.setSize(1280,1024);
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
        mapPanel.updatePlayerBoard(board);
        mapPanel.updateWeapon(board,mainClient);
        mapPanel.updatePowerup(myPowerups);
        mapPanel.updateSkullboard(skullBoard);

    }

    @Override
    public void choosePowerup(List<Powerup> powerups, boolean optional, String info) {
        Component component = frame.getContentPane().getComponent(0);
          if ((component.getName().equals("mapPanel"))) {
              MapPanel mapPanel = (MapPanel) component;
              mapPanel.getPowerupSelected(powerups, optional, mainClient,info);
              jDialog.setVisible(false);
          }
    }

    @Override
    public void chooseWeapon(List<Weapon> weapons, boolean optional, String info) {

        Component component = frame.getContentPane().getComponent(0);
          if ((component.getName().equals("mapPanel"))) {
              MapPanel mapPanel = (MapPanel) component;
              mapPanel.getWeaponSelected(weapons,optional,mainClient,info);
          }
    }

    @Override
    public void chooseEffect(List<Effect> effects, boolean optional) {
        Component component =frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))){
            MapPanel map = (MapPanel) component;
            map.getEffect(effects,mainClient,optional);
        }
    }

    @Override
    public void choosePlayer(List<Player> players, String info) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getPlayer(players,mainClient,info);
        }
    }

    @Override
    public void chooseDirection(List<Square.Direction> directions, String info) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getDirection(directions,mainClient,info);
        }
    }

    @Override
    public void chooseAmmoColor(List<AmmoColor> ammoColors) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getAmmoColor(ammoColors,mainClient);
        }

    }

    @Override
    public void chooseRoom(List<model.board.Color> rooms) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getRoom(rooms,mainClient);
        }
    }

    @Override
    public void showAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.addAttack(attacker,hp,marks);
        }
    }

    @Override
    public void showInfo(String info) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.addInfo(info);
        }
    }

    @Override
    public void showScore(List<Player> score) {
        JFrame x= new JFrame();
       // x=frame;
        x.getContentPane().removeAll();
        x.setSize(750,800);
        x.setLocation(300,0);
        x.setResizable(false);
        ScorePanel scorePanel= new ScorePanel(score);
        x.getContentPane().add(scorePanel);
        x.setVisible(true);
        jDialog.setVisible(false);

    }

    @Override
    public void chooseAction(List<ActionOption> actions) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getAction(actions, mainClient);
            jDialog.setVisible(false);
        }
    }

    @Override
    public void chooseSquare(List<Square> squares, String info) {
        Component component =frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))){
            MapPanel map = (MapPanel) component;
            map.getSquareSelected(squares,mainClient,info);
        }
    }


    @Override
    public void disconnection(){

    }
}


