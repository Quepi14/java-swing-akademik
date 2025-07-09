package com.yourcompany.swingapp.gui.panels;

import com.yourcompany.swingapp.gui.forms.DosenForm;
import com.yourcompany.swingapp.model.Dosen;
import com.yourcompany.swingapp.service.DosenService;
import com.yourcompany.swingapp.util.ColorConstants;
import com.yourcompany.swingapp.util.ComponentFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DosenPanel extends JPanel {
    private DosenService dosenService;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public DosenPanel() {
        this.dosenService = new DosenService();
        
        setLayout(new BorderLayout());
        setBackground(ColorConstants.BACKGROUND_COLOR);
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        JLabel header = ComponentFactory.createHeader("Data Dosen");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        
        JPanel stats = new JPanel(new GridLayout(1, 2, 30, 0));
        stats.setBackground(ColorConstants.BACKGROUND_COLOR);
        stats.setBorder(new EmptyBorder(30, 100, 30, 100));
        
        stats.add(ComponentFactory.createCard(
            String.valueOf(dosenService.getTotalDosen()), 
            "Total Dosen"
        ));
        stats.add(ComponentFactory.createCard(
            String.valueOf(dosenService.getTotalDosenTetap()), 
            "Dosen Tetap"
        ));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        
        JButton addButton = new JButton("TAMBAH DOSEN");
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
        deleteButton.addActionListener(e -> deleteDosen());
        
        JButton refreshButton = new JButton("REFRESH");
        refreshButton.setBackground(ColorConstants.SECONDARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.addActionListener(e -> loadData());
        
        JButton detailButton = new JButton("DETAIL");
        detailButton.setBackground(new Color(52, 152, 219));
        detailButton.setForeground(Color.WHITE);
        detailButton.setFocusPainted(false);
        detailButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailButton.addActionListener(e -> showDetailForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailButton);
        
        topPanel.add(stats, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columnNames = {"ID", "NAMA DOSEN", "NIDN", "PENDIDIKAN", "MATA KULIAH", "STATUS", "EMAIL", "TELEPON"};
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
        
        // Atur lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // NAMA DOSEN
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // NIDN
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // PENDIDIKAN
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // MATA KULIAH
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // STATUS
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // EMAIL
        table.getColumnModel().getColumn(7).setPreferredWidth(120); // TELEPON
        
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
        String[][] data = dosenService.getDosenTableData();
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void showAddForm() {
        DosenForm form = new DosenForm((Frame) SwingUtilities.getWindowAncestor(this), false, null);
        form.setVisible(true);
        loadData(); // Refresh data after form closes
    }
    
    private void showEditForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih dosen yang akan diedit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        Dosen dosen = dosenService.getAllDosen().stream()
            .filter(d -> d.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (dosen != null) {
            DosenForm form = new DosenForm((Frame) SwingUtilities.getWindowAncestor(this), true, dosen);
            form.setVisible(true);
            loadData(); // Refresh data after form closes
        }
    }
    
    private void showDetailForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessage(this, "Pilih dosen untuk melihat detail!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        Dosen dosen = dosenService.getAllDosen().stream()
            .filter(d -> d.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (dosen != null) {
            showDosenDetail(dosen);
        }
    }
    
    private void showDosenDetail(Dosen dosen) {
        StringBuilder detail = new StringBuilder();
        detail.append("DETAIL DOSEN\n");
        detail.append("================================\n");
        detail.append("ID: ").append(dosen.getId()).append("\n");
        detail.append("Nama: ").append(dosen.getNama()).append("\n");
        detail.append("NIDN: ").append(dosen.getNidn()).append("\n");
        detail.append("Pendidikan: ").append(dosen.getPendidikan()).append("\n");
        detail.append("Mata Kuliah: ").append(dosen.getMataKuliah()).append("\n");
        detail.append("Status: ").append(dosen.getStatus()).append("\n");
        detail.append("Email: ").append(dosen.getEmail()).append("\n");
        detail.append("Telepon: ").append(dosen.getTelepon()).append("\n");
        detail.append("Alamat: ").append(dosen.getAlamat()).append("\n");
        
        JTextArea textArea = new JTextArea(detail.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detail Dosen", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteDosen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih dosen yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String namaDosen = table.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data dosen:\n" + namaDosen + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            if (dosenService.deleteDosen(id)) {
                JOptionPane.showMessageDialog(this, "Data dosen berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data dosen!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        loadData();
    }
}