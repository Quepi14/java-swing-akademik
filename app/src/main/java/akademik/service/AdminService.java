package akademik.service;

import akademik.config.DatabaseConfig;
import akademik.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    // Mengambil seluruh data admin
    public List<Admin> getAllAdmins() {
        List<Admin> adminList = new ArrayList<>();
        String sql = "SELECT * FROM admin";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                adminList.add(admin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adminList;
    }

    // Mengambil data admin berdasarkan username
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
