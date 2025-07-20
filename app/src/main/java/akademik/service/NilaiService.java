package akademik.service;

import akademik.model.Nilai;
import akademik.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NilaiService {
    private static final Logger logger = Logger.getLogger(NilaiService.class.getName());

    public boolean inputNilai(Nilai nilai) {
        String sql = "INSERT INTO nilai (id_krs, kode_mk, nim, nilai_uts, nilai_uas, created_by) " +
                   "VALUES (?, ?, ?, ?, ?, ?) " +
                   "ON DUPLICATE KEY UPDATE " +
                   "nilai_uts = VALUES(nilai_uts), " +
                   "nilai_uas = VALUES(nilai_uas)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nilai.getIdKrs());
            stmt.setString(2, nilai.getKodeMk());
            stmt.setString(3, nilai.getNim());

            Double nilaiUts = nilai.getNilaiUts();
            stmt.setObject(4, nilaiUts, Types.DOUBLE);
            
            Double nilaiUas = nilai.getNilaiUas();
            stmt.setObject(5, nilaiUas, Types.DOUBLE);
            
            Integer createdBy = nilai.getCreatedBy();
            stmt.setObject(6, createdBy, Types.INTEGER);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inputting nilai for NIM: " + nilai.getNim() + 
                    " and kode MK: " + nilai.getKodeMk(), e);
            return false;
        }
    }

    public List<Nilai> getNilaiByKelas(String kodeMk) {
        List<Nilai> nilaiList = new ArrayList<>();
        String sql = "SELECT n.* FROM nilai n " +
                     "JOIN krs k ON n.id_krs = k.id " +
                     "JOIN kelas l ON k.id_kelas = l.id " +
                     "WHERE l.kode_mk = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kodeMk);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Nilai nilai = new Nilai(
                        rs.getInt("id"),
                        rs.getInt("id_krs"),
                        rs.getString("kode_mk"),
                        rs.getString("nim"),
                        safeGetDouble(rs, "nilai_uts"),
                        safeGetDouble(rs, "nilai_uas"),
                        safeGetDouble(rs, "nilai_akhir"),
                        rs.getString("grade"),
                        safeGetInt(rs, "created_by"),
                        rs.getTimestamp("updated_at") != null ? 
                            rs.getTimestamp("updated_at").toLocalDateTime() : null
                    );
                    nilaiList.add(nilai);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching nilai for kode MK: " + kodeMk, e);
        }
        return nilaiList;
    }

    public List<Nilai> getNilaiByMahasiswa(String nim) {
        List<Nilai> nilaiList = new ArrayList<>();
        String sql = "SELECT * FROM nilai WHERE nim = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nim);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Nilai nilai = new Nilai(
                        rs.getInt("id"),
                        rs.getInt("id_krs"),
                        rs.getString("kode_mk"),
                        rs.getString("nim"),
                        safeGetDouble(rs, "nilai_uts"),
                        safeGetDouble(rs, "nilai_uas"),
                        safeGetDouble(rs, "nilai_akhir"),
                        rs.getString("grade"),
                        safeGetInt(rs, "created_by"),
                        rs.getTimestamp("updated_at") != null ? 
                            rs.getTimestamp("updated_at").toLocalDateTime() : null
                    );
                    nilaiList.add(nilai);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching nilai for NIM: " + nim, e);
        }
        return nilaiList;
    }

    public double getNilaiAkhirByMahasiswa(String nim, String kodeMk) {
        String sql = "SELECT (nilai_uts * 0.4 + nilai_uas * 0.6) AS nilai_akhir " +
                   "FROM nilai WHERE nim = ? AND kode_mk = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return safeGetDouble(rs, "nilai_akhir");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error calculating nilai akhir for NIM: " + nim + 
                    " and kode MK: " + kodeMk, e);
        }
        return 0;
    }

    // Helper methods for safe value retrieval
    private Double safeGetDouble(ResultSet rs, String column) throws SQLException {
        double value = rs.getDouble(column);
        return rs.wasNull() ? null : value;
    }

    private Integer safeGetInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }
}