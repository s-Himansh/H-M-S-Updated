import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ManageAppointmentsDialog extends JDialog {
     private Connection connection;
     private JTable appointmentsTable;
     private JButton updateButton;
     private JButton deleteButton;
     private JTextField searchField;

     public ManageAppointmentsDialog() {
          initializeComponents();
          fetchAppointments();
     }

     private void initializeComponents() {
          setTitle("Manage Appointments");
          setSize(800, 600);
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);

          JPanel mainPanel = new JPanel(new BorderLayout());
          mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

          // Create button panel for top
          JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          updateButton = new JButton("Update");
          deleteButton = new JButton("Delete");

          topButtonPanel.add(updateButton);
          topButtonPanel.add(deleteButton);

          mainPanel.add(topButtonPanel, BorderLayout.NORTH);

          appointmentsTable = new JTable();
          JScrollPane scrollPane = new JScrollPane(appointmentsTable);
          appointmentsTable.setFillsViewportHeight(true); // Fill the entire height of the scroll pane

          // Apply styling to the table
          appointmentsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add thick border
          appointmentsTable.setRowHeight(30); // Set row height
          appointmentsTable.setIntercellSpacing(new Dimension(10, 10)); // Set cell spacing

          // Custom table cell renderer to style the table
          appointmentsTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

          mainPanel.add(scrollPane, BorderLayout.CENTER);

          // Create search panel for bottom
          JPanel bottomSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          searchField = new JTextField(20);
          JButton searchButton = new JButton("Search");

          bottomSearchPanel.add(searchField);
          bottomSearchPanel.add(searchButton);

          mainPanel.add(bottomSearchPanel, BorderLayout.SOUTH);

          // Add action listeners
          updateButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    updateAppointment();
               }
          });

          deleteButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    deleteAppointment();
               }
          });

          searchButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    String query = searchField.getText().trim();
                    searchAppointments(query);
               }
          });

          setContentPane(mainPanel);
     }

     // Custom table cell renderer to style the table
     private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
          public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
               Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

               // Set font and alignment
               setFont(new Font("Arial", Font.PLAIN, 14));
               setHorizontalAlignment(SwingConstants.CENTER);

               // Set padding for cells
               if (cell instanceof JComponent) {
                    ((JComponent) cell).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
               }

               // Alternate row colors
               if (row % 2 == 0) {
                    cell.setBackground(new Color(240, 240, 240)); // Light gray background for even rows
               } else {
                    cell.setBackground(Color.WHITE); // White background for odd rows
               }

               // Highlight selected row
               if (isSelected) {
                    cell.setBackground(new Color(143, 188, 219)); // Light blue background for selected row
               }

               return cell;
          }
     }



     private void fetchAppointments() {
          try {
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT patient_id, doctor_id, appointment_date, status FROM appointments");

               ResultSetMetaData metaData = resultSet.getMetaData();
               int columnCount = metaData.getColumnCount();

               DefaultTableModel model = new DefaultTableModel();
               for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
               }

               while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                         row[i - 1] = resultSet.getObject(i);
                    }
                    model.addRow(row);
               }

               appointmentsTable.setModel(model);

          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error fetching appointments", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     private void updateAppointment() {
          int selectedRow = appointmentsTable.getSelectedRow();
          if (selectedRow == -1) {
               JOptionPane.showMessageDialog(this, "Please select an appointment to update", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          try {
               // Get the appointment ID from the selected row
               Object appointmentIdObject = appointmentsTable.getValueAt(selectedRow, 0);
               if (appointmentIdObject == null) {
                    JOptionPane.showMessageDialog(this, "Invalid appointment ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
               }
               int appointmentId = Integer.parseInt(appointmentIdObject.toString());

               // Retrieve the appointment details from the table
               String patientId = "" + appointmentsTable.getValueAt(selectedRow, 0);
               String doctorId = "" + appointmentsTable.getValueAt(selectedRow, 1);
               String appointmentDate = "" + appointmentsTable.getValueAt(selectedRow, 2);
               String status = "" + appointmentsTable.getValueAt(selectedRow, 3);

               // Open a dialog to allow the user to update the appointment
               UpdateAppointmentDialog dialog = new UpdateAppointmentDialog(this, patientId, doctorId, appointmentDate, status, appointmentId);
               dialog.setVisible(true);

               // Refresh the table after updating
               fetchAppointments();
          } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(this, "Error parsing appointment ID", "Error", JOptionPane.ERROR_MESSAGE);
          } catch (Exception ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error updating appointment", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }




     private void deleteAppointment() {
          int selectedRow = appointmentsTable.getSelectedRow();
          if (selectedRow == -1) {
               JOptionPane.showMessageDialog(this, "Please select an appointment to delete", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
          if (confirmDelete == JOptionPane.YES_OPTION) {
               try {
                    // Get the appointment ID from the selected row
                    Object appointmentIdObject = appointmentsTable.getValueAt(selectedRow, 0);
                    if (appointmentIdObject == null) {
                         JOptionPane.showMessageDialog(this, "Invalid appointment ID", "Error", JOptionPane.ERROR_MESSAGE);
                         return;
                    }
                    int appointmentId = Integer.parseInt(appointmentIdObject.toString());

                    // Perform the deletion in the database
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM appointments WHERE appointment_id = ?");
                    statement.setInt(1, appointmentId);
                    int rowsDeleted = statement.executeUpdate();

                    if (rowsDeleted > 0) {
                         JOptionPane.showMessageDialog(this, "Appointment deleted successfully");
                         // Refresh the table after deletion
                         fetchAppointments();
                    } else {
                         JOptionPane.showMessageDialog(this, "Failed to delete appointment", "Error", JOptionPane.ERROR_MESSAGE);
                    }
               } catch (NumberFormatException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting appointment", "Error", JOptionPane.ERROR_MESSAGE);
               }
          }
     }


     private void searchAppointments(String query) {
          try {
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT patient_id, doctor_id, appointment_date, status FROM appointments WHERE CONCAT(patient_id, doctor_id, appointment_date, status) LIKE ?");
               statement.setString(1, "%" + query + "%");
               ResultSet resultSet = statement.executeQuery();

               ResultSetMetaData metaData = resultSet.getMetaData();
               int columnCount = metaData.getColumnCount();

               DefaultTableModel model = new DefaultTableModel();
               for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
               }

               while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                         row[i - 1] = resultSet.getObject(i);
                    }
                    model.addRow(row);
               }

               appointmentsTable.setModel(model);

          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error searching appointments", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }


     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               new ManageAppointmentsDialog().setVisible(true);
          });
     }
}
