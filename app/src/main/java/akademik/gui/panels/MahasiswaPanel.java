package akademik.gui.panels;

import javax.swing.*;
import java.awt.*;

public class MahasiswaPanel extends JPanel {
    public MahasiswaPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Panel Data Mahasiswa", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
