package view.GUI;
import network.client.MainClient;
import view.ViewInterface;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Map;


public class GUI implements ViewInterface {


    private MainClient mainClient;
    private JFrame login;

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
            login = new JFrame("ADRENALINA");
            login.setSize(1280, 1024);
            login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            login.setBackground(Color.DARK_GRAY);
            LoginPanel loginPanel = new LoginPanel();
            login.getContentPane().add(loginPanel);

            JButton submitButton = new JButton("START THE GAME");
            Font f=new Font("Phosphate", Font.PLAIN, 20);
            submitButton.setFont(f);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx=2;
            gbc.gridy=8;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(50, 0, 0, 3);
            submitButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    mainClient.setUsername(loginPanel.getInsNickname());
                    MainClient.setSocket(loginPanel.getConnection());
                    mainClient.sendCredentials();
                    login.setVisible(false);
                }});
            loginPanel.add(submitButton, gbc);
            login.getContentPane().add(loginPanel);
            login.setVisible(true);

        } else {
            //System.out.println("Welcome " + mainClient.getUsername());
            //fare pagina di caricamento
        }

    }

    @Override
    public void chooseBoard(Map<Integer, String> possibleBoards) {
        JFrame selmap = new JFrame();
        selmap.setSize(1280, 1024);
        selmap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SelectMapPanel slmp = new SelectMapPanel(possibleBoards);
        selmap.getContentPane().add(slmp);
        selmap.setVisible(true);
        JButton submit = new JButton("USE MAP");
        Font f=new Font("Phosphate", Font.PLAIN, 20);
        submit.setFont(f);
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.gridx=3;
        gbc.gridy=4;
        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //mainClient.sendSelectedBoard(slmp.getMapSelected());
                System.out.println(slmp.getMapSelected());
                selmap.setVisible(false);
            }});
        slmp.add(submit,gbc);
        selmap.getContentPane().add(slmp);
        selmap.setVisible(true);
    }
}


