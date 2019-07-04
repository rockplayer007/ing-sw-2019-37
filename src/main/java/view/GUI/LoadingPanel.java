package view.GUI;

import javax.swing.*;
import java.awt.*;
import static javax.swing.SwingConstants.CENTER;

public class LoadingPanel extends JPanel {
    public LoadingPanel(){
        this.setLayout(new FlowLayout());
        this.setBackground(Color.darkGray);
        JLabel loading;
        loading = new JLabel();
        loading.setOpaque(false);
        loading.setSize(300,300); loading.setIcon(new ImageIcon(LoadingPanel.class.getResource("/loading.gif")));
        this.add(loading,CENTER);
    }
}
