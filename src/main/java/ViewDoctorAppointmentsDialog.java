import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewDoctorAppointmentsDialog extends JDialog {
     private int doctorId;
     private JTable appointmentsTable;

     public ViewDoctorAppointmentsDialog(int doctorId) {
          this.doctorId = doctorId;
          initializeComponents();
          fetchAndDisplayAppointments();
     }

     private void initializeComponents() {
          setTitle("Doctor Appointments");
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setSize(600, 400);
          setLocationRelativeTo(null);
     }

     private void fetchAndDisplayAppointments() {
          try {
               Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments WHERE doctor_id = ?");
               statement.setInt(1, doctorId);
               ResultSet resultSet = statement.executeQuery();

               // Create a table model with appropriate column names
               DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                         return columnIndex == 0 ? Integer.class : String.class;
                    }
               };
               model.addColumn("Appointment ID");
               model.addColumn("Patient ID");
               model.addColumn("Appointment Date");
               model.addColumn("Status");

               // Populate the table model with data from the result set
               while (resultSet.next()) {
                    int appointmentId = resultSet.getInt("appointment_id");
                    int patientId = resultSet.getInt("patient_id");
                    String appointmentDate = resultSet.getString("appointment_date");
                    String status = resultSet.getString("status");

                    model.addRow(new Object[]{appointmentId, patientId, appointmentDate, status});
               }

               // Create appointments table with the model
               appointmentsTable = new JTable(model);
               appointmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
               appointmentsTable.setRowHeight(40); // Increase row height

               // Apply custom cell renderer to give padding and style to all columns
               appointmentsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    private final Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
                    private final Border thickBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                         JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                         cellComponent.setBorder(BorderFactory.createCompoundBorder(thickBorder, paddingBorder)); // Add padding and thick border
                         cellComponent.setHorizontalAlignment(SwingConstants.CENTER); // Center-align cell content
                         return cellComponent;
                    }
               });

               // Add table to a scroll pane and add it to the dialog
               JScrollPane scrollPane = new JScrollPane(appointmentsTable);
               getContentPane().add(scrollPane, BorderLayout.CENTER);
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               // For testing purposes, pass a sample doctor ID
               int sampleDoctorId = 2; // Provide the doctor ID
               new ViewDoctorAppointmentsDialog(sampleDoctorId).setVisible(true);
          });
     }
}
