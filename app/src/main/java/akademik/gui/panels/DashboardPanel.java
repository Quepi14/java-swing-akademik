package akademik.gui.panels;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Selamat Datang di Dashboard", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
