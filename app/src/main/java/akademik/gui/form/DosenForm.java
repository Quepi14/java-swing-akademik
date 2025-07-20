package akademik.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.shiro.authz.annotation.RequiresRoles;

import akademik.model.Dosen;
import akademik.model.JenisKelamin;
import akademik.service.DosenService;
import akademik.util.ColorConstants;

@RequiresRoles("admin")
public class DosenForm extends JDialog {
    private JTextField nidnField, namaField, alamatField, telpField, emailField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<JenisKelamin> jenisKelaminCombo;
    private JComboBox<String> statusCombo;
    private JButton simpanBtn, batalBtn;

    private final DosenService dosenService;
    private final boolean isEdit;
    private final Dosen dosen;

    public DosenForm(Frame parent, boolean isEdit, Dosen dosen, DosenService dosenService) {
        super(parent, isEdit ? "Edit Dosen" : "Tambah Dosen", true);
        this.dosenService = dosenService;
        this.isEdit = isEdit;
        this.dosen = dosen;
        
        initUI();
        if (isEdit && dosen != null) {
            loadData();
        }
    }

    private void initUI() {
        setSize(550, 550); // Diperbesar untuk menampung lebih banyak field
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel(isEdit ? "EDIT DOSEN" : "TAMBAH DOSEN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        // Form fields
        addFormField(formPanel, "NIDN:", nidnField = new JTextField());
        addFormField(formPanel, "Nama Lengkap:", namaField = new JTextField());
        addFormField(formPanel, "Jenis Kelamin:", jenisKelaminCombo = new JComboBox<>(JenisKelamin.values()));
        addFormField(formPanel, "Alamat:", alamatField = new JTextField());
        addFormField(formPanel, "No. Telepon:", telpField = new JTextField());
        addFormField(formPanel, "Email:", emailField = new JTextField());
        addFormField(formPanel, "Username:", usernameField = new JTextField());
        addFormField(formPanel, "Password:", passwordField = new JPasswordField());
        addFormField(formPanel, "Status Akun:", statusCombo = new JComboBox<>(new String[]{"AKTIF", "NON-AKTIF"}));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        batalBtn = new JButton("BATAL");
        batalBtn.addActionListener(e -> dispose());
        styleButton(batalBtn, ColorConstants.SECONDARY_COLOR);

        simpanBtn = new JButton("SIMPAN");
        simpanBtn.addActionListener(e -> simpanDosen());
        styleButton(simpanBtn, ColorConstants.PRIMARY_COLOR);

        buttonPanel.add(batalBtn);
        buttonPanel.add(simpanBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(jLabel);
        panel.add(field);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
    }

    private void loadData() {
        nidnField.setText(dosen.getNidn());
        namaField.setText(dosen.getNamaLengkap());
        jenisKelaminCombo.setSelectedItem(dosen.getJenisKelamin());
        alamatField.setText(dosen.getAlamat());
        telpField.setText(dosen.getNoTelp());
        emailField.setText(dosen.getEmail());
        usernameField.setText(dosen.getUsername());
        statusCombo.setSelectedItem(dosen.getStatusAkun());
        
        if (isEdit) {
            nidnField.setEditable(false);
            passwordField.setText("********"); // Placeholder for existing password
        }
    }

    private void simpanDosen() {
        if (!validateForm()) return;

        try {
            Dosen newDosen = new Dosen();
            newDosen.setId(isEdit ? dosen.getId() : 0);
            newDosen.setNidn(nidnField.getText());
            newDosen.setNamaLengkap(namaField.getText());
            newDosen.setJenisKelamin((JenisKelamin) jenisKelaminCombo.getSelectedItem());
            newDosen.setAlamat(alamatField.getText());
            newDosen.setNoTelp(telpField.getText());
            newDosen.setEmail(emailField.getText());
            newDosen.setUsername(usernameField.getText());
            newDosen.setStatusAkun(statusCombo.getSelectedItem().toString());
            newDosen.setTanggalBergabung(LocalDate.now());
            
            if (!isEdit || !String.valueOf(passwordField.getPassword()).equals("********")) {
                newDosen.setPassword(new String(passwordField.getPassword()));
            }

            boolean success = isEdit 
                ? dosenService.updateDosen(newDosen) 
                : dosenService.tambahDosen(newDosen);

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    isEdit ? "Data dosen berhasil diperbarui!" : "Data dosen berhasil ditambahkan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                throw new Exception("Gagal menyimpan data dosen");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            // Handle specific data validation errors
            Logger.getLogger(DosenForm.class.getName()).log(Level.WARNING, "Data tidak valid", e);
            showError("Data tidak valid: " + e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            Logger.getLogger(DosenForm.class.getName()).log(Level.SEVERE, "Error sistem", e);
            showError("Terjadi kesalahan sistem: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (nidnField.getText().trim().isEmpty()) {
            showError("NIDN harus diisi!");
            return false;
        }
        if (namaField.getText().trim().isEmpty()) {
            showError("Nama lengkap harus diisi!");
            return false;
        }
        if (usernameField.getText().trim().isEmpty()) {
            showError("Username harus diisi!");
            return false;
        }
        if (!isEdit && passwordField.getPassword().length == 0) {
            showError("Password harus diisi!");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}