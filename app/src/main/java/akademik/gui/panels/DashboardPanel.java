package akademik.gui.panels;

import akademik.service.DosenService;
import akademik.service.MahasiswaService;
import akademik.util.ColorConstants;
import akademik.util.ComponentFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private MahasiswaService mahasiswaService;
    private DosenService dosenService;
    
    public DashboardPanel() {
        this.mahasiswaService = new MahasiswaService();
        this.dosenService = new DosenService();
        
        setLayout(new BorderLayout());
        setBackground(ColorConstants.BACKGROUND_COLOR);
        
        JLabel header = ComponentFactory.createHeader("Dashboard");
        
        JPanel stats = new JPanel(new GridLayout(1, 2, 30, 0));
        stats.setBackground(ColorConstants.BACKGROUND_COLOR);
        stats.setBorder(new EmptyBorder(50, 100, 50, 100));

        stats.add(ComponentFactory.createCard(
            String.valueOf(mahasiswaService.getTotalMahasiswa()), 
            "Total Mahasiswa"
        ));
        stats.add(ComponentFactory.createCard(
            String.valueOf(dosenService.getTotalDosen()), 
            "Total Dosen"
        ));

        add(header, BorderLayout.NORTH);
        add(stats, BorderLayout.CENTER);
    }
}