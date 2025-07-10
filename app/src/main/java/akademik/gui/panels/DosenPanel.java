package akademik.gui.panels;

import javax.swing.*;
import java.awt.*;

public class DosenPanel extends JPanel {
    public DosenPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Panel Data Dosen", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
