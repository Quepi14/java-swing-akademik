package akademik.gui.panels;

import akademik.model.Admin;
import akademik.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class AdminPanel extends JPanel {
    private final AdminService adminService;
    private final DefaultTableModel tableModel;
    private final JTable userTable;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JComboBox<String> roleComboBox;
    private final JCheckBox activeCheckbox;

    public AdminPanel(AdminService adminService) {
        this.adminService = adminService;
        this.tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Role", "Status"}, 0);
        this.userTable = new JTable(tableModel);
        this.usernameField = new JTextField(20);
        this.passwordField = new JPasswordField(20);
        this.confirmPasswordField = new JPasswordField(20);
        this.roleComboBox = new JComboBox<>(new String[]{"admin", "dosen", "mahasiswa"});
        this.activeCheckbox = new JCheckBox("Active");

        checkAdminAuthorization();
        initializeUI();
        loadAdminData();
    }

    @RequiresRoles("admin")
    private void checkAdminAuthorization() {
        try {
            SecurityUtils.getSubject().checkRole("admin");
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null,
                    "Access Denied: Only admin can access this panel",
                    "Authorization Error", JOptionPane.ERROR_MESSAGE);
            throw new SecurityException("Unauthorized access", e);
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Table setup
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Admin Management"));
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Confirm Password:"));
        formPanel.add(confirmPasswordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(activeCheckbox);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton("Add", this::handleAddAdmin));
        buttonPanel.add(createButton("Update", this::handleUpdateAdmin));
        buttonPanel.add(createButton("Delete", this::handleDeleteAdmin));
        buttonPanel.add(createButton("Reset", e -> clearForm()));

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        add(mainPanel);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void loadAdminData() {
        try {
            tableModel.setRowCount(0);
            List<Admin> admins = adminService.getAllAdmins();
            for (Admin admin : admins) {
                tableModel.addRow(new Object[]{
                        admin.getId(),
                        admin.getUsername(),
                        admin.getRole(),
                        admin.isActive() ? "Active" : "Inactive"
                });
            }
        } catch (IllegalStateException e) {
            showError("Failed to load admin data: " + e.getMessage());
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            roleComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            activeCheckbox.setSelected("Active".equals(tableModel.getValueAt(selectedRow, 3)));
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    private void handleAddAdmin(ActionEvent event) {
        JButton sourceButton = (JButton) event.getSource();
        sourceButton.setEnabled(false); // Disable button during processing
        
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            boolean isActive = activeCheckbox.isSelected();

            if (username.isEmpty() || password.isEmpty()) {
                showError("Username and password are required");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            if (adminService.usernameExists(username)) {
                showError("Username already exists");
                return;
            }

            Admin newAdmin = new Admin();
            newAdmin.setUsername(username);
            newAdmin.setPassword(password);
            newAdmin.setRole(role);
            newAdmin.setNamaLengkap("");
            newAdmin.setEmail("");
            newAdmin.setTanggalDibuat(LocalDate.now());
            newAdmin.setStatusAkun(isActive ? "active" : "inactive");

            if (adminService.tambahAdmin(newAdmin)) {
                loadAdminData();
                clearForm();
                showSuccess("Admin added successfully");
            } else {
                showError("Failed to add admin");
            }
        } catch (IllegalArgumentException e) {
            showError("Invalid input: " + e.getMessage());
        } catch (IllegalStateException e) {
            showError("Operation failed: " + e.getMessage());
        } finally {
            sourceButton.setEnabled(true); // Re-enable button
        }
    }

    private void handleUpdateAdmin(ActionEvent event) {
        JButton sourceButton = (JButton) event.getSource();
        sourceButton.setEnabled(false);
        
        try {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow < 0) {
                showError("Please select an admin to update");
                return;
            }

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            boolean isActive = activeCheckbox.isSelected();

            if (username.isEmpty()) {
                showError("Username is required");
                return;
            }

            if (!password.isEmpty() && !password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            Admin existingAdmin = adminService.getAdminByUsername(username);
            int adminId = (int) tableModel.getValueAt(selectedRow, 0);
            if (existingAdmin != null && existingAdmin.getId() != adminId) {
                showError("Username already exists");
                return;
            }

            Admin updatedAdmin = new Admin();
            updatedAdmin.setId(adminId);
            updatedAdmin.setUsername(username);
            updatedAdmin.setRole(role);
            updatedAdmin.setStatusAkun(isActive ? "active" : "inactive");
            
            if (!password.isEmpty()) {
                updatedAdmin.setPassword(password);
            }

            if (adminService.updateAdmin(updatedAdmin)) {
                loadAdminData();
                clearForm();
                showSuccess("Admin updated successfully");
            } else {
                showError("Failed to update admin");
            }
        } catch (IllegalArgumentException e) {
            showError("Invalid input: " + e.getMessage());
        } catch (IllegalStateException e) {
            showError("Operation failed: " + e.getMessage());
        } finally {
            sourceButton.setEnabled(true);
        }
    }

    private void handleDeleteAdmin(ActionEvent event) {
        JButton sourceButton = (JButton) event.getSource();
        sourceButton.setEnabled(false);
        
        try {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow < 0) {
                showError("Please select an admin to delete");
                return;
            }

            int adminId = (int) tableModel.getValueAt(selectedRow, 0);
            String username = tableModel.getValueAt(selectedRow, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete admin '" + username + "'?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION && adminService.deleteAdmin(adminId)) {
                loadAdminData();
                clearForm();
                showSuccess("Admin deleted successfully");
            }
        } catch (IllegalStateException e) {
            showError("Operation failed: " + e.getMessage());
        } finally {
            sourceButton.setEnabled(true);
        }
    }

    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleComboBox.setSelectedIndex(0);
        activeCheckbox.setSelected(true);
        userTable.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}