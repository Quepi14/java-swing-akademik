package akademik.service;

import akademik.config.DatabaseConfig;
import akademik.model.Dosen;
import akademik.model.JenisKelamin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenService {

    public List<Dosen> getAllDosen() {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY nip";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Dosen dosen = new Dosen(
                    rs.getInt("nip"),
                    rs.getString("nama"),
                    rs.getString("gelar"),
                    JenisKelamin.valueOf(rs.getString("jenis_kelamin")),
                    rs.getDate("ttl")
                );
                dosenList.add(dosen);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dosenList;
    }

    public boolean addDosen(Dosen dosen) {
        String sql = "INSERT INTO dosen (nip, nama, gelar, jenis_kelamin, ttl) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dosen.getNip());
            stmt.setString(2, dosen.getNama());
            stmt.setString(3, dosen.getGelar());
            stmt.setString(4, dosen.getJenisKelamin().name());
            stmt.setDate(5, new java.sql.Date(dosen.getTtl().getTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDosen(Dosen dosen) {
        String sql = "UPDATE dosen SET nama = ?, gelar = ?, jenis_kelamin = ?, ttl = ? WHERE nip = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dosen.getNama());
            stmt.setString(2, dosen.getGelar());
            stmt.setString(3, dosen.getJenisKelamin().name());
            stmt.setDate(4, new java.sql.Date(dosen.getTtl().getTime()));
            stmt.setInt(5, dosen.getNip());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDosen(int nip) {
        String sql = "DELETE FROM dosen WHERE nip = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nip);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
