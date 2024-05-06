import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Doctor extends JFrame {
    Connection conn = null;
    private int id;
    private String name;
    private String username;
    private String password;


    private JPanel leftPanel = new JPanel();
    private static int lPanelwidth = 200;
    private static int rPanelwidth = 600;
    private static int panelheight = 600;

    private JLabel userLabel;

    private JLabel k = new JLabel();
    ImageIcon homebg = new ImageIcon(getClass().getResource("dochomepage.png"));
    ImageIcon home = new ImageIcon(getClass().getResource("homescreen.png"));
    ImageIcon setting = new ImageIcon(getClass().getResource("setting.png"));
    ImageIcon user = new ImageIcon(getClass().getResource("user.png"));
    ImageIcon patient = new ImageIcon(getClass().getResource("patient.png"));
    ImageIcon prescription = new ImageIcon(getClass().getResource("prescription.png"));
    ImageIcon referral = new ImageIcon(getClass().getResource("referral.png"));

    ImageIcon logout = new ImageIcon(getClass().getResource("logout.png"));
    Image backgroundImage = homebg.getImage();

    private HomeScreen homeS = new HomeScreen(backgroundImage);
    private PatientInfo patientS = new PatientInfo();
    private Prescription prescriptionS = new Prescription();
    private PrescriptionTextField prescriptionTextFieldS = new PrescriptionTextField();
    SettingLpanel settingLpanel = new SettingLpanel();
    SettingPasswordPanel passwordPanel = new SettingPasswordPanel();
    SettingEmailPanel emailPanel = new SettingEmailPanel();
    private Referral referralS = new Referral();
    private ReferralTextField referralTextFieldS = new ReferralTextField();


    private JButton dashboardBtn = new JButton();
    private JButton patientInfobtn = new JButton();
    private JButton prescriptionbtn = new JButton();
    private JButton referralbtn = new JButton();
    private JButton reportbtn = new JButton();
    private JButton settingbtn = new JButton();
    private JButton logoutbtn = new JButton();

    private static int buttonxDir = 2;
    private static int buttonWidth = 150;
    private static int buttonHeight = 35;

    static Color backgroundColor = new Color(0, 255, 255);
    static Color buttonBackgroundColor = new Color(1, 24, 30);
    static Color buttonForegroundColor = new Color(0x12b886);
    static Color labelForegroundColor = new Color(1, 24, 30);


    public Doctor(Connection conn) {

        this.conn = conn;

        setVisible(true);
        setSize(800, 600);
        setLayout(null);
        setMinimumSize(new Dimension(800, 600));

        leftPanel.setBackground(new Color(1, 24, 30));
        leftPanel.setBounds(0, 0, lPanelwidth, panelheight);
        leftPanel.setLayout(null);

        userLabel = new JLabel(name);
        userLabel.setForeground(labelForegroundColor);
        userLabel.setBounds((leftPanel.getWidth() / 2) - 70, 15, 120, 65);
        userLabel.setIcon(user);
        userLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        userLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        leftPanel.add(dashboardBtn = buttonInit(home, "Home        ", buttonxDir, 120, buttonWidth, buttonHeight));
        leftPanel.add(patientInfobtn = buttonInit(patient, "Patient Info", buttonxDir, 170, buttonWidth, buttonHeight));
        leftPanel.add(prescriptionbtn = buttonInit(prescription, "Prescripion", buttonxDir, 220, buttonWidth, buttonHeight));
        leftPanel.add(referralbtn = buttonInit(referral, "Referral    ", buttonxDir, 270, buttonWidth, buttonHeight));
        leftPanel.add(settingbtn = buttonInit(setting, "Setting     ", buttonxDir, 370, buttonWidth, buttonHeight));
        leftPanel.add(logoutbtn = buttonInit(logout, "Log out     ", buttonxDir, 420, buttonWidth, buttonHeight));
        leftPanel.add(userLabel);

        add(userLabel);
        add(leftPanel);
        add(homeS);
        add(patientS);
        add(prescriptionS);
        add(prescriptionTextFieldS);
        add(referralS);
        add(referralTextFieldS);
        add(settingLpanel);
        add(emailPanel);
        add(passwordPanel);

        ScreenResizeListener h = new ScreenResizeListener();
        addComponentListener(h);

        LeftPanelButtonAction leftPanelButtonAction = new LeftPanelButtonAction();
        dashboardBtn.addActionListener(leftPanelButtonAction);
        patientInfobtn.addActionListener(leftPanelButtonAction);
        prescriptionbtn.addActionListener(leftPanelButtonAction);
        referralbtn.addActionListener(leftPanelButtonAction);
        reportbtn.addActionListener(leftPanelButtonAction);
        settingbtn.addActionListener(leftPanelButtonAction);
        logoutbtn.addActionListener(leftPanelButtonAction);

        setVisible(false);
    }

    private class HomeScreen extends JPanel {
        ImageIcon hawassaLogo = new ImageIcon(getClass().getResource("h3.png"));
        private Image backgroundImage;

        public HomeScreen(Image bgImage) {
            setLayout(null);
            k.setIcon(hawassaLogo);
            k.setText("IOT CLINIC MANAGMENT SYSTEM ");
            k.setForeground(new Color(1, 24, 30));
            k.setHorizontalTextPosition(SwingConstants.RIGHT);
            k.setBounds(10, 0, 500, 130);
            k.setFont(new Font("MONOSPACED", Font.BOLD, 20));
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            backgroundImage = bgImage;
            add(k);
            setVisible(true);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

    }

    private class PatientInfo extends JPanel {
        JLabel searchLable = new JLabel();
        JLabel searchErrorMessage = new JLabel();
        JButton searchBtn = new JButton();
        JTextField searchTextField = new JTextField();
        JTextArea queryResultTextArea = new JTextArea();

        PatientInfo() {
            setLayout(null);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);

            ImageIcon searchImg = new ImageIcon(getClass().getResource("search.png"));
            searchTextField.setBounds(40, 110, 300, 35);

            searchBtn = buttonInit(searchImg, "Search", 350, 110, 120, 35);
            searchLable = labelInit(null, "Please Enter Patient ID", 30, 80, 300, 20);
            searchErrorMessage = labelInit(null, "Patient ID Not Found !!", 40, 140, 300, 35);
            searchErrorMessage.setVisible(false);

            queryResultTextArea.setBounds(40, 160, 500, 350);
            queryResultTextArea.setFont(new Font("MONOSPACED",Font.PLAIN,18));
            queryResultTextArea.setEditable(false);
            queryResultTextArea.setVisible(false);

            PatientInfoButton patientInfoButton = new PatientInfoButton();
            searchBtn.addActionListener(patientInfoButton);
            add(searchBtn);
            add(searchLable);
            add(searchTextField);
            add(searchErrorMessage);
            add(queryResultTextArea);

            setBackground(backgroundColor);
            setVisible(false);

        }

        class PatientInfoButton implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == searchBtn) {
                    try {
                        Map<String, String> queryResult = retrievPatientData(conn, Integer.parseInt(searchTextField.getText()));

                        if (!queryResult.isEmpty()) {

                            queryResultTextArea.setText("Patient Name : " + queryResult.get("name") + "\n" +
                                                       "Patient AGE : " + queryResult.get("age") +  "\n" +
                                                       "Patient Gender : " + queryResult.get("gender") +  "\n" +
                                                       "Patient Diagnosis : " + queryResult.get("diagnosis") +  "\n" +
                                                       "Patient Medication Taking : " + queryResult.get("medicationName"));
                            queryResultTextArea.setVisible(true);
                            searchErrorMessage.setVisible(false);
                        } else if (searchTextField.getText().length() == 0) {
                            searchErrorMessage.setVisible(true);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        Map<String, String> retrievPatientData(Connection conn, int id) throws SQLException {
            PreparedStatement p = conn.prepareStatement("SELECT Patient.Name, Patient.Age, Patient.Gender, MedicalRecord.Diagnosis, Prescription.MedicationName \n" +
                    "FROM Patient \n" +
                    "JOIN MedicalRecord ON Patient.ID = MedicalRecord.PatientID \n" +
                    "JOIN Prescription ON Patient.ID = Prescription.PatientID \n" +
                    "WHERE Patient.ID = ?");
            p.setInt(1, id);
            Map<String, String> queryResult = new HashMap<>();
            try {
                ResultSet R = p.executeQuery();
                if (R != null) {
                    R.next();
                    {
                        queryResult.put("name", R.getString("Name"));
                        queryResult.put("age", String.valueOf(R.getInt("Age")));
                        queryResult.put("gender", R.getString("Gender"));
                        queryResult.put("diagnosis", R.getString("Diagnosis"));
                        queryResult.put("medicationName", R.getString("MedicationName"));
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
        JLabel medicationLable = new JLabel();
        JLabel dosageInstructionLable = new JLabel();
        JLabel patientIDLable = new JLabel();
        public JTextField patientIDTextField = new JTextField();
        public JTextField medicationTextField = new JTextField();
        public JTextField dosageTextField = new JTextField();
        public JButton generatePrescriptionBtn = new JButton();

        Prescription() {
            setLayout(null);
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            setBackground(backgroundColor);

            generatePrescriptionBtn = buttonInit(null,"Generate Prescription",335, 323, 163, 35);

            patientIDLable = labelInit(null,"Enter Patient ID",70, 80, 300, 20);
            patientIDTextField.setBounds(70, 110, 300, 35);
            medicationLable = labelInit(null,"Please Enter Medication Type",70, 153, 300, 20);
            medicationTextField.setBounds(70, 183, 300, 35);
            dosageInstructionLable = labelInit(null,"Please Enter Dosage Instruction",70, 223, 300, 20);
            dosageTextField.setBounds(70, 253, 300, 35);


            add(generatePrescriptionBtn);
            add(medicationTextField);
            add(patientIDTextField);
            add(patientIDLable);
            add(medicationLable);
            add(dosageInstructionLable);
            add(dosageTextField);
            setVisible(false);
            PrescriptionButtonListner prescriptionButtonListner = new PrescriptionButtonListner();
            generatePrescriptionBtn.addActionListener(prescriptionButtonListner);

        }

        private class PrescriptionButtonListner implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == generatePrescriptionBtn) {
                    try {
                        prescriptionTextFieldS.fillPrescriptionData(Integer.parseInt(patientIDTextField.getText()));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    prescriptionS.setVisible(false);
                    prescriptionTextFieldS.setVisible(true);
                }
            }
        }
    }

    private class Referral extends JPanel {
        JLabel idLabel = new JLabel();
        JLabel hospitalNameLable = new JLabel();
        JTextField referralIDTextField = new JTextField();
        JTextField hospitalNameTextField = new JTextField();
        public JButton generateReferralBtn = new JButton();

        Referral() {
            setLayout(null);
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            setBackground(Color.CYAN);

            generateReferralBtn.setText("Generate Referral");
            generateReferralBtn.setBounds(335, 243, 163, 35);
            generateReferralBtn.setBackground(buttonBackgroundColor);
            generateReferralBtn.setForeground(buttonForegroundColor);

            idLabel.setForeground(labelForegroundColor);
            idLabel.setBounds(70, 80, 300, 20);
            idLabel.setText("Please enter patient ID");
            idLabel.setFont(new Font("MONOSPACED", Font.HANGING_BASELINE, 13));

            referralIDTextField.setBounds(70, 110, 300, 35);
            hospitalNameTextField.setBounds(70, 183, 300, 35);

            hospitalNameLable.setForeground(labelForegroundColor);
            hospitalNameLable.setBounds(70, 153, 300, 20);
            hospitalNameLable.setText("Please enter hospital name");
            hospitalNameLable.setFont(new Font("MONOSPACED", Font.HANGING_BASELINE, 13));


            add(generateReferralBtn);
            add(idLabel);
            add(referralIDTextField);
            add(hospitalNameLable);
            add(hospitalNameTextField);
            setVisible(false);
            action n = new action();
            generateReferralBtn.addActionListener(n);

        }

        private class action implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == generateReferralBtn) {
                    referralS.setVisible(false);
                    referralTextFieldS.setVisible(true);
                    try {
                        referralTextFieldS.fillReferralData(Integer.parseInt(referralIDTextField.getText()));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    public class PrescriptionTextField extends JPanel {
        JTextArea textPrescription = new JTextArea();
        JButton updateBtn = new JButton();
        JButton printBtn = new JButton();
        JButton editBtn = new JButton();


        PrescriptionTextField() {
            setLayout(null);
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            setBackground(backgroundColor);

            textPrescription.setBounds(20, 20, 535, 390);
            textPrescription.setFont(new Font("MONOSPACED",Font.PLAIN,18));

            updateBtn.setText("Update");
            updateBtn.setBackground(buttonBackgroundColor);
            updateBtn.setForeground(buttonForegroundColor);
            updateBtn.setBounds(380, 430, 100, 30);
            updateBtn.setFocusable(false);
            updateBtn.setBorderPainted(true);
            updateBtn.setContentAreaFilled(false);
            updateBtn.setOpaque(true);

            editBtn.setText("Edit");
            editBtn.setBackground(buttonBackgroundColor);
            editBtn.setForeground(buttonForegroundColor);
            editBtn.setBounds(40, 430, 100, 30);
            editBtn.setFocusable(false);
            editBtn.setBorderPainted(true);
            editBtn.setContentAreaFilled(false);
            editBtn.setOpaque(true);

            printBtn.setText("Print");
            printBtn.setBackground(buttonBackgroundColor);
            printBtn.setForeground(buttonForegroundColor);
            printBtn.setBounds(380, 470, 100, 30);
            printBtn.setFocusable(false);
            printBtn.setBorderPainted(true);
            printBtn.setContentAreaFilled(false);
            printBtn.setOpaque(true);

            PrescriptionTextFieldButton prescriptionTextFieldButton = new PrescriptionTextFieldButton();
            editBtn.addActionListener(prescriptionTextFieldButton);
            updateBtn.addActionListener(prescriptionTextFieldButton);
            printBtn.addActionListener(prescriptionTextFieldButton);

            add(textPrescription);
            add(printBtn);
            add(editBtn);
            add(updateBtn);
            setVisible(false);
        }
        private class PrescriptionTextFieldButton implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == editBtn) {
                    prescriptionS.setVisible(true);
                    prescriptionTextFieldS.setVisible(false);
                }
                else if (e.getSource() == printBtn) {
                    try {
                        textPrescription.print();
                    } catch (PrinterException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (e.getSource() == updateBtn){
                    updatePrescriptionTable(conn);
                    prescriptionTextFieldS.setVisible(false);
                    prescriptionS.setVisible(true);
                }
            }
        }
        public boolean updatePrescriptionTable(Connection conn)
        {
            try {
                PreparedStatement p = conn.prepareStatement("INSERT INTO Prescription(PatientID,DoctorID,MedicationName," +
                                                                 "DosageInstruction) values(?,?,?,?)");
                p.setInt(1,Integer.parseInt(prescriptionS.patientIDTextField.getText()));
                p.setInt(2,id);
                p.setString(3,prescriptionS.medicationTextField.getText());
                p.setString(4, prescriptionS.dosageTextField.getText());

                p.executeUpdate();
                p.close();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        void fillPrescriptionData(int id) throws SQLException {
            PreparedStatement p = conn.prepareStatement("SELECT Patient.Name \n" +
                    "FROM Patient \n" +
                    "WHERE Patient.ID = ?");
            p.setInt(1, id);
            try {
                ResultSet R = p.executeQuery();
                R.next();
                String name = R.getString("Name");
                LocalDate currentDate = LocalDate.now();
                        textPrescription.setText("Patient Name : " + name + "\n" +
                                "Date : " + currentDate +  "\n" +
                                "Medication : " + prescriptionS.medicationTextField.getText() +  "\n" +
                                "Dosage Instruction : " + prescriptionS.dosageTextField.getText() + "\n\n" +
                                "Please ensure that you take the prescribed dosage \n" +
                                "at the specified times and follow the instructions \n" +
                                "provided. Consistent and proper medication adherence \n" +
                                "is essential for your treatment's effectiveness and \n" +
                                "your overall well-being. ");
                    R.close();
                } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        }

    public class ReferralTextField extends JPanel {

        private JTextArea referralTextArea = new JTextArea();
        private JButton updateBtn = new JButton();
        private JButton printBtn = new JButton();
        private JButton editBtn = new JButton();

        ReferralTextField() {
            setLayout(null);
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            setBackground(backgroundColor);

            referralTextArea.setBounds(20, 20, 535, 390);
            referralTextArea.setFont((new Font("MONOSPACED",Font.PLAIN,18)));

            updateBtn.setText("Update");
            updateBtn.setBackground(buttonBackgroundColor);
            updateBtn.setForeground(buttonForegroundColor);
            updateBtn.setBounds(380, 430, 100, 30);
            updateBtn.setFocusable(false);
            updateBtn.setBorderPainted(true);
            updateBtn.setContentAreaFilled(false);
            updateBtn.setOpaque(true);

            editBtn.setText("Edit");
            editBtn.setBackground(buttonBackgroundColor);
            editBtn.setForeground(buttonForegroundColor);
            editBtn.setBounds(40, 430, 100, 30);
            editBtn.setFocusable(false);
            editBtn.setBorderPainted(true);
            editBtn.setContentAreaFilled(false);
            editBtn.setOpaque(true);

            printBtn.setText("Print");
            printBtn.setBackground(buttonBackgroundColor);
            printBtn.setForeground(buttonForegroundColor);
            printBtn.setBounds(380, 470, 100, 30);
            printBtn.setFocusable(false);
            printBtn.setBorderPainted(true);
            printBtn.setContentAreaFilled(false);
            printBtn.setOpaque(true);

            ReferralTextFieldButton referralTextFieldButton = new ReferralTextFieldButton();
            updateBtn.addActionListener(referralTextFieldButton);
            printBtn.addActionListener(referralTextFieldButton);
            editBtn.addActionListener(referralTextFieldButton);

            add(referralTextArea);
            add(printBtn);
            add(editBtn);
            add(updateBtn);
            setVisible(false);

        }
        private class ReferralTextFieldButton implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == editBtn) {
                    referralS.setVisible(true);
                    referralTextFieldS.setVisible(false);
                }
                else if (e.getSource() == printBtn) {
                    try {
                        referralTextArea.print();
                    } catch (PrinterException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (e.getSource() == updateBtn){
                    updateReferralTable();
                    referralS.setVisible(true);
                    referralTextFieldS.setVisible(false);
                }
            }
        }
        public void updateReferralTable()
        {
            try {
                PreparedStatement p = conn.prepareStatement("INSERT INTO Referral (PatientID,DoctorID,HospitalName)" +
                        "values(?,?,?)");
                p.setInt(1,Integer.parseInt(referralS.referralIDTextField.getText()));
                p.setInt(2,id);
                p.setString(3,referralS.hospitalNameTextField.getText());

                p.executeUpdate();
                p.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        void fillReferralData(int id) throws SQLException {
            PreparedStatement p = conn.prepareStatement("SELECT Patient.Name \n" +
                    "FROM Patient \n" +
                    "WHERE Patient.ID = ?");
            p.setInt(1, id);
            try {
                ResultSet R = p.executeQuery();
                R.next();
                LocalDate currentDate = LocalDate.now();
                referralTextArea.setText("Patient Name : " + R.getString("Name" )+ "\n" +
                        "Date : " + currentDate +  "\n" +
                        "Hospital : " + referralS.hospitalNameTextField.getText() +  "\n" +
                        "Referred By : Dr." + name + "\n\n" +
                        "After careful consideration of your medical \n" +
                        "condition and treatment needs, we recommend \n" +
                        "that you seek care at " + referralS.hospitalNameTextField.getText());
                R.close();
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
            setBackground(backgroundColor);

            accountLable = labelInit(null, "Account Management ", 20, 70, (rPanelwidth / 2) - 45, 35);
            accountLable.setForeground(new Color(0x444444));



            SettingButtonActions settingButtonActions = new SettingButtonActions();

            changePasswordBtn = buttonInit(null, "Change Password", 10, 110, (rPanelwidth / 2) - 55, 35);
            changePasswordBtn.addActionListener(settingButtonActions);

            addEmailAddressBtn = buttonInit(null, "Add Email Address", 10, 160, (rPanelwidth / 2) - 55, 35);
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
                    passwordPanel.setVisible(true);
                    emailPanel.setVisible(false);
                } else if (e.getSource() == addEmailAddressBtn) {
                    passwordPanel.setVisible(false);
                    emailPanel.setVisible(true);
                }
            }
        }

    }

    class SettingPasswordPanel extends JPanel {
        JLabel oldPasswordLable = new JLabel();
        JLabel newPasswordLable = new JLabel();
        JLabel confirmPasswordLable = new JLabel();

        JButton saveBtn = new JButton();
        JPasswordField oldPasswordTextField = new JPasswordField();
        JPasswordField newPasswordTextField = new JPasswordField();
        JPasswordField confirmPasswordTextField = new JPasswordField();

        public SettingPasswordPanel() {
            setLayout(null);
            setBounds(lPanelwidth + (rPanelwidth / 2) - 10, 20, (rPanelwidth / 2) - 30, panelheight - 40);
            setBackground(backgroundColor);

            oldPasswordLable = labelInit(null, "Enter Your Old Password", (int) (rPanelwidth * 0.05), (int) (panelheight * 0.1334), 300, 20);
            newPasswordLable = labelInit(null, "Enter New Password", (int) (rPanelwidth * 0.05), (int) (panelheight * 0.25), 300, 20);
            confirmPasswordLable = labelInit(null, "Confirm Your New Password", (int) (rPanelwidth * 0.05), (int) (panelheight * 0.3667), 300, 20);

            oldPasswordTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.1833), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));
            newPasswordTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.3), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));
            confirmPasswordTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.4166), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));

            saveBtn = buttonInit(null, "Save", (int) (rPanelwidth * 0.3166), (int) (panelheight * 0.5), 90, (int) (panelheight * 0.0583));
            PasswordButtonActions settingButtonActions = new PasswordButtonActions();
            saveBtn.addActionListener(settingButtonActions);
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

    public class SettingEmailPanel extends JPanel {
        JLabel emailLable = new JLabel();
        JButton addEmailBtn = new JButton();
        JTextField emailTextField = new JTextField();

        public SettingEmailPanel() {
            setLayout(null);
            setBounds(lPanelwidth + (rPanelwidth / 2) - 10, 20, (rPanelwidth / 2) - 30, panelheight - 40);
            setBackground(backgroundColor);

            emailLable = labelInit(null, "Add New Email Account", (int) (rPanelwidth * 0.05), (int) (panelheight * 0.25), 300, 20);

            emailTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.3), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));

            addEmailBtn = buttonInit(null, "Add", (int) (rPanelwidth * 0.3066), (int) (panelheight * 0.4166), 80, (int) (panelheight * 0.0583));
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
    static JButton buttonInit(ImageIcon icon, String text, int x, int y, int width, int height) {

        JButton btn = new JButton(text, icon);
        btn.setIconTextGap(10);
        btn.setBounds(x, y, width, height);
        btn.setFont(new Font("MONOSPACED", Font.PLAIN, 12));
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setForeground(buttonForegroundColor);
        btn.setFocusable(false);
        btn.setBackground(buttonBackgroundColor);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        return btn;
    }

    static JLabel labelInit(ImageIcon icon, String text, int x, int y, int width, int height) {

        JLabel label = new JLabel();
        label.setIcon(icon);
        label.setForeground(Doctor.labelForegroundColor);
        label.setBounds(x, y, width, height);
        label.setText(text);
        label.setFont(new Font("MONOSPACED", Font.ITALIC, 13));
        return label;
    }

    private class ScreenResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {

            int height = getHeight();
            int width = getWidth();

            lPanelwidth = (int) (width * 0.25);
            panelheight += height - panelheight;
            rPanelwidth = width - lPanelwidth;

            buttonxDir = (int) (lPanelwidth * 0.01);
            buttonWidth = (int) (lPanelwidth * 0.75);
            buttonHeight = (int) (panelheight * 0.0584);

            leftPanel.setBounds(0, 0, lPanelwidth, panelheight);

            dashboardBtn.setBounds(buttonxDir, (int) (panelheight * 0.2), buttonWidth, buttonHeight);
            patientInfobtn.setBounds(buttonxDir, (int) (panelheight * 0.284), buttonWidth, buttonHeight);
            prescriptionbtn.setBounds(buttonxDir, (int) (panelheight * 0.367), buttonWidth, buttonHeight);
            referralbtn.setBounds(buttonxDir, (int) (panelheight * 0.45), buttonWidth, buttonHeight);
            reportbtn.setBounds(buttonxDir, (int) (panelheight * 0.534), buttonWidth, buttonHeight);
            settingbtn.setBounds(buttonxDir, (int) (panelheight * 0.617), buttonWidth, buttonHeight);
            logoutbtn.setBounds(buttonxDir, (int) (panelheight * 0.7), buttonWidth, buttonHeight);

            userLabel.setBounds((leftPanel.getWidth() / 2) - (userLabel.getWidth() / 2), (int) (panelheight * 0.025), 150, (int) (panelheight * 0.1084));
            homeS.setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            patientS.setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            prescriptionS.setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            referralS.setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            settingLpanel.setBounds((int) (lPanelwidth) + (int) (rPanelwidth * 0.0167), (int) (panelheight * 0.034), (int) (rPanelwidth * 0.45), (int) (panelheight * 0.93) - (int) (panelheight * 0.034));
            passwordPanel.setBounds((int) (lPanelwidth + (rPanelwidth / 2)) - (int) (rPanelwidth * 0.025), (int) (panelheight * 0.034), (int) (rPanelwidth * 0.49), (int) (panelheight * 0.93) - (int) (panelheight * 0.034));
            emailPanel.setBounds(lPanelwidth + (rPanelwidth / 2) - 10, 20, (rPanelwidth / 2) - 30, panelheight - 40);


            prescriptionS.generatePrescriptionBtn.setBounds((int) (rPanelwidth * 0.5584), (int) (panelheight * 0.5383), (int) (rPanelwidth * 0.2827), (int) (panelheight * 0.0584));
            prescriptionS.patientIDLable.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.134), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.034));
            prescriptionS.patientIDTextField.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.1834), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.0584));
            prescriptionS.dosageInstructionLable.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.3716), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.034));
            prescriptionS.dosageTextField.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.4216), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.0584));
            prescriptionS.medicationTextField.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.305), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.0584));
            prescriptionS.medicationLable.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.255), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.034));
            prescriptionTextFieldS.setBounds(lPanelwidth, 0, rPanelwidth, panelheight);

            prescriptionTextFieldS.textPrescription.setBounds((int) (rPanelwidth * 0.034), (int) (panelheight * 0.034), (int) (rPanelwidth * 0.8916), (int) (panelheight * 0.65));
            prescriptionTextFieldS.editBtn.setBounds((int) (rPanelwidth * 0.067), (int) (panelheight * 0.7167), (int) (rPanelwidth * 0.167), (int) (panelheight * 0.05));
            prescriptionTextFieldS.printBtn.setBounds((int) (rPanelwidth * 0.634), (int) (panelheight * 0.7834), (int) (rPanelwidth * 0.167), (int) (panelheight * 0.05));
            prescriptionTextFieldS.updateBtn.setBounds((int) (rPanelwidth * 0.634), (int) (panelheight * 0.7167), (int) (rPanelwidth * 0.167), (int) (panelheight * 0.05));

            referralS.generateReferralBtn.setBounds((int) (rPanelwidth * 0.5584), (int) (panelheight * 0.405), (int) (rPanelwidth * 0.2717), (int) (panelheight * 0.0584));
            referralS.referralIDTextField.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.1834), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.0584));
            referralS.hospitalNameTextField.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.305), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.0584));
            referralS.hospitalNameLable.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.255), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.034));
            referralS.idLabel.setBounds((int) (rPanelwidth * 0.1167), (int) (panelheight * 0.134), (int) (rPanelwidth * 0.5), (int) (panelheight * 0.034));


            referralTextFieldS.setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            referralTextFieldS.referralTextArea.setBounds((int) (rPanelwidth * 0.034), (int) (panelheight * 0.034), (int) (rPanelwidth * 0.8916), (int) (panelheight * 0.65));
            referralTextFieldS.editBtn.setBounds((int) (rPanelwidth * 0.067), (int) (panelheight * 0.7167), (int) (rPanelwidth * 0.167), (int) (panelheight * 0.05));
            referralTextFieldS.printBtn.setBounds((int) (rPanelwidth * 0.634), (int) (panelheight * 0.7834), (int) (rPanelwidth * 0.167), (int) (panelheight * 0.05));
            referralTextFieldS.updateBtn.setBounds((int) (rPanelwidth * 0.634), (int) (panelheight * 0.7167), (int) (rPanelwidth * 0.167), (int) (panelheight * 0.05));

            settingLpanel.accountLable.setBounds((int) (rPanelwidth * 0.0167), (int) (panelheight * 0.1166), (rPanelwidth / 2) - (int) (rPanelwidth * 0.075), (int) (panelheight * 0.0583));
            settingLpanel.changePasswordBtn.setBounds((int) (rPanelwidth * 0.0167), (int) (panelheight * 0.1834), (rPanelwidth / 2) - (int) (rPanelwidth * 0.075), (int) (panelheight * 0.0583));
            settingLpanel.addEmailAddressBtn.setBounds((int) (rPanelwidth * 0.0167), (int) (panelheight * 0.2667), (rPanelwidth / 2) - (int) (rPanelwidth * 0.075), (int) (panelheight * 0.0583));
            passwordPanel.oldPasswordLable.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.1334), 300, 20);
            passwordPanel.newPasswordLable.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.25), 300, 20);
            passwordPanel.confirmPasswordLable.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.3667), 300, 20);
            passwordPanel.oldPasswordTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.1833), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));
            passwordPanel.newPasswordTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.3), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));
            passwordPanel.confirmPasswordTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.4166), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));
            passwordPanel.saveBtn.setBounds((int) (rPanelwidth * 0.3166), (int) (panelheight * 0.5), 90, (int) (panelheight * 0.0583));

            emailPanel.addEmailBtn.setBounds((int) (rPanelwidth * 0.3066), (int) (panelheight * 0.4166), 80, (int) (panelheight * 0.0583));
            emailPanel.emailLable.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.25), 300, 20);
            emailPanel.emailTextField.setBounds((int) (rPanelwidth * 0.05), (int) (panelheight * 0.3), (int) (rPanelwidth * 0.3334), (int) (panelheight * 0.0583));

        }
    }

    void panelsVisibility() {
        homeS.setVisible(false);
        patientS.setVisible(false);
        referralS.setVisible(false);
        settingLpanel.setVisible(false);
        prescriptionS.setVisible(false);
        prescriptionTextFieldS.setVisible(false);
        referralTextFieldS.setVisible(false);
        emailPanel.setVisible(false);
        passwordPanel.setVisible(false);
    }

    private class LeftPanelButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dashboardBtn) {
                panelsVisibility();
                homeS.setVisible(true);
            } else if (e.getSource() == patientInfobtn) {
                panelsVisibility();
                patientS.setVisible(true);
            } else if (e.getSource() == prescriptionbtn) {
                panelsVisibility();
                prescriptionS.setVisible(true);
            } else if (e.getSource() == referralbtn) {
                panelsVisibility();
                referralS.setVisible(true);
            }
            else if (e.getSource() == settingbtn) {

                panelsVisibility();
                settingLpanel.setVisible(true);
                passwordPanel.setVisible(true);
            } else if (e.getSource() == logoutbtn) {
                panelsVisibility();
                setVisible(false);
            }


        }
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

