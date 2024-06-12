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
        setSize(800, 600); // Set the size as needed
        setLocationRelativeTo(null); // Center the window on the screen

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // Adjust rows, cols, hgap, and vgap as needed

        // Add buttons for different functionalities
        JButton viewProfileButton = new JButton("View Profile");
        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton checkMedicalRecordsButton = new JButton("Check Medical Records");

        // Add action listeners to buttons
        viewProfileButton.addActionListener(e -> {
            // Open View Profile window
            // You can create and show the ViewProfile window here
            // JOptionPane.showMessageDialog(DoctorDashboard.this, "View Profile clicked");
            try{
                new ViewDoctorProfileDialog(user.getInt("user_id")).setVisible(true);
            }catch (Exception exp){

            }
        });

        viewAppointmentsButton.addActionListener(e -> {
            // Open View Appointments window
            // You can create and show the ViewAppointments window here
            // JOptionPane.showMessageDialog(DoctorDashboard.this, "View Appointments clicked");
            try {
                new ViewDoctorAppointmentsDialog(user.getInt("user_id")).setVisible(true);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });

        checkMedicalRecordsButton.addActionListener(e -> {
            // Open Medical Records window
            // You can create and show the MedicalRecordsDialog window here
            // JOptionPane.showMessageDialog(DoctorDashboard.this, "Check Medical Records clicked");
            try {
                new ViewMedicalRecordsDialog(user.getInt("user_id")).setVisible(true);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });

        // Add buttons to button panel
        buttonPanel.add(viewProfileButton);
        buttonPanel.add(viewAppointmentsButton);
        buttonPanel.add(checkMedicalRecordsButton);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Set main panel as content pane
        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing purposes, pass a sample user details
            ResultSet sampleUserDetails = null; // Provide the ResultSet object with user details
            new DoctorDashboard(sampleUserDetails).setVisible(true);
        });
    }
}
