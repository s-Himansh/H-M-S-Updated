import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePatientDialog extends JDialog {
     private JTextField nameField;
     private JTextField ageField;
     private JTextField genderField;
     private JTextField addressField;
     private JTextField contactInfoField;
     private JButton updateButton;
     private int patientId;

     public UpdatePatientDialog(JDialog parent, int patientId) {
          super(parent, "Update Patient", true);
          this.patientId = patientId;
          initializeComponents();
          loadPatientDetails();
     }

     private void initializeComponents() {
          setSize(400, 300);
          setLocationRelativeTo(null);

          JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
          panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

          panel.add(new JLabel("Name:"));
          nameField = new JTextField();
          panel.add(nameField);

          panel.add(new JLabel("Age:"));
          ageField = new JTextField();
          panel.add(ageField);

          panel.add(new JLabel("Gender:"));
          genderField = new JTextField();
          panel.add(genderField);

          panel.add(new JLabel("Address:"));
          addressField = new JTextField();
          panel.add(addressField);

          panel.add(new JLabel("Contact Info:"));
          contactInfoField = new JTextField();
          panel.add(contactInfoField);

          updateButton = new JButton("Update");
          updateButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    updatePatientInDatabase();
               }
          });

          panel.add(new JLabel()); // Empty label to align the button
          panel.add(updateButton);

          setContentPane(panel);
     }

     private void loadPatientDetails() {
          try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma")) {
               String query = "SELECT * FROM patients WHERE patient_id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, patientId);
               var resultSet = statement.executeQuery();

               if (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    ageField.setText(String.valueOf(resultSet.getInt("age")));
                    genderField.setText(resultSet.getString("gender"));
                    addressField.setText(resultSet.getString("address"));
                    contactInfoField.setText(resultSet.getString("contact_info"));
               } else {
                    JOptionPane.showMessageDialog(this, "Patient not found", "Error", JOptionPane.ERROR_MESSAGE);
                    dispose();
               }
          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error loading patient details", "Error", JOptionPane.ERROR_MESSAGE);
               dispose();
          }
     }

     private void updatePatientInDatabase() {
          String name = nameField.getText();
          String age = ageField.getText();
          String gender = genderField.getText();
          String address = addressField.getText();
          String contactInfo = contactInfoField.getText();

          if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || address.isEmpty() || contactInfo.isEmpty()) {
               JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma")) {
               String query = "UPDATE patients SET name = ?, age = ?, gender = ?, address = ?, contact_info = ? WHERE patient_id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setString(1, name);
               statement.setInt(2, Integer.parseInt(age));
               statement.setString(3, gender);
               statement.setString(4, address);
               statement.setString(5, contactInfo);
               statement.setInt(6, patientId);
               int rowsAffected = statement.executeUpdate();

               if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Patient updated successfully");
                    dispose();
               } else {
                    JOptionPane.showMessageDialog(this, "Failed to update patient", "Error", JOptionPane.ERROR_MESSAGE);
               }
          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error updating patient in the database", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               new UpdatePatientDialog(null, 1).setVisible(true);
          });
     }
}
