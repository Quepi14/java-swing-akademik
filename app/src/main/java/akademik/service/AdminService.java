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
import akademik.model.Admin;

public class AdminService {
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());
    private final PasswordService passwordService = new DefaultPasswordService();

    public boolean tambahAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username, password, role, nama_lengkap, email, tanggal_dibuat, status_akun) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, passwordService.encryptPassword(admin.getPassword()));
            stmt.setString(3, admin.getRole());
            stmt.setString(4, admin.getNamaLengkap());
            stmt.setString(5, admin.getEmail());
            stmt.setDate(6, Date.valueOf(admin.getTanggalDibuat()));
            stmt.setString(7, admin.getStatusAkun());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding admin with username: " + admin.getUsername(), e);
            return false;
        }
    }

    public boolean updateAdmin(Admin admin) {
        String sql = "UPDATE admin SET username = ?, password = ?, role = ?, nama_lengkap = ?, " +
                     "email = ?, status_akun = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, admin.getUsername());
            
            // Only update password if it's not empty
            if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                stmt.setString(2, passwordService.encryptPassword(admin.getPassword()));
            } else {
                // Keep existing password if not being updated
                stmt.setString(2, getAdminById(admin.getId()).getPassword());
            }
            
            stmt.setString(3, admin.getRole());
            stmt.setString(4, admin.getNamaLengkap());
            stmt.setString(5, admin.getEmail());
            stmt.setString(6, admin.getStatusAkun());
            stmt.setInt(7, admin.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating admin with ID: " + admin.getId(), e);
            return false;
        }
    }

    public boolean deleteAdmin(int id) {
        String sql = "DELETE FROM admin WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting admin with ID: " + id, e);
            return false;
        }
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admin ORDER BY username";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password")); // Needed for updates
                admin.setRole(rs.getString("role"));
                admin.setNamaLengkap(rs.getString("nama_lengkap"));
                admin.setEmail(rs.getString("email"));
                admin.setTanggalDibuat(rs.getDate("tanggal_dibuat").toLocalDate());
                admin.setStatusAkun(rs.getString("status_akun"));
                
                admins.add(admin);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all admins", e);
        }
        return admins;
    }


    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setRole(rs.getString("role"));
                admin.setNamaLengkap(rs.getString("nama_lengkap"));
                admin.setEmail(rs.getString("email"));
                admin.setTanggalDibuat(rs.getDate("tanggal_dibuat").toLocalDate());
                admin.setStatusAkun(rs.getString("status_akun"));
                
                return admin;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching admin by ID: " + id, e);
        }
        return null;
    }

    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setRole(rs.getString("role"));
                admin.setNamaLengkap(rs.getString("nama_lengkap"));
                admin.setEmail(rs.getString("email"));
                admin.setTanggalDibuat(rs.getDate("tanggal_dibuat").toLocalDate());
                admin.setStatusAkun(rs.getString("status_akun"));
                
                return admin;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching admin by username: " + username, e);
        }
        return null;
    }

    public boolean usernameExists(String username) {
        return getAdminByUsername(username) != null;
    }
}