import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentForm extends JFrame {
     private Connection connection;
     private int patientId;

     private JComboBox<String> doctorComboBox;
     private JTextField dateField;

     public AppointmentForm(int patientId) {
          this.patientId = patientId;
          System.out.println("id is " + patientId);
          // Initialize components and set up the appointment form
          setTitle("Appointment Form");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setSize(400, 200); // Set the size as needed
          setLocationRelativeTo(null); // Center the window on the screen

          // Create main panel
          JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));

          // Add components for doctor selection
          JLabel doctorLabel = new JLabel("Select Doctor:");
          doctorComboBox = new JComboBox<>();
          populateDoctors(); // Populate doctor names in combo box
          mainPanel.add(doctorLabel);
          mainPanel.add(doctorComboBox);

          // Add components for appointment date selection
          JLabel dateLabel = new JLabel("Appointment Date:");
          dateField = new JTextField();
          mainPanel.add(dateLabel);
          mainPanel.add(dateField);

          // Add submit button
          JButton submitButton = new JButton("Submit");
          submitButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    submitAppointment();
               }
          });
          mainPanel.add(new JLabel()); // Placeholder
          mainPanel.add(submitButton);

          // Set main panel as content pane
          setContentPane(mainPanel);
     }

     private void populateDoctors() {
          try {
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM doctors");
               ResultSet resultSet = statement.executeQuery();
               while (resultSet.next()) {
                    String doctorName = resultSet.getString("name");
                    doctorComboBox.addItem(doctorName);
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     private void submitAppointment() {
          try {
               String selectedDoctor = (String) doctorComboBox.getSelectedItem();
               String date = dateField.getText();

               // Retrieve doctor ID based on the selected name
               PreparedStatement statement = connection.prepareStatement("SELECT doctor_id FROM doctors WHERE name = ?");
               statement.setString(1, selectedDoctor);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    int doctorId = resultSet.getInt("doctor_id");

                    // Insert appointment into database
                    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)");
                    insertStatement.setInt(1, patientId);
                    insertStatement.setInt(2, doctorId);
                    insertStatement.setString(3, date);
                    insertStatement.setString(4, "Scheduled"); // Initial status
                    insertStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
                    dispose(); // Close the appointment form
               } else {
                    JOptionPane.showMessageDialog(this, "Doctor not found!");
               }
          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error scheduling appointment!");
          }
     }


     public static void main(String[] args) {
          SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                    // For testing purposes, pass a sample patient ID
                    int samplePatientId = 6;
                    new AppointmentForm(samplePatientId).setVisible(true);
               }
          });
     }
}
