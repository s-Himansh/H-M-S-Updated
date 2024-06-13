import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PatientDashboard extends JFrame {
    static ResultSet user ;
    public PatientDashboard(ResultSet userDetails) {
        user = userDetails;

        setTitle("Patient Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // Adjust rows, cols, hgap, and vgap as needed


        JButton makeAppointmentButton = new JButton("Make Appointment");
        JButton viewProfileButton = new JButton("View Profile");
        JButton viewAppointmentsButton = new JButton("View Appointments");


        makeAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new AppointmentForm(user).setVisible(true);
                try{
                    new AppointmentForm(user.getInt("user_id")).setVisible(true);
                }catch (Exception exp){

                }

//                try {
//                    System.out.println("user is " + user.getInt("user_id"));
//                    System.out.println("username is " + user.getString("username"));
//                }catch(Exception exp){
//
//                }
            }
        });

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    new ViewProfileDialog(user.getInt("user_id")).setVisible(true);
                }catch(Exception exp){

                }

            }
        });

        viewAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ViewAppointments(user.getInt("user_id")).setVisible(true);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        });

        buttonPanel.add(makeAppointmentButton);
        buttonPanel.add(viewProfileButton);
        buttonPanel.add(viewAppointmentsButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("user is " + user);
                new PatientDashboard(user).setVisible(true);
            }
        });
    }

}
