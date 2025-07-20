package akademik.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import akademik.gui.panels.*;
import akademik.model.Admin;
import akademik.model.Dosen;
import akademik.model.Mahasiswa;
import akademik.service.AdminService;
import akademik.util.ColorConstants;
import akademik.util.ComponentFactory;

public class SidebarPanel extends JPanel {
    private final Map<String, JPanel> panelCache = new HashMap<>();
    private final MainFrame mainFrame;
    private final Admin admin;
    private final AdminService adminService;

    public SidebarPanel(MainFrame mainFrame, Admin admin, AdminService adminService) {
        this.mainFrame = mainFrame;
        this.admin = admin;
        this.adminService = adminService;
        
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorConstants.SIDEBAR_COLOR);
        setPreferredSize(new Dimension(200, 0));
        setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel logo = new JLabel("UMSIDA", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(logo);

        add(Box.createVerticalGlue());

        String[] menuItems = {"DASHBOARD", "MAHASISWA", "DOSEN"};

        for (String menuLabel : menuItems) {
            addMenuButton(menuLabel);
        }

        add(Box.createVerticalGlue());
    }

    private void addMenuButton(String menuLabel) {
        JButton button = ComponentFactory.createSidebarButton(menuLabel);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));

        button.addActionListener(e -> showPanel(menuLabel));
        
        add(button);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void showPanel(String menuLabel) {
        JPanel panelToShow = panelCache.computeIfAbsent(menuLabel, this::createPanelForMenu);
        mainFrame.showPanel(panelToShow);
    }

    private JPanel createPanelForMenu(String menuLabel) {
        boolean isAdmin = "admin".equalsIgnoreCase(admin.getRole());
        
        return switch (menuLabel) {
            case "DASHBOARD" -> new DashboardPanel(isAdmin, adminService);
            case "MAHASISWA" -> new MahasiswaPanel(convertToMahasiswa(admin));
            case "DOSEN" -> new DosenPanel(convertToDosen(admin));
            default -> new JPanel();
        };
    }

    private Mahasiswa convertToMahasiswa(Admin admin) {
        return new Mahasiswa(
            admin.getId(),
            "NIM" + admin.getId(),
            admin.getUsername(),
            null, // jenisKelamin
            "", // alamat
            "", // noTelp
            admin.getEmail(),
            admin.getUsername(),
            "", // password
            "active",
            null, // tanggalMasuk
            1 // idDosenWali
        );
    }

    private Dosen convertToDosen(Admin admin) {
        return new Dosen(
            admin.getId(),
            "NIDN" + admin.getId(),
            admin.getUsername(),
            null, // jenisKelamin
            "", // alamat
            "", // noTelp
            admin.getEmail(),
            admin.getUsername(),
            "", // password
            "active",
            null // tanggalBergabung
        );
    }
}