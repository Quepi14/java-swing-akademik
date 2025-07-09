package akademik.auth;

import akademik.config.DatabaseConfig;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseRealm implements Realm {
    
    @Override
    public String getName() {
        return "DatabaseRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = """        
            SELECT username, password FROM admin WHERE username = ? UNION
            SELECT id, password FROM mahasiswa WHERE id = ? UNION
            SELECT id, password FROM dosen WHERE id = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");

                if (!password.equals(dbPassword)) {
                    throw new IncorrectCredentialsException("Password salah!");
                }

                return new SimpleAuthenticationInfo(username, password, getName());
            } else {
                throw new UnknownAccountException("Akun tidak ditemukan!");
            }
        } catch (Exception e) {
            throw new AuthenticationException("Error koneksi ke database", e);
        }
    }
}