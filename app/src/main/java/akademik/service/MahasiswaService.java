package akademik.service;

import akademik.config.DatabaseConfig;
import akademik.model.Mahasiswa;
import akademik.model.JenisKelamin;
import akademik.model.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaService {

    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nim";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mahasiswa mhs = new Mahasiswa(
                    rs.getInt("nim"),
                    rs.getString("nama"),
                    JenisKelamin.valueOf(rs.getString("jenis_kelamin")),
                    rs.getDate("ttl"),
                    rs.getString("prodi"),
                    rs.getString("fakultas"),
                    rs.getInt("semester"),
                    Status.valueOf(rs.getString("status").toUpperCase().replace("-", "_"))
                );
                mahasiswaList.add(mhs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mahasiswaList;
    }

    public boolean addMahasiswa(Mahasiswa mahasiswa) {
        String sql = "INSERT INTO mahasiswa (nim, nama, jenis_kelamin, ttl, prodi, fakultas, semester, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mahasiswa.getNim());
            stmt.setString(2, mahasiswa.getNama());
            stmt.setString(3, mahasiswa.getJenisKelamin().name());
            stmt.setDate(4, new java.sql.Date(mahasiswa.getTtl().getTime()));
            stmt.setString(5, mahasiswa.getProdi());
            stmt.setString(6, mahasiswa.getFakultas());
            stmt.setInt(7, mahasiswa.getSemester());
            stmt.setString(8, mahasiswa.getStatus().name());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMahasiswa(Mahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET nama = ?, jenis_kelamin = ?, ttl = ?, prodi = ?, fakultas = ?, semester = ?, status = ? WHERE nim = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mahasiswa.getNama());
            stmt.setString(2, mahasiswa.getJenisKelamin().name());
            stmt.setDate(3, new java.sql.Date(mahasiswa.getTtl().getTime()));
            stmt.setString(4, mahasiswa.getProdi());
            stmt.setString(5, mahasiswa.getFakultas());
            stmt.setInt(6, mahasiswa.getSemester());
            stmt.setString(7, mahasiswa.getStatus().name());
            stmt.setInt(8, mahasiswa.getNim());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMahasiswa(int nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nim);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
}
