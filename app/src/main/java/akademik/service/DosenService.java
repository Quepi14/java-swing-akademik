package akademik.service;

import akademik.config.DatabaseConfig;
import akademik.model.Dosen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenService {
    
    public List<Dosen> getAllDosen() {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY id";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Dosen dosen = new Dosen();
                dosen.setId(rs.getInt("id"));
                dosen.setNama(rs.getString("nama"));
                dosen.setPendidikan(rs.getString("pendidikan"));
                dosen.setMataKuliah(rs.getString("mata_kuliah"));
                dosen.setEmail(rs.getString("email"));
                dosen.setTelepon(rs.getString("telepon"));
                dosenList.add(dosen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dosenList;
    }
    
    public int getTotalDosen() {
        String sql = "SELECT COUNT(*) FROM dosen";
        
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
    
    public String[][] getDosenTableData() {
        List<Dosen> dosenList = getAllDosen();
        String[][] data = new String[dosenList.size()][6];
        
        for (int i = 0; i < dosenList.size(); i++) {
            Dosen d = dosenList.get(i);
            data[i][0] = String.valueOf(d.getId());
            data[i][1] = d.getNama();
            data[i][2] = d.getPendidikan();
            data[i][3] = d.getMataKuliah();
            data[i][4] = d.getEmail();
            data[i][5] = d.getTelepon();
        }
        return data;
    }
    
    public boolean addDosen(Dosen dosen) {
        String sql = "INSERT INTO dosen (nama, pendidikan, mata_kuliah, email, telepon) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dosen.getNama());
            stmt.setString(2, dosen.getPendidikan());
            stmt.setString(3, dosen.getMataKuliah());
            stmt.setString(4, dosen.getEmail());
            stmt.setString(5, dosen.getTelepon());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDosen(Dosen dosen) {
        String sql = "UPDATE dosen SET nama = ?, pendidikan = ?, mata_kuliah = ?, email = ?, telepon = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dosen.getNama());
            stmt.setString(2, dosen.getPendidikan());
            stmt.setString(3, dosen.getMataKuliah());
            stmt.setString(4, dosen.getEmail());
            stmt.setString(5, dosen.getTelepon());
            stmt.setInt(6, dosen.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDosen(int id) {
        String sql = "DELETE FROM dosen WHERE id = ?";
        
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