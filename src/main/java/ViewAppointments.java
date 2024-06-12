import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAppointments extends JFrame {
     private Connection connection;
     private int userId;
     private JTable appointmentsTable;

     public ViewAppointments(int userId) {
          this.userId = userId;
          // Initialize components and set up the appointment viewing window
          setTitle("View Appointments");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setSize(800, 400); // Set the size as needed
          setLocationRelativeTo(null); // Center the window on the screen

          // Create main panel
          JPanel mainPanel = new JPanel(new BorderLayout());

          // Fetch and display appointments
          fetchAppointments();

          // Add table to a scroll pane and add it to the main panel
          JScrollPane scrollPane = new JScrollPane(appointmentsTable);
          mainPanel.add(scrollPane, BorderLayout.CENTER);

          // Set main panel as content pane
          setContentPane(mainPanel);
     }

     private void fetchAppointments() {
          try {
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments WHERE patient_id = ?");
               statement.setInt(1, userId);
               ResultSet resultSet = statement.executeQuery();

               // Create a table model with appropriate column names
               DefaultTableModel model = new DefaultTableModel();
               model.addColumn("Appointment ID");
               model.addColumn("Doctor ID");
               model.addColumn("Appointment Date");
               model.addColumn("Status");

               // Populate the table model with data from the result set
               while (resultSet.next()) {
                    int appointmentId = resultSet.getInt("appointment_id");
                    int doctorId = resultSet.getInt("doctor_id");
                    String appointmentDate = resultSet.getString("appointment_date");
                    String status = resultSet.getString("status");
                    model.addRow(new Object[]{appointmentId, doctorId, appointmentDate, status});
               }

               // Create appointments table with the model
               appointmentsTable = new JTable(model);
               appointmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                    // For testing purposes, pass a sample user ID
                    int sampleUserId = 6;
                    new ViewAppointments(sampleUserId).setVisible(true);
               }
          });
     }
}
