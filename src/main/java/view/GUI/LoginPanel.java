package view.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginPanel extends JPanel {

    private JLabel text;
    private JLabel text2;
    private JLabel nickname;
    private JTextField insNickname;
    private JLabel nicknameError;
    private Image image;
    private JRadioButton hero1;
    private JRadioButton hero2;
    private JRadioButton hero3;
    private JRadioButton hero4;
    private JRadioButton hero5;
    private ButtonGroup heroes = new ButtonGroup();
    private JButton submitButton;


    private void build() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Font f;
        f = new Font("Phosphate", Font.BOLD, 40);
        this.text = new JLabel("LOGIN");
        this.text.setFont(f);
        gbc.gridx=2;
        gbc.gridy=2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 10);
        this.add(this.text, gbc);

        this.nickname = new JLabel("Nickname:");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 0, 0, 10);
        this.add(this.nickname, gbc);

        this.insNickname = new JTextField(15);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(this.insNickname, gbc);

        this.nicknameError = new JLabel("Please insert your Nickname.");
        this.nicknameError.setForeground(Color.RED);
        this.nicknameError.setVisible(false);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(this.nicknameError, gbc);

        this.text2=new JLabel("Select Hero :");
        f=new Font("Phosphate", Font.PLAIN, 14);
        this.text2.setFont(f);
        gbc.gridx=2;
        gbc.gridy=4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 10);
        this.add(this.text2, gbc);


        this.hero1 = new JRadioButton("Dozer");
        gbc.gridx= 0;
        gbc.gridy=5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.heroes.add(hero1);
        this.add(this.hero1, gbc);

        this.hero2 = new JRadioButton("Banshee");
        gbc.gridx= 1;
        gbc.gridy=5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.heroes.add(hero2);
        this.add(this.hero2, gbc);

        this.hero3 = new JRadioButton("D-Struct-Or");
        gbc.gridx= 2;
        gbc.gridy=5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.heroes.add(hero3);
        this.add(this.hero3, gbc);

        this.hero4 = new JRadioButton("Violet");
        gbc.gridx= 3;
        gbc.gridy=5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.heroes.add(hero4);
        this.add(this.hero4, gbc);

        this.hero5 = new JRadioButton("Sprong");
        gbc.gridx= 4;
        gbc.gridy=5;
        gbc.anchor = GridBagConstraints.CENTER;
        this.heroes.add(hero5);
        this.add(this.hero5, gbc);

        this.submitButton = new JButton("START THE GAME");
        f=new Font("Phosphate", Font.PLAIN, 20);
        this.submitButton.setFont(f);
        gbc.gridx=2;
        gbc.gridy=8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(50, 0, 0, 0);
        submitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.out.println("start");
                //method to launch game
            }});
        this.add(this.submitButton, gbc);
    }

    private void loadImage(Image img) {
        try {
            MediaTracker track = new MediaTracker(this);
            track.addImage(img, 0);
            track.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(image, 0, 0, null);
        super.paintComponent(g);
    }


    public LoginPanel()  {
        this.build();
        image = Toolkit.getDefaultToolkit().createImage("./src/main/resources/backgroundimage.png");
        loadImage(image);
    }


}
