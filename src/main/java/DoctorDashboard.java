import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class DoctorDashboard extends JFrame {
    private ResultSet user;

    public DoctorDashboard(ResultSet userDetails) {
        this.user = userDetails;
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Doctor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton viewProfileButton = new JButton("View Profile");
        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton checkMedicalRecordsButton = new JButton("Check Medical Records");

        viewProfileButton.addActionListener(e -> {
            // JOptionPane.showMessageDialog(DoctorDashboard.this, "View Profile clicked");
            try{
                new ViewDoctorProfileDialog(user.getInt("user_id")).setVisible(true);
            }catch (Exception exp){

            }
        });

        viewAppointmentsButton.addActionListener(e -> {
            // JOptionPane.showMessageDialog(DoctorDashboard.this, "View Appointments clicked");
            try {
                new ViewDoctorAppointmentsDialog(user.getInt("user_id")).setVisible(true);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });

        checkMedicalRecordsButton.addActionListener(e -> {
            // JOptionPane.showMessageDialog(DoctorDashboard.this, "Check Medical Records clicked");
            try {
                new ViewMedicalRecordsDialog(user.getInt("user_id")).setVisible(true);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });

        buttonPanel.add(viewProfileButton);
        buttonPanel.add(viewAppointmentsButton);
        buttonPanel.add(checkMedicalRecordsButton);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ResultSet sampleUserDetails = null;
            new DoctorDashboard(sampleUserDetails).setVisible(true);
        });
    }
}
