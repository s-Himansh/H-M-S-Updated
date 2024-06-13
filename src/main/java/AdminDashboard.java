import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

public class AdminDashboard extends JFrame {
    private JPanel mainPanel;
    private JButton managePatientsButton;
    private JButton manageDoctorsButton;
    private JButton manageAppointmentsButton;
    private JButton viewMedicalRecordsButton;
    private JButton manageBillingButton;
    static ResultSet user ;
    public AdminDashboard(ResultSet userDetails) {
        user = userDetails;

        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);


        managePatientsButton = createStyledButton("Manage Patients");
        managePatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // JOptionPane.showMessageDialog(AdminDashboard.this, "Manage Patients clicked");
                new ManagePatientsDialog().setVisible(true);
            }
        });

        manageDoctorsButton = createStyledButton("Manage Doctors");
        manageDoctorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new ManageDoctorsDialog().setVisible(true);
            }
        });

        manageAppointmentsButton = createStyledButton("Manage Appointments");
        manageAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new ManageAppointmentsDialog().setVisible(true);
            }
        });

        viewMedicalRecordsButton = createStyledButton("View Medical Records");
        viewMedicalRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(AdminDashboard.this, "View Medical Records clicked");
            }
        });

        manageBillingButton = createStyledButton("Manage Billing");
        manageBillingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(AdminDashboard.this, "Manage Billing clicked");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(managePatientsButton, gbc);

        gbc.gridy++;
        mainPanel.add(manageDoctorsButton, gbc);

        gbc.gridy++;
        mainPanel.add(manageAppointmentsButton, gbc);

        gbc.gridy++;
        mainPanel.add(viewMedicalRecordsButton, gbc);

        gbc.gridy++;
        mainPanel.add(manageBillingButton, gbc);

        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(143, 188, 219));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(143, 188, 219));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println(user);
                new AdminDashboard(user).setVisible(true);
            }
        });
    }
}
