import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Receptionist extends JFrame {
    Connection conn = null;
    private JPanel leftPanel = new JPanel();
    private static int lPanelwidth = 200;
    private static int rPanelwidth = 600;
    private static int panelheight = 600;
    JLabel errorMessage = new JLabel();
    JLabel errorMessage2 = new JLabel();
        JLabel headerMessage1 = new JLabel();
        JLabel patientIDLabel = new JLabel();
        JLabel addPatientNameLabel = new JLabel();
        JLabel addAgelabel = new JLabel();
        JLabel addContactInfoLabel = new JLabel();

        JTextField patientIDTextField = new JTextField();
        JLabel addGenderLabel = new JLabel();
        JTextField addGenderTextField = new JTextField();
        JTextField addPatientNameTextField = new JTextField();
        JTextField addAgeTextField = new JTextField();
        JTextField addContactInfoTextField = new JTextField();
        public JButton addDataBtn = new JButton();


    Receptionist(Connection conn) {

            this.conn = conn;

            setLayout(null);
            setSize(rPanelwidth, panelheight);
            setBounds(lPanelwidth, 0, rPanelwidth, panelheight);
            setBackground(Doctor.backgroundColor);

            headerMessage1 = Doctor.labelInit(null, "TO ADD NEW PATIENT TO THE STOCK", 50, 40, 300, 20);
            headerMessage1.setForeground(new Color(0x444444));

            patientIDLabel  = Doctor.labelInit(null, "Please Enter Student ID", 70, 80, 300, 20);
            addPatientNameLabel = Doctor.labelInit(null, "Please Enter Patient Name", 70, 150, 300, 20);
            addAgelabel = Doctor.labelInit(null, "Please Enter Age of Patient", 70, 220, 300, 20);
            addContactInfoLabel = Doctor.labelInit(null, "Please Enter ContactIfo of The Patient", 70, 290, 380, 20);
            addGenderLabel = Doctor.labelInit(null, "Please Enter Gender of The Patient", 70, 360, 380, 20);

            patientIDTextField.setBounds(70, 110, 300, 35);
            addPatientNameTextField.setBounds(70, 180, 300, 35);
            addAgeTextField.setBounds(70, 250, 300, 35);
            addContactInfoTextField.setBounds(70, 320, 300, 35);
            addGenderTextField.setBounds(70, 390, 300, 35);

            errorMessage = Doctor.labelInit(null,"Patient ID Not Found ! ",150, 505, 370, 40);
            errorMessage.setVisible(false);
            errorMessage2 = Doctor.labelInit(null,"Please Fill All The Input Area ! ",150, 505, 220, 40);
            errorMessage2.setVisible(false);

            addDataBtn = Doctor.buttonInit(null, "Add", 335, 460, 163, 35);
            ReceptionistButtonListner receptionistButtonListner = new ReceptionistButtonListner();
            addDataBtn.addActionListener(receptionistButtonListner);

            add(headerMessage1);
            add(patientIDLabel);
            add(patientIDTextField);
            add(addPatientNameTextField);
            add(addAgeTextField);
            add(addDataBtn);
            add(addPatientNameLabel);
            add(addAgelabel);
            add(addContactInfoTextField);
            add(addContactInfoLabel);
            add(addGenderLabel);
            add(addGenderTextField);
            add(errorMessage);
            add(errorMessage2);

            setVisible(false);

        }
    private class ReceptionistButtonListner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addDataBtn) {
                updateStudentData();
            }
        }
    }
    public void updateStudentData() {

        PreparedStatement p = null;
        try {
            p = conn.prepareStatement("SELECT * FROM Student where StudentID = ?");
            p.setInt(1, Integer.parseInt(patientIDTextField.getText()));

        } catch (SQLException e) {
            errorMessage.setVisible(true);
            errorMessage2.setVisible(false);
        }

        try {
            ResultSet R = p.executeQuery();

            if (R != null) {
                R.next();
                {
                    try {
                        PreparedStatement p1 = conn.prepareStatement("INSERT INTO Patient(Name,Age,ContactInfo,Gender) values(?,?,?,?)");

                        p1.setString(1, addPatientNameTextField.getText());
                        p1.setInt(2, Integer.parseInt(addAgeTextField.getText()));
                        p1.setString(3, addContactInfoTextField.getText());
                        p1.setString(4, addGenderTextField.getText());

                        p1.executeUpdate();
                        p1.close();

                    } catch (SQLException e) {
                        errorMessage.setVisible(false);
                        errorMessage2.setVisible(true);
                    }
                }
            }

            R.close();
        }
        catch (SQLException E)
        {
            errorMessage.setVisible(true);
            errorMessage2.setVisible(false);
        }
    }

    }
