import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewMedicalRecordsDialog extends JDialog {
     private int doctorId;
     private JTable medicalRecordsTable;
     private JTextField patientIdFilterField;

     public ViewMedicalRecordsDialog(int doctorId) {
          this.doctorId = doctorId;
          initializeComponents();
          fetchAndDisplayMedicalRecords();
     }

     private void initializeComponents() {
          setTitle("Medical Records");
          setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          setSize(800, 600); // Adjust size as needed
          setLocationRelativeTo(null);


          JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          JLabel filterLabel = new JLabel("Filter by patient ID:");
          patientIdFilterField = new JTextField(10);
          JButton filterButton = new JButton("Filter");
          filterButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    String patientIdFilter = patientIdFilterField.getText().trim();
                    filterMedicalRecords(patientIdFilter);
               }
          });
          filterPanel.add(filterLabel);
          filterPanel.add(patientIdFilterField);
          filterPanel.add(filterButton);
          add(filterPanel, BorderLayout.NORTH);
     }

     private void fetchAndDisplayMedicalRecords() {
          try {
               Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM medical_records WHERE doctor_id = ?");
               statement.setInt(1, doctorId);
               ResultSet resultSet = statement.executeQuery();


               DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                         return columnIndex == 0 ? Integer.class : String.class;
                    }
               };
               model.addColumn("Record ID");
               model.addColumn("Patient ID");
               model.addColumn("Diagnosis");
               model.addColumn("Treatment");
               model.addColumn("Record Date");


               while (resultSet.next()) {
                    int recordId = resultSet.getInt("record_id");
                    int patientId = resultSet.getInt("patient_id");
                    String diagnosis = resultSet.getString("diagnosis");
                    String treatment = resultSet.getString("treatment");
                    String recordDate = resultSet.getString("record_date");

                    model.addRow(new Object[]{recordId, patientId, diagnosis, treatment, recordDate});
               }


               medicalRecordsTable = new JTable(model);
               medicalRecordsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
               medicalRecordsTable.setRowHeight(40);


               medicalRecordsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    private final Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 5, 10);
                    private final Border thickBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                         JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                         cellComponent.setBorder(BorderFactory.createCompoundBorder(thickBorder, paddingBorder)); // Adding thick border
                         return cellComponent;
                    }
               });


               JScrollPane scrollPane = new JScrollPane(medicalRecordsTable);
               getContentPane().add(scrollPane, BorderLayout.CENTER);
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     private void filterMedicalRecords(String patientIdFilter) {
          DefaultTableModel model = (DefaultTableModel) medicalRecordsTable.getModel();
          TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
          medicalRecordsTable.setRowSorter(sorter);
          if (patientIdFilter.length() == 0) {
               sorter.setRowFilter(null);
          } else {
               try {
                    sorter.setRowFilter(RowFilter.regexFilter(patientIdFilter));
               } catch (Exception e) {
                    e.printStackTrace();
               }
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(() -> {

               int sampleDoctorId = 2;
               new ViewMedicalRecordsDialog(sampleDoctorId).setVisible(true);
          });
     }
}
