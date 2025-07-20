package akademik.gui;

import akademik.model.Admin;
import akademik.model.Dosen;
import akademik.model.Mahasiswa;
import akademik.gui.panels.*;
import akademik.service.AdminService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private final Admin user;
    private final AdminService adminService;

    public MainFrame(Admin user, AdminService adminService) {
        this.user = user;
        this.adminService = adminService;
        initializeFrame();
        setupLayout();
        showRoleSpecificPanel();
    }

    private void initializeFrame() {
        setTitle("Academic System - " + user.getRole().toUpperCase());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupLayout() {
        // Updated to pass adminService to SidebarPanel
        SidebarPanel sidebar = new SidebarPanel(this, user, adminService);
        add(sidebar, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private void showRoleSpecificPanel() {
        boolean isAdmin = "admin".equalsIgnoreCase(user.getRole());
        
        switch (user.getRole().toLowerCase()) {
            case "admin" -> showPanel(new DashboardPanel(isAdmin, adminService));
            case "dosen" -> {
                Dosen dosen = convertAdminToDosen(user);
                showPanel(new DosenPanel(dosen));
            }
            case "mahasiswa" -> {
                Mahasiswa mahasiswa = convertAdminToMahasiswa(user);
                showPanel(new MahasiswaPanel(mahasiswa));
            }
            default -> JOptionPane.showMessageDialog(this, 
                    "Unknown role: " + user.getRole(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Dosen convertAdminToDosen(Admin admin) {
        return new Dosen(
            admin.getId(),
            "NIDN" + admin.getId(),
            admin.getUsername(),
            null,
            "",
            "",
            admin.getEmail(),
            admin.getUsername(),
            "",
            "active",
            null
        );
    }

    private Mahasiswa convertAdminToMahasiswa(Admin admin) {
        return new Mahasiswa(
            admin.getId(),
            "NIM" + admin.getId(),
            admin.getUsername(),
            null,
            "",
            "",
            admin.getEmail(),
            admin.getUsername(),
            "",
            "active",
            null,
            1
        );
    }

    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}