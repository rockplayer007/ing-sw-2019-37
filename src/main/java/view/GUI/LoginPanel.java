package view.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class LoginPanel extends JPanel implements ActionListener {

    private JLabel text;
    private JLabel text2;
    private JLabel nickname;
    private JTextField insNickname;
    private JLabel nicknameError;
    private JLabel connectionError;
    private Image image;
    private JRadioButton rmi;
    private JRadioButton socket;
    private ButtonGroup connGroup = new ButtonGroup();
    private String connSelected="null";
    private JTextField ip;
    private boolean nicknameErr;
    private boolean connectionErr;

 public LoginPanel()  {
        JLabel ipText;
        image = Toolkit.getDefaultToolkit().createImage("."+ File.separatorChar+"src"+File.separatorChar+"main"+File.separatorChar+"resources"+File.separatorChar +"backgroundimage.png");
        loadImage(image);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        Font f;
        f = new Font("Phosphate", Font.BOLD, 40);
        this.text = new JLabel("LOGIN");
        this.text.setFont(f);
        gbc.gridx=2;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 10);
        this.add(this.text, gbc);

        this.nickname = new JLabel("Nickname:");
        gbc.gridx = 1;
        gbc.gridy = 3;
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
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(this.nicknameError, gbc);

        this.text2=new JLabel("Select Connection :");
        gbc.gridx=1;
        gbc.gridy=5;
        this.add(this.text2, gbc);

        this.rmi = new JRadioButton("RMI");
        gbc.gridx= 2;
        gbc.gridy=5;
        this.connGroup.add(rmi);
        rmi.addActionListener(this);
        this.add(this.rmi, gbc);

        this.socket = new JRadioButton("SOCKET");
        gbc.gridx= 2;
        gbc.gridy=6;
        this.connGroup.add(socket);
        socket.addActionListener(this);
        this.add(this.socket, gbc);

        this.connectionError = new JLabel("Please insert the connection type.");
        this.connectionError.setForeground(Color.RED);
        this.connectionError.setVisible(false);
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(this.connectionError, gbc);

        ipText=new JLabel("IP address of the server");
        gbc.gridx = 1;
        gbc.gridy = 8;
        this.add(ipText, gbc);

        ip=new JTextField("127.0.0.1",15);
        gbc.gridx=2;
        gbc.gridy=8;
     this.add(ip, gbc);
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

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(image, 0, 0, null);
        super.paintComponent(g);
    }

    public String getInsNickname(){
        return insNickname.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         connSelected = e.getActionCommand();

    }

    public String getConnSelected(){
        return connSelected;
    }

    public void setNicknameErr(String err){

        this.nicknameError.setText(err);
        this.nicknameError.setVisible(true);
    }

    public void setNicknameError(Boolean err){
        this.nicknameError.setVisible(err);
        this.nicknameErr=!err;
    }

    public void setConnectionError(Boolean err){
        this.connectionError.setVisible(err);
        this.connectionErr=!err;
    }
    public String getIp(){
     return ip.getText();
    }

    public boolean getConnectionErr(){
        return connectionErr;
    }

    public boolean getNicknameErr(){
        return nicknameErr;
    }

    public boolean getConnection(){

        return connSelected.equals("SOCKET");
    }
}
