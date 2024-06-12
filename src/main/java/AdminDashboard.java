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
        // Initialize components and set up the admin dashboard
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set the size as needed
        setLocationRelativeTo(null); // Center the window on the screen

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Add buttons to the dashboard
        managePatientsButton = createStyledButton("Manage Patients");
        managePatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Manage Patients window
                // JOptionPane.showMessageDialog(AdminDashboard.this, "Manage Patients clicked");
                new ManagePatientsDialog().setVisible(true);
            }
        });

        manageDoctorsButton = createStyledButton("Manage Doctors");
        manageDoctorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Manage Doctors window
                JOptionPane.showMessageDialog(AdminDashboard.this, "Manage Doctors clicked");
            }
        });

        manageAppointmentsButton = createStyledButton("Manage Appointments");
        manageAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Manage Appointments window
                JOptionPane.showMessageDialog(AdminDashboard.this, "Manage Appointments clicked");
            }
        });

        viewMedicalRecordsButton = createStyledButton("View Medical Records");
        viewMedicalRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open View Medical Records window
                JOptionPane.showMessageDialog(AdminDashboard.this, "View Medical Records clicked");
            }
        });

        manageBillingButton = createStyledButton("Manage Billing");
        manageBillingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Manage Billing window
                JOptionPane.showMessageDialog(AdminDashboard.this, "Manage Billing clicked");
            }
        });

        // Add buttons to the panel
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

        // Set the content pane
        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        button.setForeground(Color.WHITE); // Set text color
        button.setBackground(new Color(143, 188, 219)); // Set background color
        button.setFocusPainted(false); // Remove focus paint
        button.setBorderPainted(false); // Remove border paint
        button.setOpaque(true); // Make the button opaque for background color
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180)); // Change background color on hover
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(143, 188, 219)); // Restore background color on exit
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
