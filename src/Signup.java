import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Signup extends JFrame{
    Connection conn = null;
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JTextField username = new JTextField();
    JTextField idNumber = new JTextField();
    JPasswordField password = new JPasswordField();
    JLabel idNumberLable = new JLabel();
    JLabel header = new JLabel();
    JLabel usernameLabel = new JLabel();
    JLabel passwordLabel = new JLabel();
    JLabel errorMessage = new JLabel();
    JLabel errorMessage2 = new JLabel();
    JLabel errorMessage3 = new JLabel();
    JLabel successMessagelabel = new JLabel();
    JButton signinBtn = new JButton();
    int lPanelwidth = 270;
    int rPanelwidth = 490;
    int panelheight = 520;
    ImageIcon hawassaLogo = new ImageIcon(getClass().getResource("Hawassa1.png"));
    Font font = new Font("MONOSPACED", Font.BOLD, 14);
    public  Signup(int width, int height , Connection conn)
    {
        this.conn = conn;

        setTitle("IOT Clinic System");
        setSize(width, height);
        setMinimumSize(new Dimension(800,600));
        setLayout(null);

        setIconImage(hawassaLogo.getImage());
        handler h = new handler();
        handler2 h2 = new handler2();
        keyListener key = new keyListener();
        addComponentListener(h);


        leftPanel.setBounds(0, 20, lPanelwidth, panelheight);

        rightPanel.setBounds(lPanelwidth, 20, rPanelwidth, panelheight);


        getContentPane().setBackground(new Color(0x000C0E));
        leftPanel.setBackground(new Color(1, 24, 30));
        leftPanel.setLayout(null);

        header.setText("SIGN UP INTO IOT CAMPUS CLINIC SYSTEM ");
        header.setBounds(30, 105, 420, 40);
        header.setFont(new Font("MONOSPACED", Font.BOLD + Font.ROMAN_BASELINE, 16));

        idNumberLable.setText("ID number");
        idNumberLable.setBounds(30, 155, 300, 40);
        idNumberLable.setFont(font);

        usernameLabel.setText("Username");
        usernameLabel.setBounds(30, 220, 300, 40);
        usernameLabel.setFont(font);


        passwordLabel.setText("Password");
        passwordLabel.setBounds(30, 285, 300, 40);
        passwordLabel.setFont(font);

        errorMessage.setText("Invalid ID number ! ");
        errorMessage.setBounds(150, 387, 220,40);
        errorMessage.setForeground(new Color(0xFA1313));
        errorMessage.setFont(new Font("SANS_SERIF",Font.CENTER_BASELINE,13));
        errorMessage.setVisible(false);

        errorMessage3.setText("Sign UP Failed Please Try Again! ");
        errorMessage3.setBounds(150, 387, 220,40);
        errorMessage3.setForeground(new Color(0xFA1313));
        errorMessage3.setFont(new Font("SANS_SERIF",Font.CENTER_BASELINE,13));
        errorMessage3.setVisible(false);

        errorMessage2.setText("Please fill all the input area ! ");
        errorMessage2.setBounds(150, 387, 220, 40);
        errorMessage2.setForeground(new Color(0xFA1313));
        errorMessage2.setFont(new Font("SANS_SERIF", Font.CENTER_BASELINE, 13));
        errorMessage2.setVisible(false);

        successMessagelabel.setText("Signed up successfully ");
        successMessagelabel.setBounds(320, 25, 220, 40);
        successMessagelabel.setForeground(new Color(0xF740FF06, true));
        successMessagelabel.setFont(new Font("SANS_SERIF", Font.CENTER_BASELINE, 16));
        successMessagelabel.setVisible(false);

        rightPanel.setBackground(new Color(0xE1DEDE));
        rightPanel.setLayout(null);

        idNumber.setBounds(30, 190, 270, 30);
        idNumber.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 152), 1));
        idNumber.addKeyListener(key);

        username.setBounds(30, 255, 270, 30);
        username.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 152), 1));
        username.addKeyListener(key);

        password.setBounds(30, 320, 270, 30);
        password.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 152), 1));
        password.addKeyListener(key);

        signinBtn.setBounds(30, 385, 100, 30);
        signinBtn.setText("Sign up");
        signinBtn.setBackground(new Color(1, 24, 30));
        signinBtn.setForeground(new Color(0xE1DEDE));
        signinBtn.setFocusable(false);
        signinBtn.setFont(font);
        signinBtn.addActionListener(h2);

        rightPanel.add(header);
        rightPanel.add(usernameLabel);
        rightPanel.add(passwordLabel);
        rightPanel.add(username);
        rightPanel.add(password);
        rightPanel.add(idNumber);
        rightPanel.add(idNumberLable);
        rightPanel.add(signinBtn);
        rightPanel.add(errorMessage);
        rightPanel.add(errorMessage2);
        rightPanel.add(successMessagelabel);

        add(leftPanel);
        add(rightPanel);
        //setVisible(false);

    }
    class handler extends ComponentAdapter {


        @Override
        public void componentResized(ComponentEvent e) {

            int height = getHeight();
            int width = getWidth();

            lPanelwidth = (int) (width * 0.3375);
            panelheight += height - panelheight;
            rPanelwidth = width - lPanelwidth;
            int w = (int) (rPanelwidth * (0.5510));
            int h = (int) (panelheight * (0.0508));
            int x = (int) (rPanelwidth * (0.0508));
            int y = (int) (panelheight * (255.0 / 520));

            leftPanel.setBounds(0, 20, lPanelwidth, panelheight);
            rightPanel.setBounds(lPanelwidth, 20, rPanelwidth, panelheight);

            header.setBounds(x, 105, 390, 40);
            idNumberLable.setBounds(x, 155, 370, 40);
            usernameLabel.setBounds(x, 220, 300, 40);
            passwordLabel.setBounds(x, 285, 300, 40);
            errorMessage.setBounds(x + 130, 387, 220,40);
            errorMessage2.setBounds(x + 130, 387, 220, 40);

            idNumber.setBounds(x, 190, w, h);
            username.setBounds(x, 255, w, h);
            password.setBounds(x, 320, w, h);

            signinBtn.setBounds(x, 385, 100, 30);
            errorMessage.setBounds(x+ 130, 387, 220,40);


        }

    }
    class handler2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signinBtn)
            {
                try {
                    Map<String,String> queryResult = checkID(conn, Integer.parseInt(idNumber.getText()));
                    if(!queryResult.isEmpty())
                    {
                          if(updateUserTable(conn,queryResult))
                              setVisible(false);
                          else {
                              errorMessage3.setVisible(true);
                          }
                    }
                    else if (username.getText().length() == 0 || password.getText().length() == 0) {
                        errorMessage.setVisible(false);
                        errorMessage2.setVisible(true);
                        errorMessage3.setVisible(false);
                    }
                    else {
                        errorMessage.setVisible(true);
                        errorMessage2.setVisible(false);
                        errorMessage3.setVisible(false);
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


                }

                }
            }

    class keyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if( e.getKeyCode() == KeyEvent.VK_ENTER) {
                signinBtn.doClick();
            }
        }
    }
    public Map<String,String> checkID(Connection conn ,int id) throws SQLException {

        PreparedStatement p = conn.prepareStatement("SELECT * FROM DepartmentInfo where DepartmentInfo.ID = ?");
        p.setInt(1,id);

        Map<String, String> result = new HashMap<>();;
        try {
            ResultSet R = p.executeQuery();

            if (R != null) {
                R.next();
                {
                    result.put("id", String.valueOf(R.getInt("ID")));
                    result.put("name", R.getString("Name"));
                    result.put("age", String.valueOf(R.getInt("Age")));
                    result.put("role", R.getString("Role"));
                }
            }

            R.close();
        }
        catch (SQLException E)
        {
            errorMessage.setVisible(true);
            errorMessage2.setVisible(false);
        }
        return result;
    }
 public boolean updateUserTable(Connection conn,Map<String,String> queryResult)
 {
     try {
         PreparedStatement p = conn.prepareStatement("INSERT INTO User values(?,?,?,?,?,?)");

         p.setInt(1,Integer.parseInt(queryResult.get("id")));
         p.setString(2,queryResult.get("name"));
         p.setInt(3,Integer.parseInt(queryResult.get("age")));
         p.setString(6,queryResult.get("role"));
         p.setString(4,username.getText());
         p.setString(5, new String(password.getPassword()));

         p.executeUpdate();
         p.close();
         return true;
     } catch (SQLException e) {
         return false;
     }
 }
}
