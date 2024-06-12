import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ManagePatientsDialog extends JDialog {
     private Connection connection;
     private JPanel mainPanel;
     private JTable patientsTable;
     private JButton addButton;
     private JButton updateButton;
     private JButton deleteButton;

     public ManagePatientsDialog() {
          initializeComponents();
          fetchPatients();
     }

     private void initializeComponents() {
          setTitle("Manage Patients");
          setSize(800, 600); // Set the size as needed
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null); // Center the dialog on the screen

          mainPanel = new JPanel(new BorderLayout());

          // Create buttons
          addButton = new JButton("Add");
          updateButton = new JButton("Update");
          deleteButton = new JButton("Delete");

          // Add action listeners to buttons
          addButton.addActionListener(e -> addPatient());
          updateButton.addActionListener(e -> updatePatient());
          deleteButton.addActionListener(e -> deletePatient());

          // Add buttons to a panel
          JPanel buttonPanel = new JPanel();
          buttonPanel.add(addButton);
          buttonPanel.add(updateButton);
          buttonPanel.add(deleteButton);

          // Add button panel to main panel
          mainPanel.add(buttonPanel, BorderLayout.NORTH);

          // Create patients table
          patientsTable = new JTable();
          patientsTable.setGridColor(Color.GRAY); // Set grid color
          patientsTable.setRowHeight(30); // Set row height
          patientsTable.setIntercellSpacing(new Dimension(10, 10)); // Set cell spacing

          // Set table cell renderer to customize appearance
          patientsTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

          // Add mouse listener to change row color on hover
          patientsTable.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                         int selectedRow = patientsTable.getSelectedRow();
                         if (selectedRow != -1) {
                              int patientId = (int) patientsTable.getValueAt(selectedRow, 0);
                              openUpdateDialog(patientId);
                         }
                    }
               }
          });

          JScrollPane scrollPane = new JScrollPane(patientsTable);

          // Add table to main panel
          mainPanel.add(scrollPane, BorderLayout.CENTER);

          // Set main panel as content pane
          setContentPane(mainPanel);
     }

     private void fetchPatients() {
          try {
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM patients");
               ResultSet resultSet = statement.executeQuery();

               // Get metadata to determine number of columns
               ResultSetMetaData metaData = resultSet.getMetaData();
               int columnCount = metaData.getColumnCount();

               // Create a DefaultTableModel with appropriate column names
               DefaultTableModel model = new DefaultTableModel();
               for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
               }

               // Populate patients table with data from the result set
               while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                         row[i - 1] = resultSet.getObject(i);
                    }
                    model.addRow(row);
               }

               // Set the model to the patients table
               patientsTable.setModel(model);
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     private void addPatient() {
          AddPatientDialog dialog = new AddPatientDialog(this);
          dialog.setVisible(true);
          fetchPatients(); // Refresh the table after adding a new patient
     }

     private void updatePatient() {
          int selectedRow = patientsTable.getSelectedRow();
          if (selectedRow != -1) {
               int patientId = (int) patientsTable.getValueAt(selectedRow, 0);
               openUpdateDialog(patientId);
          } else {
               JOptionPane.showMessageDialog(this, "Please select a patient to update.");
          }
     }

     private void deletePatient() {
          int selectedRow = patientsTable.getSelectedRow();
          if (selectedRow != -1) {
               int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
               if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                         int patientId = (int) patientsTable.getValueAt(selectedRow, 0);
                         PreparedStatement statement = connection.prepareStatement("DELETE FROM patients WHERE patient_id = ?");
                         statement.setInt(1, patientId);
                         int rowsAffected = statement.executeUpdate();
                         if (rowsAffected > 0) {
                              JOptionPane.showMessageDialog(this, "Patient deleted successfully");
                              fetchPatients(); // Refresh the table after deletion
                         } else {
                              JOptionPane.showMessageDialog(this, "Failed to delete patient", "Error", JOptionPane.ERROR_MESSAGE);
                         }
                    } catch (SQLException e) {
                         e.printStackTrace();
                         JOptionPane.showMessageDialog(this, "Error deleting patient", "Error", JOptionPane.ERROR_MESSAGE);
                    }
               }
          } else {
               JOptionPane.showMessageDialog(this, "Please select a patient to delete.");
          }
     }

     private void openUpdateDialog(int patientId) {
          UpdatePatientDialog dialog = new UpdatePatientDialog(this, patientId);
          dialog.setVisible(true);
          fetchPatients(); // Refresh the table after updating patient details
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
                    ((JComponent) cell).setBorder(new EmptyBorder(5, 10, 5, 10));
               }

               // Alternate row colors
               if (row % 2 == 0) {
                    cell.setBackground(Color.WHITE);
               } else {
                    cell.setBackground(Color.LIGHT_GRAY);
               }

               // Highlight selected row
               if (isSelected) {
                    cell.setBackground(Color.CYAN);
               }

               return cell;
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               new ManagePatientsDialog().setVisible(true);
          });
     }
}
