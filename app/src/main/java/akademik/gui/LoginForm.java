package akademik.gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import akademik.config.ShiroConfig;
import akademik.data.UserDatabase;
import akademik.model.Admin;
import akademik.service.AdminService;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDatabase userDb;
    private AdminService adminService;

    public LoginForm() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        try {
            ShiroConfig.initializeShiro();
            this.userDb = new UserDatabase();
            this.adminService = new AdminService();
            initComponents();
            setupFrame();
        } catch (Exception e) {
            showErrorAndExit("System initialization failed: " + e.getMessage());
        }
    }

    private void setupFrame() {
        setTitle("Login - Sistem Akademik");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("SISTEM AKADEMIK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 30, 0);
        mainPanel.add(titleLabel, gbc);
        
        // Form fields
        addFormField(mainPanel, gbc, "Username:", usernameField, 1);
        addFormField(mainPanel, gbc, "Password:", passwordField, 2);
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        getRootPane().setDefaultButton(loginButton);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        
        if (username.isEmpty() || passwordChars.length == 0) {
            showError("Please enter username and password");
            return;
        }
        
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(new UsernamePasswordToken(username, new String(passwordChars)));
            
            Admin user = userDb.getUserByUsername(username);
            if (user == null) {
                throw new AuthenticationException("User data not found");
            }
            
            openDashboard(user);
            
        } catch (AuthenticationException ex) {
            handleAuthenticationError(ex);
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        } finally {
            Arrays.fill(passwordChars, '\0');
            passwordField.setText("");
        }
    }

    private void handleAuthenticationError(AuthenticationException ex) {
        String errorMessage = "Authentication failed: ";
        if (ex == null) {
            errorMessage += "Unknown error";
        } else if (ex instanceof UnknownAccountException) {
            errorMessage += "Invalid username";
        } else if (ex instanceof IncorrectCredentialsException) {
            errorMessage += "Wrong password";
        } else if (ex instanceof LockedAccountException) {
            errorMessage += "Account locked";
        } else if (ex instanceof DisabledAccountException) {
            errorMessage += "Account disabled";
        } else if (ex instanceof ExcessiveAttemptsException) {
            errorMessage += "Too many failed attempts";
        } else {
            errorMessage += ex.getMessage() != null ? ex.getMessage() : "Unknown authentication error";
        }
        showError(errorMessage);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showErrorAndExit(String message) {
        Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, message);
        showError(message);
        System.exit(1);
    }

    private void openDashboard(Admin user) {
        if (user == null) {
            showError("Invalid user data");
            return;
        }
        
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            try {
                String role = user.getRole() != null ? user.getRole().toLowerCase() : "";
                switch (role) {
                    case "admin" -> new MainFrame(user, adminService).setVisible(true);
                    case "dosen", "mahasiswa" -> showError("Only admin can access this system");
                    default -> showErrorAndExit("Unknown role: " + user.getRole());
                }
            } catch (Exception ex) {
                showErrorAndExit("Failed to open dashboard: " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new LoginForm().setVisible(true);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, "Application startup failed", ex);
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + ex.getMessage(),
                    "Fatal Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}