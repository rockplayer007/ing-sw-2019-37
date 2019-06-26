package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoadingPanel extends JPanel {
    private JLabel loading;
    public LoadingPanel(){
        this.setLayout(new BorderLayout());
        this.setBackground(Color.darkGray);
        loading = new JLabel();
        loading.setOpaque(false);
        loading.setSize(300,300);
       // loading.setBorderPainted (false);
        loading.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"loading.gif"));
        this.add(loading, BorderLayout.CENTER);
    }
}
