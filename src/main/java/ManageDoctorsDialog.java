import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class ManageDoctorsDialog extends JDialog {
     private Connection connection;
     private JPanel mainPanel;
     private JTable doctorsTable;
     private JButton addButton;
     private JButton updateButton;
     private JButton deleteButton;
     private JTextField searchField;

     public ManageDoctorsDialog() {
          initializeComponents();
          fetchDoctors();
     }

     private void initializeComponents() {
          setTitle("Manage Doctors");
          setSize(800, 600); // Set the size as needed
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null); // Center the dialog on the screen

          mainPanel = new JPanel(new BorderLayout());

          // Create buttons
          addButton = new JButton("Add");
          updateButton = new JButton("Update");
          deleteButton = new JButton("Delete");

          // Add action listeners to buttons
          addButton.addActionListener(e -> addDoctor());
          updateButton.addActionListener(e -> updateDoctor());
          deleteButton.addActionListener(e -> deleteDoctor());

          // Add buttons to a panel
          JPanel buttonPanel = new JPanel();
          buttonPanel.add(addButton);
          buttonPanel.add(updateButton);
          buttonPanel.add(deleteButton);

          // Add button panel to main panel
          mainPanel.add(buttonPanel, BorderLayout.NORTH);

          // Add search field
          searchField = new JTextField();
          searchField.setPreferredSize(new Dimension(200, 25));
          searchField.setToolTipText("Search by doctor name");
          searchField.addKeyListener(new KeyAdapter() {
               @Override
               public void keyReleased(KeyEvent e) {
                    String query = searchField.getText().trim();
                    filterTable(query);
               }
          });
          searchField.addKeyListener(new KeyAdapter() {
               @Override
               public void keyReleased(KeyEvent e) {
                    String query = searchField.getText().trim();
                    filterTable(query);
               }
          });
          JPanel searchPanel = new JPanel();
          searchPanel.add(new JLabel("Search: "));
          searchPanel.add(searchField);
          mainPanel.add(searchPanel, BorderLayout.SOUTH);

          // Create doctors table
          doctorsTable = new JTable();
          doctorsTable.setGridColor(Color.GRAY); // Set grid color
          doctorsTable.setRowHeight(30); // Set row height
          doctorsTable.setIntercellSpacing(new Dimension(10, 10)); // Set cell spacing

          // Set table cell renderer to customize appearance
          doctorsTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

          // Add mouse listener to change row color on hover
          doctorsTable.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                         int selectedRow = doctorsTable.getSelectedRow();
                         if (selectedRow != -1) {
                              int doctorId = (int) doctorsTable.getValueAt(selectedRow, 0);
                              // openUpdateDialog(doctorId);
                         }
                    }
               }
          });

          JScrollPane scrollPane = new JScrollPane(doctorsTable);

          // Add table to main panel
          mainPanel.add(scrollPane, BorderLayout.CENTER);

          // Set main panel as content pane
          setContentPane(mainPanel);
     }

     private void fetchDoctors() {
          try {
               // Establish connection to the database
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");

               // Create a statement
               Statement statement = connection.createStatement();

               // Execute a query to fetch doctors
               ResultSet resultSet = statement.executeQuery("SELECT * FROM doctors");

               // Get metadata to determine number of columns
               ResultSetMetaData metaData = resultSet.getMetaData();
               int columnCount = metaData.getColumnCount();

               // Create a DefaultTableModel with appropriate column names
               DefaultTableModel model = new DefaultTableModel();
               for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
               }

               // Populate doctors table with data from the result set
               while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                         row[i - 1] = resultSet.getObject(i);
                    }
                    model.addRow(row);
               }

               // Set the model to the doctors table
               doctorsTable.setModel(model);

          } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error fetching doctors", "Error", JOptionPane.ERROR_MESSAGE);
          }
     }

     private void addDoctor() {
          AddDoctorDialog dialog = new AddDoctorDialog(this);
          dialog.setVisible(true);
          fetchDoctors();
     }

     private void updateDoctor() {
          int selectedRow = doctorsTable.getSelectedRow();
          if (selectedRow == -1) {
               JOptionPane.showMessageDialog(this, "Please select a doctor to update", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          // Get the doctor ID from the selected row
          int doctorId = (int) doctorsTable.getValueAt(selectedRow, 0);

          // Get the data of the selected doctor
          String name = (String) doctorsTable.getValueAt(selectedRow, 1);
          String specialty = (String) doctorsTable.getValueAt(selectedRow, 2);
          String contactInfo = (String) doctorsTable.getValueAt(selectedRow, 3);

          // Create an instance of the UpdateDoctorDialog and pass the data
          UpdateDoctorDialog dialog = new UpdateDoctorDialog(this, doctorId, name, specialty, contactInfo);
          dialog.setVisible(true);
          fetchDoctors(); // Refresh the table after updating
     }


     private void deleteDoctor() {
          int selectedRow = doctorsTable.getSelectedRow();
          if (selectedRow == -1) {
               JOptionPane.showMessageDialog(this, "Please select a doctor to delete", "Error", JOptionPane.ERROR_MESSAGE);
               return;
          }

          // Get the doctor ID from the selected row
          int doctorId = (int) doctorsTable.getValueAt(selectedRow, 0);

          int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
               try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma")) {
                    // Create a prepared statement to delete the doctor
                    String query = "DELETE FROM doctors WHERE doctor_id = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, doctorId);

                    // Execute the statement
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                         JOptionPane.showMessageDialog(this, "Doctor deleted successfully");
                         fetchDoctors(); // Refresh the table after deletion
                    } else {
                         JOptionPane.showMessageDialog(this, "Failed to delete doctor", "Error", JOptionPane.ERROR_MESSAGE);
                    }
               } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting doctor from the database", "Error", JOptionPane.ERROR_MESSAGE);
               }
          }
     }

     private void filterTable(String query) {
          DefaultTableModel model = (DefaultTableModel) doctorsTable.getModel();
          TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
          doctorsTable.setRowSorter(sorter);

          if (query.trim().length() == 0) {
               sorter.setRowFilter(null);
          } else {
               sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
          }
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
                    ((JComponent) cell).setBorder(new EmptyBorder(5, 10,5, 5));
               }

               // Alternate row colors
                    cell.setBackground(Color.WHITE);

               // Highlight selected row
               if (isSelected) {
                    cell.setBackground(new Color(143, 188, 219));
               }

               return cell;
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {
               new ManageDoctorsDialog().setVisible(true);
          });
     }
}
