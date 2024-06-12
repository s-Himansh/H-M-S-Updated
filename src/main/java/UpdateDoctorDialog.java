import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateDoctorDialog extends JDialog {
     private JTextField nameField;
     private JTextField specialtyField;
     private JTextField contactInfoField;
     private JButton saveButton;

     public UpdateDoctorDialog(JDialog parent, int doctorId, String name, String specialty, String contactInfo) {
          super(parent, "Update Doctor", true);
          initializeComponents(doctorId, name, specialty, contactInfo);
     }

     private void initializeComponents(int doctorId, String name, String specialty, String contactInfo) {
          setSize(400, 300);
          setLocationRelativeTo(null);

          JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
          panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

          panel.add(new JLabel("Name:"));
          nameField = new JTextField(name);
          panel.add(nameField);

          panel.add(new JLabel("Specialty:"));
          specialtyField = new JTextField(specialty);
          panel.add(specialtyField);

          panel.add(new JLabel("Contact Info:"));
          contactInfoField = new JTextField(contactInfo);
          panel.add(contactInfoField);

          saveButton = new JButton("Save");
          saveButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    updateDoctorInDatabase(doctorId);
               }
          });

          panel.add(new JLabel()); // Empty label to align the button
          panel.add(saveButton);

          setContentPane(panel);
     }

     private void updateDoctorInDatabase(int doctorId) {
          String name = nameField.getText();
          String specialty = specialtyField.getText();
          String contactInfo = contactInfoField.getText();

          if (name.isEmpty() || specialty.isEmpty() || contactInfo.isEmpty()) {
               JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma")) {
               // Update the doctor's information in the database
               String query = "UPDATE doctors SET name = ?, specialty = ?, contact_info = ? WHERE doctor_id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setString(1, name);
               statement.setString(2, specialty);
               statement.setString(3, contactInfo);
               statement.setInt(4, doctorId);
               statement.executeUpdate();

               JOptionPane.showMessageDialog(this, "Doctor updated successfully");
               dispose();
          } catch (SQLException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error updating doctor in the database", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }
}
