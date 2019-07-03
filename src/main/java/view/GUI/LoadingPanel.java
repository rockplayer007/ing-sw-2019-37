package view.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.SwingConstants.CENTER;

public class LoadingPanel extends JPanel {
    //private static final Logger logger = Logger.getLogger(LoadingPanel.class.getName());

    public LoadingPanel(){
        JLabel label=new JLabel("Welcome");
        this.setLayout(new BorderLayout());
        /*
        this.setLayout(new FlowLayout());
        this.setBackground(Color.darkGray);
        JLabel loading;
        loading = new JLabel();
        loading.setOpaque(false);
        loading.setSize(300,300);
        Image image = null;
        try {
            image= ImageIO.read(LoadingPanel.class.getResourceAsStream("/loading.gif"));
        } catch (IOException e){
            logger.log(Level.WARNING, "Image not loaded correctly", e);
        }
       // loading.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"loading.gif"));
        loading.setIcon(new ImageIcon(image));*/
        //this.add(loading,CENTER);

        this.add(label,BorderLayout.CENTER);
    }
}
