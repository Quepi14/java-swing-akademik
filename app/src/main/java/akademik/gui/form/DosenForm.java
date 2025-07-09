package akademik.gui.forms;

import akademik.model.Dosen;
import akademik.service.DosenService;
import akademik.util.ColorConstants;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DosenForm extends JDialog {
    private JTextField namaField;
    private JComboBox<String> pendidikanCombo;
    private JTextField mataKuliahField;
    private JTextField emailField;
    private JTextField teleponField;
    private DosenService dosenService;
    private boolean isEdit;
    private Dosen dosen;
    
    public DosenForm(Frame parent, boolean isEdit, Dosen dosen) {
        super(parent, isEdit ? "Edit Dosen" : "Tambah Dosen", true);
        this.dosenService = new DosenService();
        this.isEdit = isEdit;
        this.dosen = dosen;
        
        initComponents();
        if (isEdit && dosen != null) {
            loadData();
        }
    }
    
    private void initComponents() {
        setSize(400, 380);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorConstants.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(isEdit ? "EDIT DOSEN" : "TAMBAH DOSEN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Nama
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        namaField = new JTextField(20);
        formPanel.add(namaField, gbc);
        
        // Pendidikan
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Pendidikan:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        pendidikanCombo = new JComboBox<>(new String[]{"S1", "S2", "S3"});
        formPanel.add(pendidikanCombo, gbc);
        
        // Mata Kuliah
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Mata Kuliah:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mataKuliahField = new JTextField(20);
        formPanel.add(mataKuliahField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Telepon
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        teleponField = new JTextField(20);
        formPanel.add(teleponField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        
        JButton saveButton = new JButton("SIMPAN");
        saveButton.setBackground(ColorConstants.PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.addActionListener(new SaveActionListener());
        
        JButton cancelButton = new JButton("BATAL");
        cancelButton.setBackground(ColorConstants.SECONDARY_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        namaField.setText(dosen.getNama());
        pendidikanCombo.setSelectedItem(dosen.getPendidikan());
        mataKuliahField.setText(dosen.getMataKuliah());
        emailField.setText(dosen.getEmail());
        teleponField.setText(dosen.getTelepon());
    }
    
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validateForm()) {
                Dosen d = new Dosen();
                d.setNama(namaField.getText().trim());
                d.setPendidikan((String) pendidikanCombo.getSelectedItem());
                d.setMataKuliah(mataKuliahField.getText().trim());
                d.setEmail(emailField.getText().trim());
                d.setTelepon(teleponField.getText().trim());
                
                boolean success;
                if (isEdit) {
                    d.setId(dosen.getId());
                    success = dosenService.updateDosen(d);
                } else {
                    success = dosenService.addDosen(d);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(DosenForm.this, 
                        isEdit ? "Data dosen berhasil diperbarui!" : "Data dosen berhasil ditambahkan!", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(DosenForm.this, 
                        "Gagal menyimpan data dosen!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private boolean validateForm() {
        if (namaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (mataKuliahField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mata Kuliah harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}