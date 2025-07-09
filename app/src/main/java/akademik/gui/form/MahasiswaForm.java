package akademik.gui.forms;

import akademik.model.Mahasiswa;
import akademik.service.MahasiswaService;
import akademik.util.ColorConstants;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MahasiswaForm extends JDialog {
    private JTextField namaField;
    private JTextField kelasField;
    private JTextField mataKuliahField;
    private JTextField nimField;
    private JComboBox<String> jenisKelaminCombo;
    private JTextField emailField;
    private JTextField teleponField;
    private MahasiswaService mahasiswaService;
    private boolean isEdit;
    private Mahasiswa mahasiswa;
    
    public MahasiswaForm(Frame parent, boolean isEdit, Mahasiswa mahasiswa) {
        super(parent, isEdit ? "Edit Mahasiswa" : "Tambah Mahasiswa", true);
        this.mahasiswaService = new MahasiswaService();
        this.isEdit = isEdit;
        this.mahasiswa = mahasiswa;
        
        initComponents();
        if (isEdit && mahasiswa != null) {
            loadData();
        }
    }
    
    private void initComponents() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorConstants.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(isEdit ? "EDIT MAHASISWA" : "TAMBAH MAHASISWA", SwingConstants.CENTER);
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
        
        // Kelas
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Kelas:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        kelasField = new JTextField(20);
        formPanel.add(kelasField, gbc);
        
        // Mata Kuliah
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Mata Kuliah:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mataKuliahField = new JTextField(20);
        formPanel.add(mataKuliahField, gbc);
        
        // NIM
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("NIM:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nimField = new JTextField(20);
        formPanel.add(nimField, gbc);
        
        // Jenis Kelamin
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Jenis Kelamin:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        jenisKelaminCombo = new JComboBox<>(new String[]{"L", "P"});
        formPanel.add(jenisKelaminCombo, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Telepon
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
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
        namaField.setText(mahasiswa.getNama());
        kelasField.setText(mahasiswa.getKelas());
        mataKuliahField.setText(mahasiswa.getMataKuliah());
        nimField.setText(mahasiswa.getNim());
        jenisKelaminCombo.setSelectedItem(mahasiswa.getJenisKelamin());
        emailField.setText(mahasiswa.getEmail());
        teleponField.setText(mahasiswa.getTelepon());
    }
    
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validateForm()) {
                Mahasiswa m = new Mahasiswa();
                m.setNama(namaField.getText().trim());
                m.setKelas(kelasField.getText().trim());
                m.setMataKuliah(mataKuliahField.getText().trim());
                m.setNim(nimField.getText().trim());
                m.setJenisKelamin((String) jenisKelaminCombo.getSelectedItem());
                m.setEmail(emailField.getText().trim());
                m.setTelepon(teleponField.getText().trim());
                
                boolean success;
                if (isEdit) {
                    m.setId(mahasiswa.getId());
                    success = mahasiswaService.updateMahasiswa(m);
                } else {
                    success = mahasiswaService.addMahasiswa(m);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(MahasiswaForm.this, 
                        isEdit ? "Data mahasiswa berhasil diperbarui!" : "Data mahasiswa berhasil ditambahkan!", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(MahasiswaForm.this, 
                        "Gagal menyimpan data mahasiswa!", 
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
        if (kelasField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kelas harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (mataKuliahField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mata Kuliah harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nimField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}