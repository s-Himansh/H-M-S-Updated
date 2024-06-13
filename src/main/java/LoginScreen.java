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


        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Add padding
        mainPanel.setBackground(Color.WHITE); // Set background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


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
        setLocationRelativeTo(null);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 144, 255));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                ResultSet userDetails = authenticate(username, password);
                if (userDetails != null) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    switch (role) {
                        case "Admin":
                            new AdminDashboard(userDetails).setVisible(true);
                            break;
                        case "Doctor":
                            new DoctorDashboard(userDetails).setVisible(true);
                            break;
                        case "Nurse":
                            // new NurseDashboard(userDetails).setVisible(true);
                            break;
                        case "Patient":
                            new PatientDashboard(userDetails).setVisible(true);
                            break;
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    private ResultSet authenticate(String username, String password) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hms", "root", "sharma");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND PASSWORD = '" + password + "' AND role = '" + role.toLowerCase() + "'");
            if (rs.next()) {
                return rs; // Return the ResultSet containing user details
            } else {
                return null; // Invalid credentials
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
