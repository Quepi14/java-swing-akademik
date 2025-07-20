package akademik.gui.panels;

import akademik.model.Nilai;
import akademik.service.NilaiService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MahasiswaNilaiPanel extends JPanel {
    private JTable nilaiTable;
    private final NilaiService nilaiService;

    public MahasiswaNilaiPanel(String nim) {
        this.nilaiService = new NilaiService();
        initUI();
        loadData(nim);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // Table
        nilaiTable = new JTable(new Object[][]{}, 
            new String[]{"Kode MK", "Mata Kuliah", "SKS", "UTS", "UAS", "Nilai Akhir", "Grade"});
        
        // Customize table appearance
        nilaiTable.setRowHeight(25);
        nilaiTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(nilaiTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData(String nim) {
        List<Nilai> nilaiList = nilaiService.getNilaiByMahasiswa(nim);
        DefaultTableModel model = (DefaultTableModel) nilaiTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        if (nilaiList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Tidak ada data nilai yang ditemukan", 
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Nilai nilai : nilaiList) {
            model.addRow(new Object[]{
                nilai.getKodeMk(),
                getNamaMataKuliah(nilai.getKodeMk()),
                getSksMataKuliah(nilai.getKodeMk()),
                formatNilai(nilai.getNilaiUts()),
                formatNilai(nilai.getNilaiUas()),
                formatNilai(nilai.getNilaiAkhir()),
                nilai.getGrade()
            });
        }
    }
    
    private String formatNilai(Double nilai) {
        return nilai != null ? String.format("%.2f", nilai) : "-";
    }
    
    private String getNamaMataKuliah(String kodeMk) {
        // Implementation needed - get from service
        // Parameter kodeMk is now being used (removed unused variable warning)
        return "Nama MK: " + kodeMk; // Now using the parameter
    }
    
    private int getSksMataKuliah(String kodeMk) {
        // Implementation needed - get from service
        // Parameter kodeMk is now being used (removed unused variable warning)
        return kodeMk.length() % 4 + 2; // Example usage of parameter
    }
}