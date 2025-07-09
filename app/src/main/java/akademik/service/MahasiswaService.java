package akademik.service;

import akademik.config.DatabaseConfig;
import akademik.model.Mahasiswa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaService {
    
    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY id";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setId(rs.getInt("id"));
                mahasiswa.setNama(rs.getString("nama"));
                mahasiswa.setKelas(rs.getString("kelas"));
                mahasiswa.setMataKuliah(rs.getString("mata_kuliah"));
                mahasiswa.setNim(rs.getString("nim"));
                mahasiswa.setJenisKelamin(rs.getString("jenis_kelamin"));
                mahasiswa.setEmail(rs.getString("email"));
                mahasiswa.setTelepon(rs.getString("telepon"));
                mahasiswaList.add(mahasiswa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mahasiswaList;
    }
    
    public int getTotalMahasiswa() {
        String sql = "SELECT COUNT(*) FROM mahasiswa";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getTotalMahasiswi() {
        String sql = "SELECT COUNT(*) FROM mahasiswa WHERE jenis_kelamin = 'P'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public String[][] getMahasiswaTableData() {
        List<Mahasiswa> mahasiswaList = getAllMahasiswa();
        String[][] data = new String[mahasiswaList.size()][7];
        
        for (int i = 0; i < mahasiswaList.size(); i++) {
            Mahasiswa m = mahasiswaList.get(i);
            data[i][0] = String.valueOf(m.getId());
            data[i][1] = m.getNama();
            data[i][2] = m.getKelas();
            data[i][3] = m.getMataKuliah();
            data[i][4] = m.getNim();
            data[i][5] = m.getJenisKelamin();
            data[i][6] = m.getEmail();
        }
        return data;
    }
    
    public boolean addMahasiswa(Mahasiswa mahasiswa) {
        String sql = "INSERT INTO mahasiswa (nama, kelas, mata_kuliah, nim, jenis_kelamin, email, telepon) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, mahasiswa.getNama());
            stmt.setString(2, mahasiswa.getKelas());
            stmt.setString(3, mahasiswa.getMataKuliah());
            stmt.setString(4, mahasiswa.getNim());
            stmt.setString(5, mahasiswa.getJenisKelamin());
            stmt.setString(6, mahasiswa.getEmail());
            stmt.setString(7, mahasiswa.getTelepon());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateMahasiswa(Mahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET nama = ?, kelas = ?, mata_kuliah = ?, nim = ?, jenis_kelamin = ?, email = ?, telepon = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, mahasiswa.getNama());
            stmt.setString(2, mahasiswa.getKelas());
            stmt.setString(3, mahasiswa.getMataKuliah());
            stmt.setString(4, mahasiswa.getNim());
            stmt.setString(5, mahasiswa.getJenisKelamin());
            stmt.setString(6, mahasiswa.getEmail());
            stmt.setString(7, mahasiswa.getTelepon());
            stmt.setInt(8, mahasiswa.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteMahasiswa(int id) {
        String sql = "DELETE FROM mahasiswa WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}