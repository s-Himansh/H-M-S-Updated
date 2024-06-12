import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginScreen extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String role;

    public LoginScreen(String role) {
        this.role = role;

        // Initialize components
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Add padding
        mainPanel.setBackground(Color.WHITE); // Set background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add labels for input fields
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        usernameField = createStyledTextField();
        mainPanel.add(usernameField, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        passwordField = createStyledPasswordField();
        mainPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        loginButton = createStyledButton("Login");
        mainPanel.add(loginButton, gbc);

        // Set the content pane
        setContentPane(mainPanel);
        setTitle(role + " Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Pack the frame to preferred size
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20); // Set preferred width
        textField.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20); // Set preferred width
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font
        button.setForeground(Color.WHITE); // Set text color
        button.setBackground(new Color(30, 144, 255)); // Set background color
        button.setFocusPainted(false); // Remove focus paint
        button.setBorderPainted(false); // Remove border paint
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Open dashboard according to role
                    switch (role) {
                        case "Admin":
                            new AdminDashboard().setVisible(true);
                            break;
                        case "Doctor":
                            new DoctorDashboard().setVisible(true);
                            break;
                        case "Nurse":
                            new NurseDashboard().setVisible(true);
                            break;
                        case "Patient":
                            new PatientDashboard().setVisible(true);
                            break;
                    }
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    private boolean authenticate(String username, String password) {
        // Database authentication logic
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND PASSWORD = '" + password + "' AND role = '" + role.toLowerCase() + "'");
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen("Admin").setVisible(true);
            }
        });
    }
}
