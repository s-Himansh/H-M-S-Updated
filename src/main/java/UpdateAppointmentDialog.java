import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAppointmentDialog extends JDialog {
     private JTextField dateField;
     private JTextField timeField;
     private JTextField patientNameField;
     private JTextField doctorNameField;
     private JComboBox<String> statusComboBox;
     private int appointmentId;

     public UpdateAppointmentDialog(JDialog parent, String patientId, String doctorId, String appointmentDate, String status, int appointmentId) {
          super(parent, "Update Appointment", true);
          this.appointmentId = appointmentId;

          JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
          mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

          JLabel dateLabel = new JLabel("Patient ID: ");
          dateField = new JTextField(patientId);
          JLabel patientNameLabel = new JLabel("Doctor ID: ");
          patientNameField = new JTextField(doctorId);
          JLabel doctorNameLabel = new JLabel("Appointment Date: ");
          doctorNameField = new JTextField(appointmentDate);
          JLabel statusLabel = new JLabel("Status:");
          String[] statuses = {"Pending", "Confirmed", "Completed"};
          statusComboBox = new JComboBox<>(statuses);
          statusComboBox.setSelectedItem(status);

          mainPanel.add(dateLabel);
          mainPanel.add(dateField);
          mainPanel.add(patientNameLabel);
          mainPanel.add(patientNameField);
          mainPanel.add(doctorNameLabel);
          mainPanel.add(doctorNameField);
          mainPanel.add(statusLabel);
          mainPanel.add(statusComboBox);

          JButton updateButton = new JButton("Update");
          JButton cancelButton = new JButton("Cancel");

          JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          buttonPanel.add(updateButton);
          buttonPanel.add(cancelButton);

          updateButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    updateAppointment();
               }
          });

          cancelButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    dispose();
               }
          });

          getContentPane().setLayout(new BorderLayout());
          getContentPane().add(mainPanel, BorderLayout.CENTER);
          getContentPane().add(buttonPanel, BorderLayout.SOUTH);

          pack();
          setLocationRelativeTo(parent);
     }

     private void updateAppointment() {
          // Get updated values from the text fields
          String patientId = dateField.getText().trim(); // Assuming this field represents patient_id
          String doctorId = patientNameField.getText().trim(); // Assuming this field represents doctor_id
          String appointmentDate = doctorNameField.getText().trim();
          String status = (String) statusComboBox.getSelectedItem();

          // Perform validation if needed

          try {
               // Establish database connection
               Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               // Create the SQL statement
               String sql = "UPDATE appointments SET appointment_date=?, patient_id=?, doctor_id=?, status=? WHERE appointment_id=?";
               // Create prepared statement
               PreparedStatement statement = connection.prepareStatement(sql);
               // Set parameters
               statement.setString(1, appointmentDate);
               statement.setString(2, patientId);
               statement.setString(3, doctorId);
               statement.setString(4, status);
               statement.setInt(5, appointmentId);
               // Execute the update
               int rowsUpdated = statement.executeUpdate();
               if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Appointment updated successfully");
               } else {
                    JOptionPane.showMessageDialog(this, "Failed to update appointment", "Error", JOptionPane.ERROR_MESSAGE);
               }
          } catch (SQLException ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error updating appointment", "Error", JOptionPane.ERROR_MESSAGE);
          }

          // Close the dialog
          dispose();
     }

}
