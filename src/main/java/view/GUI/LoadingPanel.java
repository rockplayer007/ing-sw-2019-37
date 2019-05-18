package view.GUI;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {
    private JButton loading;
    public LoadingPanel(){
        this.setLayout(new BorderLayout());
        this.setBackground(Color.darkGray);
        loading = new JButton();
        loading.setOpaque(false);
        loading.setSize(300,300);
        loading.setBorderPainted (false);
        loading.setIcon(new ImageIcon("./src/main/resources/loading.gif"));
        this.add(loading, BorderLayout.CENTER);
    }
}
