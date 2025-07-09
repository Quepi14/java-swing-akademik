package akademik.gui;

import akademik.gui.panels.DashboardPanel;
import akademik.gui.panels.DosenPanel;
import akademik.gui.panels.MahasiswaPanel;
import akademik.util.ColorConstants;
import akademik.util.ComponentFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarPanel extends JPanel {
    public SidebarPanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorConstants.SIDEBAR_COLOR);
        setPreferredSize(new Dimension(200, 0));
        setBorder(new EmptyBorder(20, 0, 20, 0));

        // Logo/Title
        JLabel logo = new JLabel("UMSIDA", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(logo);

        String[] menuItems = {
            "DASHBOARD",
            "TOTAL MAHASISWA",
            "TOTAL DOSEN"
        };

        for (String item : menuItems) {
            JButton button = ComponentFactory.createSidebarButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(200, 50));

            button.addActionListener(e -> {
                switch (item) {
                    case "DASHBOARD":
                        mainFrame.showPanel(new DashboardPanel());
                        break;
                    case "TOTAL MAHASISWA":
                        mainFrame.showPanel(new MahasiswaPanel());
                        break;
                    case "TOTAL DOSEN":
                        mainFrame.showPanel(new DosenPanel());
                        break;
                }
            });

            add(button);
            add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }
}