package akademik.gui.panels;

import akademik.model.Dosen;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

public class DosenPanel extends JPanel {
    private JTable nilaiTable;
    private final Dosen dosen;
    
    public DosenPanel(Dosen dosen) {
        this.dosen = dosen;
        initUI();
        loadDataDosen();
    }

    private void initUI() {
        setLayout(new MigLayout("wrap, fillx, insets 20", "[grow]", "[][grow]"));

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("fillx"));
        JLabel titleLabel = new JLabel("KELAS SAYA - " + dosen.getNamaLengkap()); // Changed to getNamaLengkap()
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "foreground: #2c3e50");
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshData());

        headerPanel.add(titleLabel, "gapbottom 10, wrap");
        headerPanel.add(refreshBtn, "right");
        add(headerPanel, "growx, wrap");

        // TabbedPane untuk kelas dan nilai
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel Kelas
        JPanel kelasPanel = new JPanel(new BorderLayout());
        kelasPanel.add(createClassTable(), BorderLayout.CENTER);
        
        // Panel Input Nilai
        JPanel nilaiPanel = new JPanel(new BorderLayout());
        nilaiPanel.add(createGradeForm(), BorderLayout.NORTH);
        nilaiPanel.add(createGradeTable(), BorderLayout.CENTER);
        
        tabbedPane.addTab("Kelas Saya", kelasPanel);
        tabbedPane.addTab("Input Nilai", nilaiPanel);
        
        add(tabbedPane, "grow, push");
    }

    private JComponent createClassTable() {
        String[] columns = {"Kode MK", "Mata Kuliah", "SKS", "Kelas", "Jumlah Mahasiswa"};
        Object[][] data = getClassDataForDosen(); // Get data based on lecturer ID
        
        JTable table = new JTable(data, columns);
        customizeTable(table);
        
        return new JScrollPane(table);
    }

    private JComponent createGradeForm() {
        JPanel panel = new JPanel(new MigLayout("wrap 3, fillx, insets 10", "[][grow][]", "[]"));
        
        JComboBox<String> kelasCombo = new JComboBox<>(getKelasOptions());
        JComboBox<String> mahasiswaCombo = new JComboBox<>();
        JTextField nilaiField = new JTextField(5);
        JButton saveBtn = new JButton("Simpan Nilai");
        
        // Add listener to update students when class changes
        kelasCombo.addActionListener(e -> {
            String selectedKelas = (String) kelasCombo.getSelectedItem();
            updateMahasiswaCombo(mahasiswaCombo, selectedKelas);
        });
        
        panel.add(new JLabel("Pilih Kelas:"));
        panel.add(kelasCombo, "growx");
        panel.add(new JLabel(""));
        panel.add(new JLabel("Mahasiswa:"));
        panel.add(mahasiswaCombo, "growx");
        panel.add(new JLabel(""));
        panel.add(new JLabel("Nilai:"));
        panel.add(nilaiField);
        panel.add(saveBtn, "gapleft push");
        
        saveBtn.addActionListener(e -> {
            String kelas = (String) kelasCombo.getSelectedItem();
            String nim = (String) mahasiswaCombo.getSelectedItem();
            String nilai = nilaiField.getText();
            saveGrade(kelas, nim, nilai);
        });
        
        return panel;
    }

    private JComponent createGradeTable() {
        String[] columns = {"NIM", "Nama", "UTS", "UAS", "Nilai Akhir", "Grade"};
        Object[][] data = getNilaiDataForDosen(); // Get data based on lecturer ID
        
        nilaiTable = new JTable(data, columns);
        customizeTable(nilaiTable);
        
        return new JScrollPane(nilaiTable);
    }

    private void customizeTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
    }

    private void loadDataDosen() {
        // Load initial data for the lecturer
        refreshData();
    }

    private void refreshData() {
        // Refresh both tables
        refreshClassTable();
        refreshNilaiTable();
    }

    private void refreshClassTable() {
        // Implementation to refresh class table
    }

    private void refreshNilaiTable() {
        // Implementation to refresh grade table
    }

    private Object[][] getClassDataForDosen() {
        // Implement to get class data for this.dosen.getId()
        return new Object[][]{}; // Placeholder
    }

    private String[] getKelasOptions() {
        // Implement to get class options for this.dosen.getId()
        return new String[]{}; // Placeholder
    }

    private void updateMahasiswaCombo(JComboBox<String> combo, String kelas) {
        combo.removeAllItems();
        
        try {
            // Use the kelas parameter to fetch students for that specific class
            // Example implementation (you'll need to replace with your actual database call):
            // List<Mahasiswa> students = Database.getMahasiswaByKelasAndDosen(kelas, this.dosen.getId());
            
            // Mock data for demonstration - replace with actual database call
            String[] students = {};
            if (kelas != null && !kelas.isEmpty()) {
                if (kelas.equals("A1")) {
                    students = new String[]{"12345 - John Doe", "12346 - Jane Smith"};
                } else if (kelas.equals("B2")) {
                    students = new String[]{"12347 - Alice Johnson", "12348 - Bob Brown"};
                }
            }
            
            // Add students to the combo box
            for (String student : students) {
                combo.addItem(student);
            }
            
            // Enable/disable combo based on whether we found students
            combo.setEnabled(students.length > 0);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal memuat daftar mahasiswa: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            combo.setEnabled(false);
        }
    }

    private Object[][] getNilaiDataForDosen() {
        // Implement to get grade data for this.dosen.getId()
        return new Object[][]{}; // Placeholder
    }

    private void saveGrade(String kelas, String nim, String nilai) {
        try {
            double nilaiDouble = Double.parseDouble(nilai);
            if (nilaiDouble < 0 || nilaiDouble > 100) {
                throw new IllegalArgumentException("Nilai harus antara 0-100");
            }
            
            // Save to database using this.dosen.getId()
            // Database.saveNilai(this.dosen.getId(), kelas, nim, nilaiDouble);
            
            refreshNilaiTable();
            JOptionPane.showMessageDialog(this, 
                String.format("Nilai %s berhasil disimpan untuk NIM %s di kelas %s", 
                nilai, nim, kelas));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Format nilai tidak valid. Harap masukkan angka.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan nilai: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}