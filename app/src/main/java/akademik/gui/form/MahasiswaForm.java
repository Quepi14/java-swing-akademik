package akademik.gui.form;

import akademik.model.JenisKelamin;
import akademik.model.Mahasiswa;
import akademik.model.Status;
import akademik.service.MahasiswaService;
import akademik.util.ColorConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MahasiswaForm extends JDialog {
    private JTextField nimField, namaField, ttlField, prodiField, fakultasField;
    private JComboBox<String> jenisKelaminCombo, statusCombo;
    private JSpinner semesterSpinner;
    private final MahasiswaService mahasiswaService;
    private final boolean isEdit;
    private final Mahasiswa mahasiswa;

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
        setSize(400, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorConstants.BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel titleLabel = new JLabel(isEdit ? "EDIT MAHASISWA" : "TAMBAH MAHASISWA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ColorConstants.BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        nimField = addTextField(formPanel, "NIM:", gbc, y++);
        namaField = addTextField(formPanel, "Nama:", gbc, y++);
        ttlField = addTextField(formPanel, "Tanggal Lahir (yyyy-MM-dd):", gbc, y++);
        prodiField = addTextField(formPanel, "Prodi:", gbc, y++);
        fakultasField = addTextField(formPanel, "Fakultas:", gbc, y++);

        // Jenis Kelamin
        gbc.gridx = 0;
        gbc.gridy = y;
        formPanel.add(new JLabel("Jenis Kelamin:"), gbc);
        jenisKelaminCombo = new JComboBox<>(new String[]{"L", "P"});
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(jenisKelaminCombo, gbc);
        y++;

        // Semester
        gbc.gridx = 0;
        gbc.gridy = y;
        formPanel.add(new JLabel("Semester:"), gbc);
        semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));
        gbc.gridx = 1;
        formPanel.add(semesterSpinner, gbc);
        y++;

        // Status
        gbc.gridx = 0;
        gbc.gridy = y;
        formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"Aktif", "Cuti", "Lulus", "DO"});
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        y++;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        JButton saveButton = new JButton("SIMPAN");
        saveButton.setBackground(ColorConstants.PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.addActionListener(this::saveAction);

        JButton cancelButton = new JButton("BATAL");
        cancelButton.setBackground(ColorConstants.SECONDARY_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField addTextField(JPanel panel, String label, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(20);
        panel.add(field, gbc);
        return field;
    }

    private void loadData() {
        nimField.setText(String.valueOf(mahasiswa.getNim()));
        namaField.setText(mahasiswa.getNama());
        ttlField.setText(new SimpleDateFormat("yyyy-MM-dd").format(mahasiswa.getTtl()));
        prodiField.setText(mahasiswa.getProdi());
        fakultasField.setText(mahasiswa.getFakultas());
        jenisKelaminCombo.setSelectedItem(mahasiswa.getJenisKelamin().name());
        semesterSpinner.setValue(mahasiswa.getSemester());
        statusCombo.setSelectedItem(mahasiswa.getStatus().name().replace("_", "-").toUpperCase());
    }

    private void saveAction(ActionEvent e) {
        if (!validateForm()) return;

        try {
            Mahasiswa m = new Mahasiswa(
                Integer.parseInt(nimField.getText().trim()),
                namaField.getText().trim(),
                jenisKelaminCombo.getSelectedItem().equals("L") ? JenisKelamin.L : JenisKelamin.P,
                new SimpleDateFormat("yyyy-MM-dd").parse(ttlField.getText().trim()),
                prodiField.getText().trim(),
                fakultasField.getText().trim(),
                (Integer) semesterSpinner.getValue(),
                Status.valueOf(statusCombo.getSelectedItem().toString().toUpperCase().replace("-", "_"))
            );

            boolean success;
            if (isEdit) {
                // NOTE: Kalau nanti kamu tambahkan ID, set disini
                success = mahasiswaService.updateMahasiswa(m);
            } else {
                success = mahasiswaService.addMahasiswa(m);
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        isEdit ? "Data mahasiswa berhasil diperbarui!" : "Data mahasiswa berhasil ditambahkan!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data mahasiswa!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException | ParseException ex) {
            JOptionPane.showMessageDialog(this, "Format input salah: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (nimField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (namaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ttlField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal lahir harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
