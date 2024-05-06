import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Pharmacist extends JFrame {
    Connection conn = null;
    private int id;
    private String name;
    private String username;
    private String password;

    private JPanel leftPanel = new JPanel();
    private static int lPanelwidth = 200;
    private static int rPanelwidth = 600;
    private static int panelheight = 600;

    JLabel userLabel;

    JLabel k = new JLabel();
    private ImageIcon homebg = new ImageIcon(getClass().getResource("dochomepage.png"));
    private ImageIcon homeScreenImg = new ImageIcon(getClass().getResource("homescreen.png"));
    private ImageIcon userImg = new ImageIcon(getClass().getResource("user.png"));
    private ImageIcon medicationImg = new ImageIcon(getClass().getResource("medication.png"));
    private ImageIcon prescriptionImg = new ImageIcon(getClass().getResource("prescription.png"));
    private ImageIcon manageStockImg = new ImageIcon(getClass().getResource("referral.png"));
    private ImageIcon settingImg = new ImageIcon(getClass().getResource("setting.png"));
    private ImageIcon logoutImg = new ImageIcon(getClass().getResource("logout.png"));
    Image backgroundImage = homebg.getImage();

    MedicationInfo  medicationInfo = new MedicationInfo();
    Prescription prescription = new Prescription();
    ManageStock manageStock = new ManageStock();
    SettingPasswordPanel pharmacistPasswordPanel = new SettingPasswordPanel();
    SettingLpanel pharmacistSettingLpanel = new SettingLpanel();
    SettingEmailPanel pharmacistEmailPanel = new SettingEmailPanel();

    private JButton homeBtn = new JButton();
    private JButton medicationBtn = new JButton();
    private JButton manageStockBtn= new JButton();
    private JButton prescriptionbtn = new JButton();
    private JButton stockReportingBtn = new JButton();
    private JButton settingBtn = new JButton();
    private JButton logoutBtn = new JButton();

    private static int buttonxDir = 3;
    private static int buttonWidth = 170;
    private static int buttonHeight = 35;

    public Pharmacist(Connection conn) {

        this.conn = conn;

        setSize(800, 600);
        setLayout(null);
        setMinimumSize(new Dimension(800, 600));

        leftPanel.setBackground(new Color(1, 24, 30));
        leftPanel.setBounds(0, 0, lPanelwidth, panelheight);
        leftPanel.setLayout(null);

        userLabel = new JLabel(" user");
        userLabel.setForeground(Doctor.labelForegroundColor);
        userLabel.setBounds((leftPanel.getWidth() / 2) - 50, 15, 75, 65);
        userLabel.setIcon(userImg);
        userLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        userLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        leftPanel.add(homeBtn = Doctor.buttonInit(homeScreenImg, "Home        ", buttonxDir,120,buttonWidth,buttonHeight));
        leftPanel.add(medicationBtn = Doctor.buttonInit(medicationImg, "Medication  ", buttonxDir,170,buttonWidth,buttonHeight));
        leftPanel.add(prescriptionbtn = Doctor.buttonInit(prescriptionImg, "Prescription", buttonxDir,220,buttonWidth,buttonHeight));
        leftPanel.add(manageStockBtn = Doctor.buttonInit(manageStockImg, "Manage Stock", buttonxDir,270,buttonWidth,buttonHeight));
        leftPanel.add(settingBtn = Doctor.buttonInit(settingImg, "Setting     ", buttonxDir,370,buttonWidth,buttonHeight));
        leftPanel.add(logoutBtn = Doctor.buttonInit(logoutImg, "Log Out     ", buttonxDir,420,buttonWidth,buttonHeight));
        leftPanel.add(userLabel);

        LeftPanelButtonAction leftPanelButtonAction = new LeftPanelButtonAction();

        homeBtn.addActionListener(leftPanelButtonAction);
        medicationBtn.addActionListener(leftPanelButtonAction);
        prescriptionbtn.addActionListener(leftPanelButtonAction);
        manageStockBtn.addActionListener(leftPanelButtonAction);
        settingBtn.addActionListener(leftPanelButtonAction);
        logoutBtn.addActionListener(leftPanelButtonAction);

        ScreenResizeListener screenResizeListener = new ScreenResizeListener();

        add(leftPanel);
        add(medicationInfo);
        add(prescription);
        add(manageStock);
        add(pharmacistSettingLpanel);
        add(pharmacistPasswordPanel);
        add(pharmacistEmailPanel);

        addComponentListener(screenResizeListener);
        setVisible(false);
    }

    private class MedicationInfo extends JPanel {
        private JLabel searchLable = new JLabel();
        private JButton searchBtn = new JButton();
        private JTextField searchTextField = new JTextField();
        private JTextArea queryResultTextArea = new JTextArea();
        private JLabel searchErrorMessage = new JLabel();
        MedicationInfo() {
            setLayout(null);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);

            ImageIcon searchImg = new ImageIcon(getClass().getResource("search.png"));
            searchTextField.setBounds(70, 110, 300, 35);

            searchBtn = Doctor.buttonInit(searchImg,"Search",380,110,120,35);
            searchLable = Doctor.labelInit(null,"Please Enter Medication Name",70,80,300,20);

            searchErrorMessage = Doctor.labelInit(null, "Medication Not Found !!", 40, 140, 300, 35);
            searchErrorMessage.setVisible(false);

            queryResultTextArea.setBounds(40, 160, 500, 350);
            queryResultTextArea.setFont(new Font("MONOSPACED",Font.PLAIN,18));
            queryResultTextArea.setEditable(false);
            queryResultTextArea.setVisible(false);

            MedicationInfoButton medicationInfoButton = new MedicationInfoButton();
            searchBtn.addActionListener(medicationInfoButton);

            add(queryResultTextArea);
            add(searchErrorMessage);
            add(searchBtn);
            add(searchLable);
            add(searchTextField);

            setBackground(Doctor.backgroundColor);
            setVisible(false);

        }
        class MedicationInfoButton implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == searchBtn) {
                    try {
                        Map<String, String> queryResult = retrievPatientData(Integer.parseInt(searchTextField.getText()));

                        if (!queryResult.isEmpty()) {

                            queryResultTextArea.setText("Medication Name : " + queryResult.get("name") + "\n" +
                                    "Medication Quantity : " + queryResult.get("quantity") +  "\n" +
                                    "Medication Expiration Date : " + queryResult.get("expirationDate"));
                            queryResultTextArea.setVisible(true);
                            searchErrorMessage.setVisible(false);
                        } else if (searchTextField.getText().length() == 0) {
                            searchErrorMessage.setVisible(true);
                            queryResultTextArea.setVisible(false);
                        }
                    } catch (SQLException ex) {
                        searchErrorMessage.setVisible(true);
                    }
                }
            }
        }

        Map<String, String> retrievPatientData(int id) throws SQLException {
            PreparedStatement p = conn.prepareStatement("SELECT MedicationName, ExpirationDate, Quantity \n" +
                    "FROM Medication \n" +
                    "WHERE MedicationID = ?");
            p.setInt(1, id);
            Map<String, String> queryResult = new HashMap<>();
            try {
                ResultSet R = p.executeQuery();
                if (R != null) {
                    R.next();
                    {
                        queryResult.put("name", R.getString("MedicationName"));
                        queryResult.put("quantity", String.valueOf(R.getInt("Quantity")));
                        queryResult.put("expirationDate", R.getString("ExpirationDate"));
                    }
                    R.close();
                }
            } catch (SQLException e) {
                queryResultTextArea.setVisible(false);
                searchErrorMessage.setVisible(true);
                return queryResult;
            }
            return queryResult;
        }
    }
    private class Prescription extends JPanel {
        JLabel medicationLabel = new JLabel();
        JLabel quantityLabel = new JLabel();
        JTextField medicationTextField = new JTextField();
        JTextField quantityTextField = new JTextField();
        public JButton prescribeBtn = new JButton();

        Prescription() {
            setLayout(null);
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            setBackground(Doctor.backgroundColor);

            prescribeBtn = Doctor.buttonInit(null,"Prescribe",335,243,163,35);

            medicationLabel = Doctor.labelInit(null,"Please Enter Medication Name",70,80,300,20);
            quantityLabel = Doctor.labelInit(null,"Please Enter Quantity of Medication",70,153,300,20);

            medicationTextField.setBounds(70, 110, 300, 35);
            quantityTextField.setBounds(70, 183, 300, 35);
            PrescriptionButtonListner prescriptionButtonListner = new PrescriptionButtonListner();

            prescribeBtn.addActionListener(prescriptionButtonListner);
            add(prescribeBtn);
            add(medicationTextField);
            add(medicationLabel);
            add(quantityLabel);
            add(quantityTextField);
            setVisible(false);

        }
        private class PrescriptionButtonListner implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == prescribeBtn) {
                    updateMedicationTable();
                }
            }
        }
        void updateMedicationTable()
        {
            try {
                PreparedStatement p = conn.prepareStatement("select Quantity from Medication where MedicationName = ?");
                p.setString(1,medicationTextField.getText());
                ResultSet R = p.executeQuery();
                int quantity = R.getInt("Quantity");
                int quntityToDitact = Integer.parseInt(quantityTextField.getText());

                if (quantity >= quntityToDitact){
                        try {
                            PreparedStatement p1 = conn.prepareStatement("Update Medication set Quantity = ? where MedicationName= ?");
                            quantity = quantity - quntityToDitact;

                            p1.setInt(1, quantity);
                            p1.setString(2, medicationTextField.getText());

                            p1.executeUpdate();
                            p1.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        System.out.println("nbv jvhcccccccccccccc");
                    }
                } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
    }
    }
 private class ManageStock extends JPanel {
     JLabel headerMessage1 = new JLabel();

     JLabel addMedicationNameLabel = new JLabel();
     JLabel medicationQuantityLabel = new JLabel();
     JLabel expirationDateLabel = new JLabel();
     JTextField medicationNameTextField = new JTextField();
     JTextField medicationQuantityTextField = new JTextField();
     JTextField expirationDateTextField = new JTextField();
     public JButton addMedicationBtn = new JButton();


     ManageStock() {
         setLayout(null);
         setSize(rPanelwidth, panelheight);
         setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
         setBackground(Doctor.backgroundColor);

         headerMessage1 = Doctor.labelInit(null, "TO ADD NEW MEDICATION TO THE STOCK", 50, 40, 300, 20);
         headerMessage1.setForeground(new Color(0x444444));

         addMedicationNameLabel = Doctor.labelInit(null, "Please Enter Medication Name", 70, 80, 300, 20);
         medicationQuantityLabel = Doctor.labelInit(null, "Please Enter Quantity of Medication", 70, 150, 300, 20);
         expirationDateLabel = Doctor.labelInit(null, "Please Enter Exp Date of The Medication", 70, 220, 380, 20);


         medicationNameTextField.setBounds(70, 110, 300, 35);
         medicationQuantityTextField.setBounds(70, 180, 300, 35);
         expirationDateTextField.setBounds(70, 250, 300, 35);


         addMedicationBtn = Doctor.buttonInit(null, "Add", 335, 310, 163, 35);
         ManageStockButtonListner manageStockButtonListner = new ManageStockButtonListner();
         addMedicationBtn.addActionListener(manageStockButtonListner);

         add(headerMessage1);
         add(addMedicationNameLabel);
         add(medicationQuantityLabel);
         add(expirationDateLabel);
         add(medicationNameTextField);
         add(medicationQuantityTextField);
         add(expirationDateTextField);
         add(addMedicationBtn);


         setVisible(false);

     }

     private class ManageStockButtonListner implements ActionListener {

         @Override
         public void actionPerformed(ActionEvent e) {
             if (e.getSource() == addMedicationBtn) {
                 updateMedicationTable();
             }
         }
     }

     public void updateMedicationTable() {
         try {
             PreparedStatement p = conn.prepareStatement("INSERT INTO Medication (MedicationName,Quantity,ExpirationDate) values(?,?,?)");

             p.setString(1, medicationNameTextField.getText());
             p.setInt(2, Integer.parseInt(medicationQuantityTextField.getText()));
             p.setString(3, expirationDateTextField.getText());

             p.executeUpdate();
             p.close();

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
     }
 }
    class SettingLpanel extends JPanel {
        JButton changePasswordBtn = new JButton();
        JButton addEmailAddressBtn = new JButton();

        JLabel accountLable = new JLabel();

        public SettingLpanel() {
            setLayout(null);
            setBounds(lPanelwidth + 20, 20, (rPanelwidth / 2) - 60, panelheight - 40);
            setBackground(Doctor.backgroundColor);

            accountLable = Doctor.labelInit(null,"Account Management ",20, 70, (rPanelwidth / 2) - 45, 35);
            accountLable.setForeground(new Color(0x444444));



            SettingButtonActions settingButtonActions = new SettingButtonActions();

            changePasswordBtn = Doctor.buttonInit(null,"Change Password",10, 110, (rPanelwidth / 2) - 55, 35);
            changePasswordBtn.addActionListener(settingButtonActions);

            addEmailAddressBtn = Doctor.buttonInit(null,"Add Email Address",10, 160, (rPanelwidth / 2) - 55, 35);
            addEmailAddressBtn.addActionListener(settingButtonActions);



            add(accountLable);
            add(addEmailAddressBtn);
            add(changePasswordBtn);


            setVisible(false);

        }
        class SettingButtonActions implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == changePasswordBtn) {
                    pharmacistPasswordPanel.setVisible(true);
                    pharmacistEmailPanel.setVisible(false);
                }
                else if (e.getSource() == addEmailAddressBtn) {
                    pharmacistPasswordPanel.setVisible(false);
                    pharmacistEmailPanel.setVisible(true);
                }

            }
        }
    }
    class SettingPasswordPanel extends JPanel{
        JLabel oldPasswordLable = new JLabel();
        JLabel newPasswordLable = new JLabel();
        JLabel confirmPasswordLable = new JLabel();

        JButton saveBtn = new JButton();
        JPasswordField oldPasswordTextField = new JPasswordField();
        JPasswordField newPasswordTextField = new JPasswordField();
        JPasswordField confirmPasswordTextField = new JPasswordField();

        public SettingPasswordPanel(){
            setLayout(null);
            setBounds(lPanelwidth + (rPanelwidth / 2) - 10, 20, (rPanelwidth / 2) - 30, panelheight - 40);
            setBackground(Doctor.backgroundColor);

            oldPasswordLable  = Doctor.labelInit(null,"Enter Your Old Password",(int)(rPanelwidth * 0.05), (int)(panelheight * 0.1334), 300, 20);
            newPasswordLable = Doctor.labelInit(null,"Enter New Password",(int)(rPanelwidth * 0.05), (int)(panelheight * 0.25), 300, 20);
            confirmPasswordLable = Doctor.labelInit(null,"Confirm Your New Password",(int)(rPanelwidth * 0.05), (int)(panelheight * 0.3667), 300, 20);

            oldPasswordTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.1833), (int)(rPanelwidth * 0.3334),  (int)(panelheight * 0.0583));
            newPasswordTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.3), (int)(rPanelwidth * 0.3334),  (int)(panelheight * 0.0583));
            confirmPasswordTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.4166), (int)(rPanelwidth * 0.3334), (int)(panelheight * 0.0583));

            saveBtn = Doctor.buttonInit(null,"Save",(int)(rPanelwidth * 0.3166), (int)(panelheight * 0.5), 90,   (int)(panelheight * 0.0583));

            PasswordButtonActions passwordButtonActions = new PasswordButtonActions();
            saveBtn.addActionListener(passwordButtonActions);

            add(oldPasswordLable);
            add(newPasswordLable);
            add(confirmPasswordLable);
            add(oldPasswordTextField);
            add(newPasswordTextField);
            add(confirmPasswordTextField);
            add(saveBtn);

            setVisible(false);
        }
        class PasswordButtonActions implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == saveBtn) {
                    changePassword();
                }
            }
        }
        void changePassword() {
            String tempOldPassword = new String(oldPasswordTextField.getPassword());
            String tempNewPassword = new String(newPasswordTextField.getPassword());
            String tempConfirmPassword = new String(confirmPasswordTextField.getPassword());

            if (password.equals(tempOldPassword)){
                if (tempNewPassword.equals(tempConfirmPassword)) {
                    try {
                        PreparedStatement p = conn.prepareStatement("Update User set Password = ? where User.ID = ?");
                        p.setString(1, new String(newPasswordTextField.getPassword()));
                        p.setInt(2, id);

                        p.executeUpdate();
                        p.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    System.out.println("nbv jvhcccccccccccccc");
                }
            }
        }
    }

    public class SettingEmailPanel extends JPanel{
        JLabel emailLable = new JLabel();
        JButton addEmailBtn = new JButton();
        JTextField emailTextField = new JTextField();

        public SettingEmailPanel(){
            setLayout(null);
            setBounds(lPanelwidth + (rPanelwidth / 2) - 10, 20, (rPanelwidth / 2) - 30, panelheight - 40);
            setBackground(Doctor.backgroundColor);

            emailLable = Doctor.labelInit(null,"Add New Email Account",(int)(rPanelwidth * 0.05), (int)(panelheight * 0.25), 300, 20);

            emailTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.3), (int)(rPanelwidth * 0.3334),  (int)(panelheight * 0.0583));

            addEmailBtn = Doctor.buttonInit(null,"Add",(int)(rPanelwidth * 0.3066) ,(int)(panelheight * 0.4166) ,80 ,(int)(panelheight * 0.0583));
            SettingEmailButtonActions settingEmailButtonActions = new SettingEmailButtonActions();
            addEmailBtn.addActionListener(settingEmailButtonActions);

            add(emailLable);
            add(emailTextField);
            add(addEmailBtn);

            setVisible(false);
        }
        class SettingEmailButtonActions implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addEmailBtn) {
                    addEmail();
                }
            }
        }
        void addEmail() {
            try {
                PreparedStatement p = conn.prepareStatement("Update User set EmailAddress = ? where User.ID = ?");
                p.setString(1, new String(emailTextField.getText()));
                p.setInt(2, id);

                p.executeUpdate();
                p.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
        private class LeftPanelButtonAction implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == homeBtn) {
                    panelsVisibility();
                } else if (e.getSource() == medicationBtn) {
                    panelsVisibility();
                    medicationInfo.setVisible(true);
                } else if (e.getSource() == prescriptionbtn) {
                    panelsVisibility();
                    prescription.setVisible(true);
                } else if (e.getSource() == manageStockBtn) {
                    panelsVisibility();
                    manageStock.setVisible(true);
                } else if (e.getSource() == stockReportingBtn) {
                    panelsVisibility();
                } else if (e.getSource() == settingBtn) {
                    panelsVisibility();
                    pharmacistSettingLpanel.setVisible(true);
                    pharmacistPasswordPanel.setVisible(true);
                } else if (e.getSource() == logoutBtn) {
                    panelsVisibility();
                    setVisible(false);
                }

            }
        }

        private class ScreenResizeListener extends ComponentAdapter {
            @Override
            public void componentResized(ComponentEvent e) {
                int height = getHeight();
                int width = getWidth();

                lPanelwidth = (int)(width * 0.25);
                panelheight += height - panelheight;
                rPanelwidth = width - lPanelwidth;

                buttonxDir = (int)(lPanelwidth * 0.01);
                buttonWidth = (int)(lPanelwidth * 0.75);
                buttonHeight = (int)(panelheight * 0.0584);

                leftPanel.setBounds(0,0,lPanelwidth,panelheight);

                homeBtn.setBounds(buttonxDir,(int)(panelheight * 0.2),buttonWidth,buttonHeight);
                medicationBtn.setBounds(buttonxDir,(int)(panelheight * 0.284),buttonWidth,buttonHeight);
                prescriptionbtn.setBounds(buttonxDir,(int)(panelheight * 0.367),buttonWidth,buttonHeight);
                manageStockBtn.setBounds(buttonxDir,(int)(panelheight * 0.45),buttonWidth,buttonHeight);
                stockReportingBtn.setBounds(buttonxDir,(int)(panelheight * 0.534),buttonWidth,buttonHeight);
                settingBtn.setBounds(buttonxDir,(int)(panelheight * 0.617),buttonWidth,buttonHeight);
                logoutBtn.setBounds(buttonxDir,(int)(panelheight * 0.7),buttonWidth,buttonHeight);

                userLabel.setBounds((leftPanel.getWidth() / 2) - (userLabel.getWidth() / 2),(int)(panelheight * 0.025),150,(int)(panelheight * 0.1084));
                medicationInfo.setBounds(lPanelwidth,0,rPanelwidth,panelheight);
                prescription.setBounds(lPanelwidth,0,rPanelwidth,panelheight);
                manageStock.setBounds(lPanelwidth,0,rPanelwidth,panelheight);
                pharmacistSettingLpanel.setBounds((int)(lPanelwidth) + (int)(rPanelwidth * 0.0167), (int)(panelheight * 0.034), (int)(rPanelwidth * 0.45), (int)(panelheight * 0.93) - (int)(panelheight * 0.034));
                pharmacistPasswordPanel.setBounds((int)(lPanelwidth + (rPanelwidth / 2)) - (int)(rPanelwidth * 0.025), (int)(panelheight * 0.034), (int)(rPanelwidth * 0.49), (int)(panelheight * 0.93) - (int)(panelheight * 0.034));
                pharmacistEmailPanel.setBounds(lPanelwidth + (rPanelwidth / 2) - 10, 20, (rPanelwidth / 2) - 30, panelheight - 40);

                medicationInfo.searchTextField.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.1834), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));
                medicationInfo.searchLable.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.134), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));
                medicationInfo.searchBtn.setBounds((int)(rPanelwidth * 0.6334),(int)(panelheight * 0.1834),(int)(rPanelwidth * 0.2),(int)(panelheight * 0.0584));

                manageStock.headerMessage1.setBounds((int)(rPanelwidth * 0.0834), (int)(panelheight * 0.0667), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.034));
                manageStock.addMedicationNameLabel.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.134), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.034));
                manageStock.medicationNameTextField.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.1834), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));
                manageStock.medicationQuantityLabel.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.255), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.034));
                manageStock.medicationQuantityTextField.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.305), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));

                manageStock.expirationDateLabel.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.3667), (int)(rPanelwidth * 0.6333), (int)(panelheight * 0.034));
                manageStock.expirationDateTextField.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.4167), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));


                manageStock.addMedicationBtn.setBounds((int)(rPanelwidth * 0.5583), (int)(panelheight * 0.5166), (int)(rPanelwidth * 0.2716), (int)(panelheight * 0.0584));

                prescription.prescribeBtn.setBounds((int)(rPanelwidth * 0.5584), (int)(panelheight * 0.405), (int)(rPanelwidth * 0.2717), (int)(panelheight * 0.0584));
                prescription.medicationTextField.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.1834), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));
                prescription.quantityTextField.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.305), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.0584));
                prescription.quantityLabel.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.255), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.034));
                prescription.medicationLabel.setBounds((int)(rPanelwidth * 0.1167), (int)(panelheight * 0.134), (int)(rPanelwidth * 0.5), (int)(panelheight * 0.034));



                pharmacistSettingLpanel.accountLable.setBounds((int)(rPanelwidth * 0.0167),(int)(panelheight * 0.1166),(rPanelwidth / 2) -(int)(rPanelwidth * 0.075) , (int)(panelheight * 0.0583));
                pharmacistSettingLpanel.changePasswordBtn.setBounds((int)(rPanelwidth * 0.0167), (int)(panelheight * 0.1834), (rPanelwidth / 2) -(int)(rPanelwidth * 0.075) , (int)(panelheight * 0.0583));
                pharmacistSettingLpanel.addEmailAddressBtn.setBounds((int)(rPanelwidth * 0.0167),  (int)(panelheight * 0.2667), (rPanelwidth / 2) -(int)(rPanelwidth * 0.075) , (int)(panelheight * 0.0583));
                pharmacistPasswordPanel.oldPasswordLable.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.1334), 300, 20);
                pharmacistPasswordPanel.newPasswordLable.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.25), 300, 20);
                pharmacistPasswordPanel.confirmPasswordLable.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.3667), 300, 20);
                pharmacistPasswordPanel.oldPasswordTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.1833), (int)(rPanelwidth * 0.3334),  (int)(panelheight * 0.0583));
                pharmacistPasswordPanel.newPasswordTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.3), (int)(rPanelwidth * 0.3334),  (int)(panelheight * 0.0583));
                pharmacistPasswordPanel.confirmPasswordTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.4166), (int)(rPanelwidth * 0.3334), (int)(panelheight * 0.0583));
                pharmacistPasswordPanel.saveBtn.setBounds((int)(rPanelwidth * 0.3166), (int)(panelheight * 0.5), 90,   (int)(panelheight * 0.0583));

                pharmacistEmailPanel.addEmailBtn.setBounds((int)(rPanelwidth * 0.3066), (int)(panelheight * 0.4166), 80,   (int)(panelheight * 0.0583));
                pharmacistEmailPanel.emailLable.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.25), 300, 20);
                pharmacistEmailPanel.emailTextField.setBounds((int)(rPanelwidth * 0.05), (int)(panelheight * 0.3), (int)(rPanelwidth * 0.3334),  (int)(panelheight * 0.0583));


            }
        }
    void panelsVisibility()
    {
          medicationInfo.setVisible(false);
          prescription.setVisible(false);
          manageStock.setVisible(false);
          pharmacistSettingLpanel.setVisible(false);
          pharmacistEmailPanel.setVisible(false);
          pharmacistPasswordPanel.setVisible(false);
//          referralTextFieldS.setVisible(false);
//          emailPanel.setVisible(false);
//          passwordPanel.setVisible(false);
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}