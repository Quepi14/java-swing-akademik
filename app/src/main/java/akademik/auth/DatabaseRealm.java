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

        String sql = "SELECT username, password FROM admin WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (!password.equals(storedPassword)) {
                    throw new IncorrectCredentialsException("Password salah!");
                }

                return new SimpleAuthenticationInfo(username, storedPassword, getName());
            } else {
                throw new UnknownAccountException("Akun tidak ditemukan!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException("Terjadi kesalahan saat mengakses database.", e);
        }
    }
}
