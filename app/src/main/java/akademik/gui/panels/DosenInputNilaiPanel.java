package akademik.gui.panels;

import akademik.model.Nilai;
import akademik.service.NilaiService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class DosenInputNilaiPanel extends JPanel {
    private JComboBox<String> kelasCombo;
    private JTable mahasiswaTable;
    private final NilaiService nilaiService;

    public DosenInputNilaiPanel(int idDosen) {
        this.nilaiService = new NilaiService();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kelasCombo = new JComboBox<>();
        JButton loadBtn = new JButton("Load Mahasiswa");
        
        filterPanel.add(new JLabel("Pilih Kelas:"));
        filterPanel.add(kelasCombo);
        filterPanel.add(loadBtn);
        add(filterPanel, BorderLayout.NORTH);

        // Table
        mahasiswaTable = new JTable(new Object[][]{}, 
            new String[]{"NIM", "Nama", "UTS", "UAS", "Akhir", "Grade"});
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);
        add(scrollPane, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Simpan Nilai");
        actionPanel.add(saveBtn);
        add(actionPanel, BorderLayout.SOUTH);

        // Event Listeners
        loadBtn.addActionListener(e -> loadMahasiswa());
        saveBtn.addActionListener(e -> saveNilai());
    }

    private void loadMahasiswa() {
        // Implementasi loading mahasiswa per kelas
    }

    private void saveNilai() {
        try {
            int rowCount = mahasiswaTable.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                Nilai nilai = new Nilai(
                    0, 
                    ((Number)mahasiswaTable.getValueAt(i, 0)).intValue(),
                    kelasCombo.getSelectedItem().toString(), 
                    mahasiswaTable.getValueAt(i, 1).toString(), 
                    ((Number)mahasiswaTable.getValueAt(i, 2)).doubleValue(), 
                    ((Number)mahasiswaTable.getValueAt(i, 3)).doubleValue(), 
                    null, 
                    null, 
                    ((Number)mahasiswaTable.getValueAt(i, 0)).intValue(),
                    LocalDateTime.now()
                );
                nilaiService.inputNilai(nilai);
            }
            JOptionPane.showMessageDialog(this, "Nilai berhasil disimpan!");
        } catch (ClassCastException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, """
                                                Format nilai tidak valid. Pastikan:
                                                - NIM harus angka
                                                - Nilai UTS/UAS harus angka (0-100)""", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}