package akademik.gui.panels;

import akademik.model.Mahasiswa;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

public class MahasiswaPanel extends JPanel {
    private final Mahasiswa mahasiswa;

    public MahasiswaPanel(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        initUI();
        loadDataMahasiswa();
    }

    private void initUI() {
        setLayout(new MigLayout("wrap, fillx, insets 20", "[grow]", "[][grow]"));

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("fillx"));
        JLabel titleLabel = new JLabel("PROFIL MAHASISWA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "foreground: #2c3e50");
        
        headerPanel.add(titleLabel);
        add(headerPanel, "growx, wrap");

        // TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel Profil
        JPanel profilPanel = createProfilePanel();
        
        // Panel Nilai
        JPanel nilaiPanel = new JPanel(new BorderLayout());
        nilaiPanel.add(createGradeTable(), BorderLayout.CENTER);
        
        tabbedPane.addTab("Profil", profilPanel);
        tabbedPane.addTab("Nilai", nilaiPanel);
        
        add(tabbedPane, "grow, push");
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 2, fillx, insets 20", 
            "[right][left]", "[]10[]"));
        
        panel.add(new JLabel("NIM:"));
        panel.add(new JLabel(mahasiswa.getNim()), "growx");
        
        panel.add(new JLabel("Nama Lengkap:"));
        panel.add(new JLabel(mahasiswa.getNamaLengkap()), "growx");
        
        panel.add(new JLabel("Jenis Kelamin:"));
        panel.add(new JLabel(mahasiswa.getJenisKelamin().toString()), "growx");
        
        panel.add(new JLabel("Dosen Wali:"));
        panel.add(new JLabel("Dr. Lesant, S.Sam., M.Sam."), "growx"); 
        
        panel.add(new JLabel("Email:"));
        panel.add(new JLabel(mahasiswa.getEmail()), "growx");
        
        return panel;
    }

    private JComponent createGradeTable() {
        String[] columns = {"Kode MK", "Mata Kuliah", "SKS", "Nilai Akhir", "Grade"};
        Object[][] data = {}; // Will be populated from database
        
        JTable table = new JTable(data, columns);
        customizeTable(table);
        
        return new JScrollPane(table);
    }

    private void customizeTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
    }

    private void loadDataMahasiswa() {
        // Implementation to load student data
        // Now uses the mahasiswa field that was passed in constructor
    }
}