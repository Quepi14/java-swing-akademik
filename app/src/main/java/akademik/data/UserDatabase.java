package akademik.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import akademik.model.Admin;

public class UserDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/akademik";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public boolean createAccount(String username, String password, String role, String identifier) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            if (!isValidIdentifier(identifier, role, conn)) {
                return false;
            }

            final String sql;
            if (role.equalsIgnoreCase("dosen")) {
                sql = "UPDATE dosen SET username = ?, password = SHA2(?, 256), status_akun = 'active' WHERE nidn = ?";
            } else if (role.equalsIgnoreCase("mahasiswa")) {
                sql = "UPDATE mahasiswa SET username = ?, password = SHA2(?, 256), status_akun = 'active' WHERE nim = ?";
            } else {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, identifier);
                return ps.executeUpdate() > 0;
            }
        }
    }

    private boolean isValidIdentifier(String identifier, String role, Connection conn) throws SQLException {
        final String sql;
        if (role.equalsIgnoreCase("dosen")) {
            sql = "SELECT COUNT(*) FROM dosen WHERE nidn = ? AND status_akun = 'inactive'";
        } else if (role.equalsIgnoreCase("mahasiswa")) {
            sql = "SELECT COUNT(*) FROM mahasiswa WHERE nim = ? AND status_akun = 'inactive'";
        } else {
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, identifier);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<UserInfo> getUnregisteredUsers(String role) throws SQLException {
        final String sql;
        if (role.equalsIgnoreCase("dosen")) {
            sql = "SELECT nidn as identifier, nama_lengkap, email FROM dosen WHERE status_akun = 'inactive'";
        } else if (role.equalsIgnoreCase("mahasiswa")) {
            sql = "SELECT nim as identifier, nama_lengkap, email FROM mahasiswa WHERE status_akun = 'inactive'";
        } else {
            return new ArrayList<>();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            List<UserInfo> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new UserInfo(
                    rs.getString("identifier"),
                    rs.getString("nama_lengkap"),
                    rs.getString("email")
                ));
            }
            return users;
        }
    }

    public Admin authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, role, nama_lengkap, email, tanggal_dibuat " +
                     "FROM admin WHERE username = ? AND password = SHA2(?, 256)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setRole(rs.getString("role"));
                    admin.setNamaLengkap(rs.getString("nama_lengkap"));
                    admin.setEmail(rs.getString("email"));
                    admin.setTanggalDibuat(rs.getDate("tanggal_dibuat").toLocalDate());
                    return admin;
                }
            }
        }
        return null;
    }

    public Admin getUserByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, role, nama_lengkap, email, tanggal_dibuat " +
                     "FROM admin WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setRole(rs.getString("role"));
                    admin.setNamaLengkap(rs.getString("nama_lengkap"));
                    admin.setEmail(rs.getString("email"));
                    admin.setTanggalDibuat(rs.getDate("tanggal_dibuat").toLocalDate());
                    return admin;
                }
            }
        }
        return null;
    }

    public static class UserInfo {
        private final String identifier;
        private final String namaLengkap;
        private final String email;

        public UserInfo(String identifier, String namaLengkap, String email) {
            this.identifier = identifier;
            this.namaLengkap = namaLengkap;
            this.email = email;
        }

        public String getIdentifier() { return identifier; }
        public String getNamaLengkap() { return namaLengkap; }
        public String getEmail() { return email; }
    }
}