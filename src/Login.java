import com.sun.source.tree.CatchTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Login extends JFrame {
    Connection conn = null;
    Doctor doc = null;
    Pharmacist pharmacist = null;
    Receptionist receptionist = null;
    private JPanel leftPanel = new JPanel();
    private JPanel rightPanel = new JPanel();


    JTextField usernameTextField = new JTextField();
    JPasswordField passwordTextField = new JPasswordField();
    JButton loginBtn = new JButton();
    JButton signinBtn = new JButton();

    JLabel header = new JLabel();
    JLabel usernameLabel = new JLabel();
    JLabel passwordLabel = new JLabel();
    JLabel errorMessage = new JLabel();
    JLabel errorMessage2 = new JLabel();
    JLabel successMessagelabel = new JLabel();

    JLabel question = new JLabel();
    Font font = new Font("MONOSPACED", Font.BOLD, 14);
    Font font2 = new Font("MONOSPACED", Font.BOLD + Font.ROMAN_BASELINE, 16);
    Font font3 = new Font("SANS_SERIF", Font.BOLD, 12);
    ImageIcon hawassaLogo = new ImageIcon(getClass().getResource("Hawassa1.png"));
    private int lPanelwidth = 270;
    private int rPanelwidth = 490;
    private int panelheight = 520;

    Login(Connection conn) {

        this.conn = conn;
        doc = new Doctor(conn);
        pharmacist = new Pharmacist(conn);
        receptionist = new Receptionist(conn);

        setTitle("IOT Clinic System");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLayout(null);

        setIconImage(hawassaLogo.getImage());
        setBackground(new Color(0x000000));
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension dim = tool.getScreenSize();

        ScreenResizeListener h = new ScreenResizeListener();
        ButtonListener h2 = new ButtonListener();
        keyListener t = new keyListener();

        addComponentListener(h);

        leftPanel.setBounds(0, 20, lPanelwidth, panelheight);
        rightPanel.setBounds(lPanelwidth, 20, rPanelwidth, panelheight);


        getContentPane().setBackground(new Color(0x000C0E));
        leftPanel.setBackground(new Color(1, 24, 30));
        leftPanel.setLayout(null);


        rightPanel.setBackground(new Color(0xE1DEDE));
        rightPanel.setLayout(null);

        header.setText("LOG INTO IOT CAMPUS CLINIC SYSTEM ");
        header.setBounds(30, 105, 340, 40);
        header.setFont(font2);

        usernameLabel.setText("Username");
        usernameLabel.setBounds(30, 155, 300, 40);
        usernameLabel.setFont(font);

        passwordLabel.setText("Password");
        passwordLabel.setBounds(30, 220, 300, 40);
        passwordLabel.setFont(font);

        question.setText("Don't you have an account ? ");
        question.setBounds(30, 380, 190, 40);
        question.setFont(font3);

        errorMessage.setText("Incorrect username or password ! ");
        errorMessage.setBounds(150, 321, 220, 40);
        errorMessage.setForeground(new Color(0xFA1313));
        errorMessage.setFont(new Font("SANS_SERIF", Font.CENTER_BASELINE, 13));
        errorMessage.setVisible(false);

       errorMessage2.setText("Please fill all the input area ! ");
       errorMessage2.setBounds(150, 321, 220, 40);
       errorMessage2.setForeground(new Color(0xFA1313));
       errorMessage2.setFont(new Font("SANS_SERIF", Font.CENTER_BASELINE, 13));
       errorMessage2.setVisible(false);



        usernameTextField.setBounds(30, 190, 270, 30);
        usernameTextField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 152), 1));

        passwordTextField.setBounds(30, 255, 270, 30);
        passwordTextField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 152), 1));


        loginBtn.setBounds(30, 320, 100, 30);
        loginBtn.setText("Login");
        loginBtn.setBackground(new Color(1, 24, 30));
        loginBtn.setForeground(new Color(0xE1DEDE));
        loginBtn.setFocusable(false);
        loginBtn.setFont(font);

        loginBtn.addActionListener(h2);
        loginBtn.addKeyListener(t);
        usernameTextField.addKeyListener(t);
        passwordTextField.addKeyListener(t);

        signinBtn.setBounds(222, 385, 100, 30);
        signinBtn.setText("Sign Up");
        signinBtn.setBackground(new Color(0xE1DEDE));
        signinBtn.setForeground(new Color(1, 24, 30));
        signinBtn.setFocusable(false);
        signinBtn.setFont(font);
        signinBtn.setBorder(BorderFactory.createLineBorder(new Color(0xE1DEDE), 2));
        signinBtn.addActionListener(h2);


        rightPanel.add(header);
        rightPanel.add(usernameLabel);
        rightPanel.add(passwordLabel);
        rightPanel.add(question);
        rightPanel.add(usernameTextField);
        rightPanel.add(passwordTextField);
        rightPanel.add(loginBtn);
        rightPanel.add(signinBtn);
        rightPanel.add(errorMessage);
        rightPanel.add(errorMessage2);
        rightPanel.add(successMessagelabel);

        add(leftPanel);
        add(rightPanel);
        setVisible(true);
    }

   private class ScreenResizeListener extends ComponentAdapter {
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

            leftPanel.setBounds(0, 20, lPanelwidth, panelheight);

            rightPanel.setBounds(lPanelwidth, 20, rPanelwidth, panelheight);

            usernameTextField.setBounds(x, 190, w, h);
            passwordTextField.setBounds(x, 255, w, h);
            header.setBounds(x, 105, 340, 40);
            usernameLabel.setBounds(x, 155, 300, 40);
            passwordLabel.setBounds(x, 220, 300, 40);
            question.setBounds(x, 380, 190, 40);
            loginBtn.setBounds(x, 320, 100, 30);
            errorMessage.setBounds(x + 120, 321, 220, 40);
        }
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginBtn)
            {
                try {
                    Map<String,String> queryResult = ValidateUserPassword(conn,usernameTextField.getText(),new String(passwordTextField.getPassword()));

                    if(!queryResult.isEmpty() )
                    {
                           if(queryResult.get("role").equals("Doctor")) {
                               {
                                   doc.setName(queryResult.get("name"));
                                   doc.setUsername(queryResult.get("username"));
                                   doc.setPassword(queryResult.get("password"));
                                   doc.setId(Integer.parseInt(queryResult.get("id")));
                                   doc.setVisible(true);
                                   setVisible(false);
                               }
                           }
                           else if(queryResult.get("role").equals("Pharmacist")) {
                               {
                                   pharmacist.setName(queryResult.get("name"));
                                   pharmacist.setUsername(queryResult.get("username"));
                                   pharmacist.setPassword(queryResult.get("password"));
                                   pharmacist.setId(Integer.parseInt(queryResult.get("id")));
                                   pharmacist.setVisible(true);
                                   setVisible(false);
                               }
                           }
                           else if(queryResult.get("role").equals("Receptionist")) {
                               {
//                                  receptionist.setName(queryResult.get("name"));
//                                  receptionist.setUsername(queryResult.get("username"));
//                                  receptionist.setPassword(queryResult.get("password"));
//                                  receptionist.setId(Integer.parseInt(queryResult.get("id")));
                                   receptionist.setVisible(true);
                                   setVisible(false);
                               }
                           }
                    }
                     else  if (usernameTextField.getText().length() == 0 || passwordTextField.getText().length() == 0)
                    {
                        errorMessage.setVisible(false);
                        errorMessage2.setVisible(true);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (e.getSource() == signinBtn) {
                Dimension dim = new Dimension(getSize());
                setVisible(false);
                Signup f = new Signup(dim.width,dim.height,conn);
                f.setVisible(true);

            }

        }
    }

    class keyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if( e.getKeyCode() == KeyEvent.VK_ENTER) {
                loginBtn.doClick();
            }
        }
        }
        public Map<String,String> ValidateUserPassword(Connection conn , String username , String password) throws SQLException {

            PreparedStatement p = conn.prepareStatement("SELECT * FROM User where Username = ? AND Password = ?");
            p.setString(1,username);
            p.setString(2,password);

            Map<String, String> result = new HashMap<>();
            try {
                ResultSet R = p.executeQuery();
                if (R != null) {
                    R.next();
                    {
                        result.put("id", String.valueOf(R.getInt("ID")));
                        result.put("username", R.getString("Username"));
                        result.put("name", R.getString("Name"));
                        result.put("password", R.getString("Password"));
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
    }


