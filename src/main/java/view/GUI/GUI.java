package view.GUI;
import javax.swing.*;
import java.awt.*;

public class GUI {

    public static void main(String args[])
    {
        JFrame login;
        JFrame map;
        login = new JFrame("ADRENALINA");
        map = new JFrame("ADRENALINA");
        //exit = new JFrame("");
        login.setSize(1280,1024);
        map.setSize(1280,1024);
       /* Object[] options = {"Yes", "No"};
        int n =JOptionPane.showOptionDialog(exit, "Are you sure you want to exit the game?",
                "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[2]);
        if (n == JOptionPane.YES_OPTION){
            System.out.println( "Selezionata opzione " + options[0] ); }
        else if (n == JOptionPane.NO_OPTION){
            System.out.println( "Selezionata opzione " + options[1] );
            */
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setBackground(Color.DARK_GRAY);
        LoginPanel loginPanel = new LoginPanel();
        login.getContentPane().add(loginPanel);
        MapPanel mapPanel = new MapPanel();
        map.getContentPane().add(mapPanel);
        login.setVisible(true);
        map.setVisible(true);
    } }

