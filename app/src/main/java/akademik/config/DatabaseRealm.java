package akademik.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseRealm extends AuthenticatingRealm {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseRealm.class);

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = getPasswordFromDatabase(username);

        if (password == null) {
            logger.warn("Login attempt for non-existent user: {}", username);
            throw new UnknownAccountException("User not found");
        }

        return new SimpleAuthenticationInfo(username, password, getName());
    }

    private String getPasswordFromDatabase(String username) throws AuthenticationException {
        String sql = "SELECT password FROM user WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/akademik", "root", "");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("password");
            }
            return null;
            
        } catch (SQLException e) {
            logger.error("Database error while fetching password for user: {}", username, e);
            throw new AuthenticationException("Database access error", e);
        }
    }
}