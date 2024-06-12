import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddPatientDialog extends JDialog {
     private JTextField nameField;
     private JTextField ageField;
     private JTextField genderField;
     private JTextField contactField;
     private JTextField addressField;
     private JTextField contactInfoField;
     private JTextField usernameField;
     private JTextField passwordField;
     private JButton addButton;

     public AddPatientDialog(JDialog parent) {
          super(parent, "Add New Patient", true);
          initializeComponents();
     }

     private void initializeComponents() {
          setSize(400, 450);
          setLocationRelativeTo(null);

          JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
          panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

          panel.add(new JLabel("Username:"));
          usernameField = new JTextField();
          panel.add(usernameField);

          panel.add(new JLabel("Password:"));
          passwordField = new JTextField();
          panel.add(passwordField);

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

          addButton = new JButton("Add");
          addButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    addPatientToDatabase();
               }
          });

          panel.add(new JLabel()); // Empty label to align the button
          panel.add(addButton);

          setContentPane(panel);
     }

     private void addPatientToDatabase() {
          String username = usernameField.getText();
          String password = passwordField.getText();
          String name = nameField.getText();
          String age = ageField.getText();
          String gender = genderField.getText();
          String address = addressField.getText();
          String contactInfo = contactInfoField.getText();

          if (username.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || gender.isEmpty() || address.isEmpty() || contactInfo.isEmpty()) {
               JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma")) {
               // Insert into users table
               String userQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, 'patient')";
               PreparedStatement userStatement = connection.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
               userStatement.setString(1, username);
               userStatement.setString(2, password);
               userStatement.executeUpdate();

               // Retrieve the generated user_id
               ResultSet generatedKeys = userStatement.getGeneratedKeys();
               if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert into patients table
                    String patientQuery = "INSERT INTO patients (patient_id, name, age, gender, address, contact_info) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement patientStatement = connection.prepareStatement(patientQuery);
                    patientStatement.setInt(1, userId);
                    patientStatement.setString(2, name);
                    patientStatement.setInt(3, Integer.parseInt(age));
                    patientStatement.setString(4, gender);
                    patientStatement.setString(5, address);
                    patientStatement.setString(6, contactInfo);
                    patientStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Patient added successfully");
                    dispose();
               } else {
                    JOptionPane.showMessageDialog(this, "Failed to retrieve user ID", "Error", JOptionPane.ERROR_MESSAGE);
               }
          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error adding patient to the database", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               new AddPatientDialog(null).setVisible(true);
          });
     }
}
