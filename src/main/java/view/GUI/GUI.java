package view.GUI;
import network.client.MainClient;
import view.ViewInterface;

import javax.swing.*;
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

        mainClient.connect();
        logIn(true);

    }

    public void logIn(boolean ask) {
        if (ask) {
            login = new JFrame("ADRENALINA");
            login.setSize(1280, 1024);
            login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            login.setBackground(Color.DARK_GRAY);
            LoginPanel loginPanel = new LoginPanel();
            login.getContentPane().add(loginPanel);
            login.setVisible(true);
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
                    mainClient.sendCredentials();
                }});
            loginPanel.add(submitButton, gbc);
            login.getContentPane().add(loginPanel);

        } else {
            //System.out.println("Welcome " + mainClient.getUsername());
            //fare pagina di caricamento
        }

    }

    @Override
    public void chooseBoard(Map<Integer, String> possibleBoards) {

    }
}

   /*
public static void main(String args[])
    {
        JFrame login;
        JFrame map;
        login = new JFrame("ADRENALINA");
        map = new JFrame("ADRENALINA");
        //exit = new JFrame("");
        login.setSize(1280,1024);
        //login.setResizable(false);
        map.setSize(1280,1024);
        Object[] options = {"Yes", "No"};
        int n =JOptionPane.showOptionDialog(exit, "Are you sure you want to exit the game?",
                "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[2]);
        if (n == JOptionPane.YES_OPTION){
            System.out.println( "Selezionata opzione " + options[0] ); }
        else if (n == JOptionPane.NO_OPTION){
            System.out.println( "Selezionata opzione " + options[1] );

        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setBackground(Color.DARK_GRAY);
        LoginPanel loginPanel = new LoginPanel();

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
               // System.out.println("start");
                System.out.println(loginPanel.getInsNickname());
                System.out.println(loginPanel.getConnection());
            }});
        loginPanel.add(submitButton, gbc);
        login.getContentPane().add(loginPanel);
        //add(submitButton, gbc);
     //   System.out.println(loginPanel.getInsNickname());
       // MapPanel mapPanel = new MapPanel();
       // map.getContentPane().add(mapPanel);
        login.setVisible(true);
       // map.setVisible(true);
    }
}
*/

