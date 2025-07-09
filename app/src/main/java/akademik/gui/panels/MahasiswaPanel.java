package akademik.gui.panels;

import akademik.gui.forms.MahasiswaForm;
import akademik.model.Mahasiswa;
import akademik.service.MahasiswaService;
import akademik.util.ColorConstants;
import akademik.util.ComponentFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MahasiswaPanel extends JPanel {
    private MahasiswaService mahasiswaService;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public MahasiswaPanel() {
        this.mahasiswaService = new MahasiswaService();
        
        setLayout(new BorderLayout());
        setBackground(ColorConstants.BACKGROUND_COLOR);
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        JLabel header = ComponentFactory.createHeader("Data Mahasiswa");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        
        JPanel stats = new JPanel(new GridLayout(1, 2, 30, 0));
        stats.setBackground(ColorConstants.BACKGROUND_COLOR);
        stats.setBorder(new EmptyBorder(30, 100, 30, 100));
        
        stats.add(ComponentFactory.createCard(
            String.valueOf(mahasiswaService.getTotalMahasiswa()), 
            "Total Mahasiswa"
        ));
        stats.add(ComponentFactory.createCard(
            String.valueOf(mahasiswaService.getTotalMahasiswi()), 
            "Total Mahasiswi"
        ));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        
        JButton addButton = new JButton("TAMBAH MAHASISWA");
        addButton.setBackground(ColorConstants.PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addButton.addActionListener(e -> showAddForm());
        
        JButton editButton = new JButton("EDIT");
        editButton.setBackground(ColorConstants.ACCENT_COLOR);
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editButton.addActionListener(e -> showEditForm());
        
        JButton deleteButton = new JButton("HAPUS");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.addActionListener(e -> deleteMahasiswa());
        
        JButton refreshButton = new JButton("REFRESH");
        refreshButton.setBackground(ColorConstants.SECONDARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.addActionListener(e -> loadData());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        topPanel.add(stats, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columnNames = {"ID", "NAMA", "KELAS", "MATA KULIAH", "NIM", "JENIS KELAMIN", "EMAIL"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(ColorConstants.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showEditForm();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));

        add(header, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        String[][] data = mahasiswaService.getMahasiswaTableData();
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void showAddForm() {
        MahasiswaForm form = new MahasiswaForm((Frame) SwingUtilities.getWindowAncestor(this), false, null);
        form.setVisible(true);
        loadData(); // Refresh data after form closes
    }
    
    private void showEditForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa yang akan diedit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        Mahasiswa mahasiswa = mahasiswaService.getAllMahasiswa().stream()
            .filter(m -> m.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (mahasiswa != null) {
            MahasiswaForm form = new MahasiswaForm((Frame) SwingUtilities.getWindowAncestor(this), true, mahasiswa);
            form.setVisible(true);
            loadData(); // Refresh data after form closes
        }
    }
    
    private void deleteMahasiswa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data mahasiswa ini?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            if (mahasiswaService.deleteMahasiswa(id)) {
                JOptionPane.showMessageDialog(this, "Data mahasiswa berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data mahasiswa!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
