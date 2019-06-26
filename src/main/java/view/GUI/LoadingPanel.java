package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import static javax.swing.SwingConstants.CENTER;

public class LoadingPanel extends JPanel {

    public LoadingPanel(){
        this.setLayout(new FlowLayout());
        this.setBackground(Color.darkGray);
        JLabel loading;
        loading = new JLabel();
        loading.setOpaque(false);
        loading.setSize(300,300);
        //loading.setBorderPainted (false);
        loading.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"loading.gif"));
        this.add(loading,CENTER);
    }
}
