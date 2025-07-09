package akademik.gui;

import akademik.gui.panels.DashboardPanel;
import akademik.util.ColorConstants;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Dashboard UMSIDA");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        SidebarPanel sidebar = new SidebarPanel(this);
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        showPanel(new DashboardPanel());
    }

    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}