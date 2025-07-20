package akademik.service;

import akademik.model.Dosen;
import akademik.model.JenisKelamin;
import akademik.config.DatabaseConfig;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DosenService {
    private static final Logger logger = Logger.getLogger(DosenService.class.getName());
    private final PasswordService passwordService = new DefaultPasswordService();

    public boolean tambahDosen(Dosen dosen) {
        String sql = "INSERT INTO dosen (nidn, nama_lengkap, jenis_kelamin, alamat, no_telp, email, username, password, status_akun, tanggal_bergabung) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dosen.getNidn());
            stmt.setString(2, dosen.getNamaLengkap());
            stmt.setString(3, dosen.getJenisKelamin().name());
            stmt.setString(4, dosen.getAlamat());
            stmt.setString(5, dosen.getNoTelp());
            stmt.setString(6, dosen.getEmail());
            stmt.setString(7, dosen.getUsername());
            stmt.setString(8, passwordService.encryptPassword(dosen.getPassword()));
            stmt.setString(9, dosen.getStatusAkun());
            stmt.setDate(10, Date.valueOf(dosen.getTanggalBergabung()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding dosen with NIDN: " + dosen.getNidn(), e);
            return false;
        }
    }

    public boolean updateDosen(Dosen dosen) {
        String sql = "UPDATE dosen SET nama_lengkap = ?, jenis_kelamin = ?, alamat = ?, no_telp = ?, " +
                     "email = ?, username = ?, status_akun = ? " +
                     (dosen.getPassword().equals("********") ? "" : ", password = ? ") +
                     "WHERE nidn = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, dosen.getNamaLengkap());
            stmt.setString(paramIndex++, dosen.getJenisKelamin().name());
            stmt.setString(paramIndex++, dosen.getAlamat());
            stmt.setString(paramIndex++, dosen.getNoTelp());
            stmt.setString(paramIndex++, dosen.getEmail());
            stmt.setString(paramIndex++, dosen.getUsername());
            stmt.setString(paramIndex++, dosen.getStatusAkun());
            
            if (!dosen.getPassword().equals("********")) {
                stmt.setString(paramIndex++, passwordService.encryptPassword(dosen.getPassword()));
            }
            
            stmt.setString(paramIndex, dosen.getNidn());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating dosen with NIDN: " + dosen.getNidn(), e);
            return false;
        }
    }

    public List<Dosen> getAllDosen() {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY nama_lengkap";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Dosen dosen = new Dosen();
                dosen.setNidn(rs.getString("nidn"));
                dosen.setNamaLengkap(rs.getString("nama_lengkap"));
                dosen.setJenisKelamin(JenisKelamin.valueOf(rs.getString("jenis_kelamin")));
                dosen.setAlamat(rs.getString("alamat"));
                dosen.setNoTelp(rs.getString("no_telp"));
                dosen.setEmail(rs.getString("email"));
                dosen.setUsername(rs.getString("username"));
                dosen.setStatusAkun(rs.getString("status_akun"));
                dosen.setTanggalBergabung(rs.getDate("tanggal_bergabung").toLocalDate());
                
                dosenList.add(dosen);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all dosen", e);
        }
        return dosenList;
    }

    public Dosen getDosenByNidn(String nidn) {
        String sql = "SELECT * FROM dosen WHERE nidn = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nidn);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Dosen dosen = new Dosen();
                dosen.setNidn(rs.getString("nidn"));
                dosen.setNamaLengkap(rs.getString("nama_lengkap"));
                dosen.setJenisKelamin(JenisKelamin.valueOf(rs.getString("jenis_kelamin")));
                dosen.setAlamat(rs.getString("alamat"));
                dosen.setNoTelp(rs.getString("no_telp"));
                dosen.setEmail(rs.getString("email"));
                dosen.setUsername(rs.getString("username"));
                dosen.setStatusAkun(rs.getString("status_akun"));
                dosen.setTanggalBergabung(rs.getDate("tanggal_bergabung").toLocalDate());
                
                return dosen;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching dosen with NIDN: " + nidn, e);
        }
        return null;
    }

    public List<String> getKelasByDosen(String nidn) {
        List<String> kelasList = new ArrayList<>();
        String sql = "SELECT k.nama_kelas FROM kelas k " +
                     "JOIN dosen d ON k.id_dosen = d.id " +
                     "WHERE d.nidn = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nidn);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                kelasList.add(rs.getString("nama_kelas"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching kelas for dosen with NIDN: " + nidn, e);
        }
        return kelasList;
    }

    public boolean saveNilai(String nim, String kodeMk, double uts, double uas) {
        String sql = "INSERT INTO nilai (nim, kode_mk, nilai_uts, nilai_uas) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "nilai_uts = VALUES(nilai_uts), " +
                     "nilai_uas = VALUES(nilai_uas)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            stmt.setDouble(3, uts);
            stmt.setDouble(4, uas);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving nilai for NIM: " + nim + " and kode MK: " + kodeMk, e);
            return false;
        }
    }
}