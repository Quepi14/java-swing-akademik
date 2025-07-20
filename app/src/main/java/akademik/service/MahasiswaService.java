package akademik.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import akademik.config.DatabaseConfig;
import akademik.model.JenisKelamin;
import akademik.model.Mahasiswa;

public class MahasiswaService {
    private static final Logger logger = Logger.getLogger(MahasiswaService.class.getName());
    private final PasswordService passwordService = new DefaultPasswordService();

    public boolean tambahMahasiswa(Mahasiswa mahasiswa) {
        String sql = "INSERT INTO mahasiswa (nim, nama_lengkap, jenis_kelamin, alamat, no_telp, email, username, password, status_akun, tanggal_masuk, id_dosen_wali) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, mahasiswa.getNim());
            stmt.setString(2, mahasiswa.getNamaLengkap());
            stmt.setString(3, mahasiswa.getJenisKelamin().name());
            stmt.setString(4, mahasiswa.getAlamat());
            stmt.setString(5, mahasiswa.getNoTelp());
            stmt.setString(6, mahasiswa.getEmail());
            stmt.setString(7, mahasiswa.getUsername());
            stmt.setString(8, passwordService.encryptPassword(mahasiswa.getPassword()));
            stmt.setString(9, mahasiswa.getStatusAkun());
            stmt.setDate(10, Date.valueOf(mahasiswa.getTanggalMasuk()));
            stmt.setInt(11, mahasiswa.getIdDosenWali());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding mahasiswa with NIM: " + mahasiswa.getNim(), e);
            return false;
        }
    }

    public boolean updateMahasiswa(Mahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET nama_lengkap = ?, jenis_kelamin = ?, alamat = ?, no_telp = ?, " +
                     "email = ?, username = ?, status_akun = ?, id_dosen_wali = ? " +
                     (mahasiswa.getPassword().equals("********") ? "" : ", password = ? ") +
                     "WHERE nim = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, mahasiswa.getNamaLengkap());
            stmt.setString(paramIndex++, mahasiswa.getJenisKelamin().name());
            stmt.setString(paramIndex++, mahasiswa.getAlamat());
            stmt.setString(paramIndex++, mahasiswa.getNoTelp());
            stmt.setString(paramIndex++, mahasiswa.getEmail());
            stmt.setString(paramIndex++, mahasiswa.getUsername());
            stmt.setString(paramIndex++, mahasiswa.getStatusAkun());
            stmt.setInt(paramIndex++, mahasiswa.getIdDosenWali());
            
            if (!mahasiswa.getPassword().equals("********")) {
                stmt.setString(paramIndex++, passwordService.encryptPassword(mahasiswa.getPassword()));
            }
            
            stmt.setString(paramIndex, mahasiswa.getNim());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating mahasiswa with NIM: " + mahasiswa.getNim(), e);
            return false;
        }
    }

    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nama_lengkap";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setNim(rs.getString("nim"));
                mhs.setNamaLengkap(rs.getString("nama_lengkap"));
                mhs.setJenisKelamin(JenisKelamin.valueOf(rs.getString("jenis_kelamin")));
                mhs.setAlamat(rs.getString("alamat"));
                mhs.setNoTelp(rs.getString("no_telp"));
                mhs.setEmail(rs.getString("email"));
                mhs.setUsername(rs.getString("username"));
                mhs.setStatusAkun(rs.getString("status_akun"));
                mhs.setTanggalMasuk(rs.getDate("tanggal_masuk").toLocalDate());
                mhs.setIdDosenWali(rs.getInt("id_dosen_wali"));
                
                mahasiswaList.add(mhs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all mahasiswa", e);
        }
        return mahasiswaList;
    }

    public Mahasiswa getMahasiswaByNim(String nim) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setNim(rs.getString("nim"));
                mhs.setNamaLengkap(rs.getString("nama_lengkap"));
                mhs.setJenisKelamin(JenisKelamin.valueOf(rs.getString("jenis_kelamin")));
                mhs.setAlamat(rs.getString("alamat"));
                mhs.setNoTelp(rs.getString("no_telp"));
                mhs.setEmail(rs.getString("email"));
                mhs.setUsername(rs.getString("username"));
                mhs.setStatusAkun(rs.getString("status_akun"));
                mhs.setTanggalMasuk(rs.getDate("tanggal_masuk").toLocalDate());
                mhs.setIdDosenWali(rs.getInt("id_dosen_wali"));
                
                return mhs;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching mahasiswa with NIM: " + nim, e);
        }
        return null;
    }

    public int getTotalMahasiswa() {
        String sql = "SELECT COUNT(*) FROM mahasiswa";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting total mahasiswa count", e);
        }
        return 0;
    }

    public int getTotalMahasiswi() {
        String sql = "SELECT COUNT(*) FROM mahasiswa WHERE jenis_kelamin = 'PEREMPUAN'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting total mahasiswi count", e);
        }
        return 0;
    }
}