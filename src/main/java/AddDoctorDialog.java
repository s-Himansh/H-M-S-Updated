import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddDoctorDialog extends JDialog {
     private JTextField nameField;
     private JTextField specialtyField;
     private JTextField contactInfoField;
     private JTextField usernameField;
     private JPasswordField passwordField;
     private JButton addButton;

     public AddDoctorDialog(JDialog parent) {
          super(parent, "Add New Doctor", true);
          initializeComponents();
     }

     private void initializeComponents() {
          setSize(400, 300);
          setLocationRelativeTo(null);

          JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
          panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

          panel.add(new JLabel("Username:"));
          usernameField = new JTextField();
          panel.add(usernameField);

          panel.add(new JLabel("Password:"));
          passwordField = new JPasswordField();
          panel.add(passwordField);

          panel.add(new JLabel("Name:"));
          nameField = new JTextField();
          panel.add(nameField);

          panel.add(new JLabel("Specialty:"));
          specialtyField = new JTextField();
          panel.add(specialtyField);

          panel.add(new JLabel("Contact Info:"));
          contactInfoField = new JTextField();
          panel.add(contactInfoField);

          addButton = new JButton("Add");
          addButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    addDoctorToDatabase();
               }
          });

          panel.add(new JLabel()); // Empty label to align the button
          panel.add(addButton);

          setContentPane(panel);
     }

     private void addDoctorToDatabase() {
          String username = usernameField.getText();
          String password = new String(passwordField.getPassword());
          String name = nameField.getText();
          String specialty = specialtyField.getText();
          String contactInfo = contactInfoField.getText();

          if (username.isEmpty() || password.isEmpty() || name.isEmpty() || specialty.isEmpty() || contactInfo.isEmpty()) {
               JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma")) {
               // Insert into users table
               String role = "doctor";
               String userQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
               PreparedStatement userStatement = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
               userStatement.setString(1, username);
               userStatement.setString(2, password);
               userStatement.setString(3, role);
               userStatement.executeUpdate();

               // Retrieve the generated user_id
               int userId;
               ResultSet generatedKeys = userStatement.getGeneratedKeys();
               if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);

                    // Insert into doctors table
                    String query = "INSERT INTO doctors (doctor_id, name, specialty, contact_info) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, userId);
                    statement.setString(2, name);
                    statement.setString(3, specialty);
                    statement.setString(4, contactInfo);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Doctor added successfully");
                    dispose();
               } else {
                    JOptionPane.showMessageDialog(this, "Failed to retrieve user ID", "Error", JOptionPane.ERROR_MESSAGE);
               }
          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error adding doctor to the database", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               new AddDoctorDialog(null).setVisible(true);
          });
     }
}
