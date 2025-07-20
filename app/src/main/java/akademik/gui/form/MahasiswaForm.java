package akademik.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.time.LocalDate;
import java.util.List;
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
import akademik.model.Mahasiswa;
import akademik.service.DosenService;
import akademik.service.MahasiswaService;
import akademik.util.ColorConstants;

@RequiresRoles("admin")
public class MahasiswaForm extends JDialog {
    private JTextField nimField, namaField, alamatField, telpField, emailField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<JenisKelamin> jenisKelaminCombo;
    private JComboBox<String> statusCombo;
    private JComboBox<Dosen> dosenWaliCombo;
    private JButton simpanBtn, batalBtn;

    private final MahasiswaService mahasiswaService;
    private final DosenService dosenService;
    private final boolean isEdit;
    private final Mahasiswa mahasiswa;

    public MahasiswaForm(Frame parent, boolean isEdit, Mahasiswa mahasiswa, 
                        MahasiswaService mahasiswaService, DosenService dosenService) {
        super(parent, isEdit ? "Edit Mahasiswa" : "Tambah Mahasiswa", true);
        this.mahasiswaService = mahasiswaService;
        this.dosenService = dosenService;
        this.isEdit = isEdit;
        this.mahasiswa = mahasiswa;
        
        initUI();
        if (isEdit && mahasiswa != null) {
            loadData();
        }
    }

    private void initUI() {
        setSize(600, 650); // Diperbesar untuk menampung lebih banyak field
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorConstants.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel(isEdit ? "EDIT MAHASISWA" : "TAMBAH MAHASISWA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        // Form fields
        addFormField(formPanel, "NIM:", nimField = new JTextField());
        addFormField(formPanel, "Nama Lengkap:", namaField = new JTextField());
        addFormField(formPanel, "Jenis Kelamin:", jenisKelaminCombo = new JComboBox<>(JenisKelamin.values()));
        addFormField(formPanel, "Alamat:", alamatField = new JTextField());
        addFormField(formPanel, "No. Telepon:", telpField = new JTextField());
        addFormField(formPanel, "Email:", emailField = new JTextField());
        addFormField(formPanel, "Username:", usernameField = new JTextField());
        addFormField(formPanel, "Password:", passwordField = new JPasswordField());
        addFormField(formPanel, "Status Akun:", statusCombo = new JComboBox<>(new String[]{"AKTIF", "NON-AKTIF"}));
        addFormField(formPanel, "Dosen Wali:", dosenWaliCombo = new JComboBox<>());
        
        // Load dosen wali options
        loadDosenWaliOptions();

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ColorConstants.BACKGROUND_COLOR);

        batalBtn = new JButton("BATAL");
        batalBtn.addActionListener(e -> dispose());
        styleButton(batalBtn, ColorConstants.SECONDARY_COLOR);

        simpanBtn = new JButton("SIMPAN");
        simpanBtn.addActionListener(e -> simpanMahasiswa());
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

    private void loadDosenWaliOptions() {
        List<Dosen> dosenList = dosenService.getAllDosen();
        dosenWaliCombo.removeAllItems();
        for (Dosen dosen : dosenList) {
            dosenWaliCombo.addItem(dosen);
        }
    }

    private void loadData() {
        nimField.setText(mahasiswa.getNim());
        namaField.setText(mahasiswa.getNamaLengkap());
        jenisKelaminCombo.setSelectedItem(mahasiswa.getJenisKelamin());
        alamatField.setText(mahasiswa.getAlamat());
        telpField.setText(mahasiswa.getNoTelp());
        emailField.setText(mahasiswa.getEmail());
        usernameField.setText(mahasiswa.getUsername());
        statusCombo.setSelectedItem(mahasiswa.getStatusAkun());
        
        // Penanganan dosen wali dengan tipe int
        int idDosenWali = mahasiswa.getIdDosenWali();
        for (int i = 0; i < dosenWaliCombo.getItemCount(); i++) {
            Dosen dosen = dosenWaliCombo.getItemAt(i);
            if (dosen != null && dosen.getId() == idDosenWali) {
                dosenWaliCombo.setSelectedIndex(i);
                break;
            }
        }
        
        if (isEdit) {
            nimField.setEditable(false);
            passwordField.setText("********");
        }
    }

    private void simpanMahasiswa() {
        if (!validateForm()) return;

        try {
            Mahasiswa mhs = new Mahasiswa();
            mhs.setId(isEdit ? mahasiswa.getId() : 0); 
            mhs.setNim(nimField.getText());
            mhs.setNamaLengkap(namaField.getText());
            mhs.setJenisKelamin((JenisKelamin) jenisKelaminCombo.getSelectedItem());
            mhs.setAlamat(alamatField.getText());
            mhs.setNoTelp(telpField.getText());
            mhs.setEmail(emailField.getText());
            mhs.setUsername(usernameField.getText());
            mhs.setStatusAkun(statusCombo.getSelectedItem().toString());
            mhs.setTanggalMasuk(LocalDate.now());
            
            if (dosenWaliCombo.getSelectedItem() != null) {
                mhs.setIdDosenWali(((Dosen) dosenWaliCombo.getSelectedItem()).getId());
            }
            
            if (!isEdit || !String.valueOf(passwordField.getPassword()).equals("********")) {
                mhs.setPassword(new String(passwordField.getPassword()));
            }

            boolean success = isEdit 
                ? mahasiswaService.updateMahasiswa(mhs) 
                : mahasiswaService.tambahMahasiswa(mhs);

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    isEdit ? "Data mahasiswa berhasil diperbarui!" : "Data mahasiswa berhasil ditambahkan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                throw new IllegalStateException("Gagal menyimpan data mahasiswa");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            // Handle data validation errors
            Logger.getLogger(MahasiswaForm.class.getName()).log(Level.WARNING, "Data tidak valid", e);
            showError("Data tidak valid: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Handle business logic errors
            Logger.getLogger(MahasiswaForm.class.getName()).log(Level.WARNING, "Gagal operasi", e);
            showError(e.getMessage());
        } catch (HeadlessException e) {
            Logger.getLogger(MahasiswaForm.class.getName()).log(Level.SEVERE, "Error sistem", e);
            showError("Terjadi kesalahan sistem: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (nimField.getText().trim().isEmpty()) {
            showError("NIM harus diisi!");
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