import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoleSelection extends JFrame {
    private JPanel mainPanel;
    private JButton adminButton;
    private JButton doctorButton;
    private JButton nurseButton;
    private JButton patientButton;

    public RoleSelection() {
        // Initializing the main panel and buttons
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Add horizontal and vertical gaps

        adminButton = createStyledButton("Admin");
        doctorButton = createStyledButton("Doctor");
        nurseButton = createStyledButton("Nurse");
        patientButton = createStyledButton("Patient");

        // Add buttons to the main panel
        mainPanel.add(adminButton);
        mainPanel.add(doctorButton);
        mainPanel.add(nurseButton);
        mainPanel.add(patientButton);


        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); // Adjust padding for larger screens

        setContentPane(mainPanel);
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "UR CARE"));
        setTitle("Role Selection");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addActionListeners(adminButton, "Admin");
        addActionListeners(doctorButton, "Doctor");
        addActionListeners(nurseButton, "Nurse");
        addActionListeners(patientButton, "Patient");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Hack", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));

        // Add hover effect
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

    private void addActionListeners(JButton button, String role) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginScreen(role);
            }
        });
    }

    private void openLoginScreen(String role) {
        new LoginScreen(role).setVisible(true);
            dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RoleSelection().setVisible(true);
            }
        });
    }
}
