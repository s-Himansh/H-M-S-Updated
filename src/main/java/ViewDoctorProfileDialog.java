import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewDoctorProfileDialog extends JDialog {
     private int doctorId;
     private ResultSet doctorDetails;

     public ViewDoctorProfileDialog(int doctorId) {
          this.doctorId = doctorId;
          fetchDoctorDetails();
          initializeComponents();
     }

     private void fetchDoctorDetails() {
          try {
               Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM doctors WHERE doctor_id = ?");
               statement.setInt(1, doctorId);
               doctorDetails = statement.executeQuery();
               doctorDetails.next(); // Move cursor to the first row
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     private void initializeComponents() {
          setTitle("View Profile");
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setSize(500, 300); // Increased size
          setLocationRelativeTo(null); // Center the dialog on the screen

          // Create main panel
          JPanel mainPanel = new JPanel(new BorderLayout());
          mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

          // Create panel for displaying profile information
          JPanel profilePanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Adjust rows, cols, hgap, and vgap as needed
          profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

          // Add labels for profile information with increased font size
          JLabel nameLabel = new JLabel("Name: " + getFullName());
          JLabel specializationLabel = new JLabel("Specialization: " + getSpecialization());
          JLabel emailLabel = new JLabel("Email: " + getEmail());
          JLabel phoneLabel = new JLabel("Phone: " + getPhone());

          // Customize label font and size
          Font labelFont = nameLabel.getFont().deriveFont(Font.BOLD, 16); // Increase font size to 16
          nameLabel.setFont(labelFont);
          specializationLabel.setFont(labelFont);
          emailLabel.setFont(labelFont);
          phoneLabel.setFont(labelFont);

          // Add profile information labels to profile panel
          profilePanel.add(nameLabel);
          profilePanel.add(specializationLabel);
          profilePanel.add(emailLabel);
          profilePanel.add(phoneLabel);

          // Add profile panel to main panel
          mainPanel.add(profilePanel, BorderLayout.CENTER);

          // Set main panel as content pane
          setContentPane(mainPanel);
     }

     private String getFullName() {
          try {
               return doctorDetails.getString("name");
          } catch (SQLException e) {
               e.printStackTrace();
               return "N/A";
          }
     }

     private String getSpecialization() {
          try {
               return doctorDetails.getString("specialty");
          } catch (SQLException e) {
               e.printStackTrace();
               return "N/A";
          }
     }

     private String getEmail() {
          // Assuming email is not provided in the database for doctors
          return "N/A";
     }

     private String getPhone() {
          try {
               return doctorDetails.getString("contact_info");
          } catch (SQLException e) {
               e.printStackTrace();
               return "N/A";
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               // For testing purposes, pass a sample doctor ID
               int sampleDoctorId = 2; // Provide the doctor ID
               new ViewDoctorProfileDialog(sampleDoctorId).setVisible(true);
          });
     }
}
