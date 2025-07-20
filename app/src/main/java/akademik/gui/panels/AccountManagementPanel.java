package akademik.gui.panels;

import akademik.model.Dosen;
import akademik.model.Mahasiswa;
import akademik.service.DosenService;
import akademik.service.MahasiswaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AccountManagementPanel extends JPanel {
    private final MahasiswaService mahasiswaService;
    private final DosenService dosenService;
    private JTable userTable;
    private JPanel filterPanel;
    
    public AccountManagementPanel(MahasiswaService mahasiswaService, DosenService dosenService) {
        this.mahasiswaService = mahasiswaService;
        this.dosenService = dosenService;
        initUI();
        loadUnregisteredUsers("mahasiswa");
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Filter Panel
        filterPanel = new JPanel();
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Mahasiswa", "Dosen"});
        JButton refreshBtn = new JButton("Refresh");
        
        filterPanel.add(new JLabel("Tampilkan:"));
        filterPanel.add(roleCombo);
        filterPanel.add(refreshBtn);
        
        // Table
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);
        
        // Action Panel
        JPanel actionPanel = new JPanel();
        JButton createBtn = new JButton("Buat Akun");
        actionPanel.add(createBtn);
        
        // Layout
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        refreshBtn.addActionListener(e -> 
            loadUnregisteredUsers(((String)roleCombo.getSelectedItem()).toLowerCase()));
            
        createBtn.addActionListener(e -> createAccount());
    }
    
    private void loadUnregisteredUsers(String role) {
        try {
            DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "NIM/NIDN", "Nama Lengkap", "Email", "Username"}
            );
            userTable.setModel(model);
            
            if (role.equals("mahasiswa")) {
                mahasiswaService.getAllMahasiswa().stream()
                    .filter(mhs -> mhs.getUsername() == null || mhs.getUsername().isEmpty())
                    .forEach(mhs -> model.addRow(new Object[]{
                        mhs.getNim(), 
                        mhs.getNim(),
                        mhs.getNamaLengkap(),
                        mhs.getEmail(),
                        mhs.getUsername()
                    }));
            } else {
                dosenService.getAllDosen().stream()
                    .filter(dsn -> dsn.getUsername() == null || dsn.getUsername().isEmpty())
                    .forEach(dsn -> model.addRow(new Object[]{
                        dsn.getNidn(),
                        dsn.getNidn(),
                        dsn.getNamaLengkap(),
                        dsn.getEmail(),
                        dsn.getUsername()
                    }));
            }
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan");
        }
    }
    
    private void createAccount() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu!");
            return;
        }
        
        String id = (String) userTable.getValueAt(selectedRow, 0);
        String role = ((String)((JComboBox<?>)filterPanel.getComponent(1)).getSelectedItem()).toLowerCase();
        
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        
        int result = JOptionPane.showConfirmDialog(
            this, inputPanel, "Buat Akun", 
            JOptionPane.OK_CANCEL_OPTION
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean success;
                if (role.equals("mahasiswa")) {
                    Mahasiswa mhs = mahasiswaService.getMahasiswaByNim(id);
                    if (mhs == null) throw new IllegalStateException("Mahasiswa tidak ditemukan");
                    mhs.setUsername(usernameField.getText());
                    mhs.setPassword(new String(passwordField.getPassword()));
                    success = mahasiswaService.updateMahasiswa(mhs);
                } else {
                    Dosen dsn = dosenService.getDosenByNidn(id);
                    if (dsn == null) throw new IllegalStateException("Dosen tidak ditemukan");
                    dsn.setUsername(usernameField.getText());
                    dsn.setPassword(new String(passwordField.getPassword()));
                    success = dosenService.updateDosen(dsn);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Akun berhasil dibuat!");
                    loadUnregisteredUsers(role);
                } else {
                    throw new IllegalStateException("Gagal menyimpan data");
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Input tidak valid: " + e.getMessage());
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
}