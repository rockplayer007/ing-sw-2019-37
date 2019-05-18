package view.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoadingPanel extends JPanel {
    private JButton loading;
    public LoadingPanel(){
        this.setLayout(new BorderLayout());
        this.setBackground(Color.darkGray);
        loading = new JButton();
        loading.setOpaque(false);
        loading.setSize(300,300);
        loading.setBorderPainted (false);
        loading.setIcon(new ImageIcon("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"loading.gif"));
        this.add(loading, BorderLayout.CENTER);
    }
}
