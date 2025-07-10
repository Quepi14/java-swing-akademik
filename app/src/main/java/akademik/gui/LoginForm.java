package akademik.gui;

import akademik.util.ColorConstants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {

    public LoginForm() {
        setTitle("Login Sistem Akademik UMSIDA");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorConstants.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("SISTEM AKADEMIK UMSIDA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setOpaque(true);
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setOpaque(true);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 10, 10, 10);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(ColorConstants.PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(ColorConstants.BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(ColorConstants.PRIMARY_COLOR);
            }
        });

        formPanel.add(loginButton, gbc);

        // Login Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username dan password harus diisi!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            try {
                currentUser.login(token);
                String principal = (String) currentUser.getPrincipal();
                String[] parts = principal.split(":");
                String usernameOnly = parts[0];
                String role = parts[1];

                if (role.equalsIgnoreCase("admin")) {
                    JOptionPane.showMessageDialog(this,
                            "Login berhasil sebagai Admin: " + usernameOnly,
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    new MainFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Akses ditolak! Hanya admin yang diizinkan login ke sistem ini.",
                            "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
                    currentUser.logout();
                }

            } catch (UnknownAccountException | IncorrectCredentialsException ex) {
                JOptionPane.showMessageDialog(this,
                        "Login gagal: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (AuthenticationException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error sistem: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Support enter key
        getRootPane().setDefaultButton(loginButton);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }
}
