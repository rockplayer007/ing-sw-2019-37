package view.GUI;
import model.board.GameBoard;
import model.board.Square;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;
import network.client.MainClient;
import view.ViewInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.Map;


public class GUI implements ViewInterface {


    private MainClient mainClient;
    private JFrame frame = new JFrame("ADRENALINE");
    private boolean first=true;
    private boolean firstUpdate=true;
    private MapPanel mapPanel;


    public GUI(MainClient mainClient) {
        this.mainClient = mainClient;
    }

    @Override
    public void launch() throws NotBoundException, IOException {

        logIn(true);
        mainClient.connect();

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
            gbc.gridy=8;
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

                    if (loginPanel.getConnectionErr()&&loginPanel.getNicknameErr()) {
                        first=false;
                        frame.getContentPane().removeAll();
                        LoadingPanel loadingPanel = new LoadingPanel();
                        frame.getContentPane().add(loadingPanel);
                        mainClient.setUsername(loginPanel.getInsNickname());
                        mainClient.setSocket(loginPanel.getConnection());
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
        JDialog jDialog= new JDialog(frame,"TIMEOUT");
        JLabel label = new JLabel("The time to perform the action has expired.");
        jDialog.add(label);
        jDialog.setSize(300, 100);
        jDialog.setLocation(500,10);
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
            //mapPanel.blockAll; devo implementarlo
        }

    }

    @Override
    public void updatedBoard(GameBoard board) {
        if(firstUpdate){
            frame.getContentPane().removeAll();
            frame.setResizable(false);
            mapPanel = new MapPanel(board);
            mapPanel.setName("mapPanel");
            mapPanel.createMapButton();
            frame.getContentPane().add(mapPanel);
            frame.setVisible(true);
            firstUpdate=false;
        }

        mapPanel.updBoardGui(board);


    }

    @Override
    public void choosePowerup(List<Powerup> powerups, boolean optional) {
        Component component =frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))){
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getCardSelected(powerups,optional,mainClient);
        }
    }

    @Override
    public void chooseWeapon(List<Weapon> weapons, boolean optional) {

    }

    @Override
    public void chooseAction(List<ActionOption> actions) {
        Component component = frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))) {
            MapPanel mapPanel = (MapPanel) component;
            mapPanel.getAction(actions, mainClient);
        }
    }

    @Override
    public void chooseSquare(List<Square> squares) {
        Component component =frame.getContentPane().getComponent(0);
        if ((component.getName().equals("mapPanel"))){
            MapPanel map = (MapPanel) component;
            map.getSquareSelected(squares,mainClient);
        }
    }


}


