import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewProfileDialog extends JDialog {
     private int patientId;

     public ViewProfileDialog(int patientId) {
          this.patientId = patientId;
          initializeComponents();
          fetchAndDisplayProfile();
     }

     private void initializeComponents() {
          setTitle("View Profile");
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setSize(600, 300); // Increased width and height
          setLocationRelativeTo(null);

          // Increase default font size for all labels
          UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 16));
     }

     private void fetchAndDisplayProfile() {
          try {
               Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM patients WHERE patient_id = ?");
               statement.setInt(1, patientId);
               ResultSet resultSet = statement.executeQuery();

               if (resultSet.next()) {
                    // Extract patient data
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    String gender = resultSet.getString("gender");
                    String address = resultSet.getString("address");
                    String contactInfo = resultSet.getString("contact_info");

                    // Create panel to display patient data
                    JPanel panel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.insets = new Insets(5, 5, 5, 5);

                    addLabel(panel, gbc, "Patient ID:", Integer.toString(patientId));
                    addLabel(panel, gbc, "Name:", name);
                    addLabel(panel, gbc, "Age:", Integer.toString(age));
                    addLabel(panel, gbc, "Gender:", gender);
                    addLabel(panel, gbc, "Address:", address);
                    addLabel(panel, gbc, "Contact Info:", contactInfo);

                    getContentPane().add(panel, BorderLayout.CENTER);
               } else {
                    JOptionPane.showMessageDialog(this, "Patient data not found.");
               }
          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error retrieving patient data.");
          }
     }

     private void addLabel(JPanel panel, GridBagConstraints gbc, String labelText, String value) {
          JLabel label = new JLabel(labelText);
          label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1)); // Border color and thickness
          gbc.gridx = 0;
          panel.add(label, gbc);

          JLabel valueLabel = new JLabel(value);
          valueLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1)); // Border color and thickness
          gbc.gridx = 1;
          panel.add(valueLabel, gbc);

          gbc.gridy++;
     }

     public static void main(String[] args) {
          int samplePatientId = 6;
          SwingUtilities.invokeLater(() -> new ViewProfileDialog(samplePatientId).setVisible(true));
     }
}
