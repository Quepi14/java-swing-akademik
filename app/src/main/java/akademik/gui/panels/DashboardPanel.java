package akademik.gui.panels;

import akademik.service.AdminService;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final JTabbedPane tabbedPane;
    
    public DashboardPanel(boolean isAdmin, AdminService adminService) {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Tab Beranda (Wajib ada untuk semua role)
        tabbedPane.addTab("Beranda", createHomePanel());
        
        // Tambahkan AdminPanel HANYA jika user adalah admin
        if (isAdmin) {
            tabbedPane.addTab("Manajemen User", new AdminPanel(adminService));
        }
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Konten beranda yang sama untuk semua role
        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Akademik", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        
        return panel;
    }
}