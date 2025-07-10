package akademik.gui.form;

import akademik.model.Dosen;
import akademik.model.JenisKelamin;
import akademik.service.DosenService;
import akademik.util.ColorConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DosenForm extends JDialog {
    private JTextField nipField;
    private JTextField namaField;
    private JTextField gelarField;
    private JComboBox<JenisKelamin> jenisKelaminCombo;
    private JFormattedTextField ttlField;

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
        setSize(400, 420);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorConstants.BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(isEdit ? "EDIT DOSEN" : "TAMBAH DOSEN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // NIP
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("NIP:"), gbc);
        gbc.gridx = 1;
        nipField = new JTextField(20);
        formPanel.add(nipField, gbc);

        // Nama
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        namaField = new JTextField(20);
        formPanel.add(namaField, gbc);

        // Gelar
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Gelar:"), gbc);
        gbc.gridx = 1;
        gelarField = new JTextField(20);
        formPanel.add(gelarField, gbc);

        // Jenis Kelamin
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Jenis Kelamin:"), gbc);
        gbc.gridx = 1;
        jenisKelaminCombo = new JComboBox<>(JenisKelamin.values());
        formPanel.add(jenisKelaminCombo, gbc);

        // TTL
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Tanggal Lahir (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        ttlField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        ttlField.setValue(new Date());
        formPanel.add(ttlField, gbc);

        // Button Panel
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
        nipField.setText(String.valueOf(dosen.getNip()));
        nipField.setEditable(false); // tidak bisa diubah saat edit
        namaField.setText(dosen.getNama());
        gelarField.setText(dosen.getGelar());
        jenisKelaminCombo.setSelectedItem(dosen.getJenisKelamin());
        ttlField.setValue(dosen.getTtl());
    }

    private boolean validateForm() {
        if (nipField.getText().trim().isEmpty() || !nipField.getText().trim().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "NIP harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (namaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (gelarField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Gelar harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ttlField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal lahir harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validateForm()) {
                try {
                    int nip = Integer.parseInt(nipField.getText().trim());
                    String nama = namaField.getText().trim();
                    String gelar = gelarField.getText().trim();
                    JenisKelamin jk = (JenisKelamin) jenisKelaminCombo.getSelectedItem();
                    Date ttl = new SimpleDateFormat("yyyy-MM-dd").parse(ttlField.getText().trim());

                    Dosen d = new Dosen(nip, nama, gelar, jk, ttl);

                    boolean success;
                    if (isEdit) {
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
                } catch (ParseException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(DosenForm.this,
                            "Format data tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
