import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ViewAppointments extends JFrame {
     private Connection connection;
     private int userId;
     private JTable appointmentsTable;

     public ViewAppointments(int userId) {
          this.userId = userId;

          setTitle("View Appointments");
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setSize(800, 400);
          setLocationRelativeTo(null);

          JPanel mainPanel = new JPanel(new BorderLayout());
          mainPanel.setBackground(Color.WHITE);

          fetchAppointments();

          JScrollPane scrollPane = new JScrollPane(appointmentsTable);
          mainPanel.add(scrollPane, BorderLayout.CENTER);

          setContentPane(mainPanel);

          applyStyling();
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

     private void applyStyling() {
          JTableHeader header = appointmentsTable.getTableHeader();
          header.setFont(new Font("Arial", Font.BOLD, 14));
          header.setBackground(Color.WHITE);
          header.setForeground(Color.BLACK);

          appointmentsTable.setFont(new Font("Arial", Font.PLAIN, 12));

          appointmentsTable.setBackground(Color.WHITE);
          appointmentsTable.setSelectionBackground(new Color(204, 255, 255));

          appointmentsTable.setIntercellSpacing(new Dimension(10, 5));
          appointmentsTable.setRowHeight(30);
          appointmentsTable.setShowGrid(true);
          appointmentsTable.setGridColor(Color.LIGHT_GRAY);
          appointmentsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

          DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
          centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
          appointmentsTable.setDefaultRenderer(Object.class, centerRenderer);
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               int sampleUserId = 6;
               new ViewAppointments(sampleUserId).setVisible(true);
          });
     }
}
