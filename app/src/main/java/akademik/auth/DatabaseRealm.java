package akademik.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akademik.config.DatabaseConfig;

public class DatabaseRealm implements Realm {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseRealm.class);

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
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());

        String sql = "SELECT username, password FROM admin WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Password validation
                if (!password.equals(storedPassword)) {
                    logger.warn("Failed login attempt for user: {}", username);
                    throw new IncorrectCredentialsException("Invalid password");
                }

                logger.info("Successful login for user: {}", username);
                return new SimpleAuthenticationInfo(
                    username,       // principal
                    storedPassword, // credentials
                    getName()       // realm name
                );
            } else {
                logger.warn("Unknown account attempt: {}", username);
                throw new UnknownAccountException("Account not found");
            }

        } catch (SQLException e) {
            logger.error("Database error during authentication for user: {}", username, e);
            throw new AuthenticationException("Database access error", e);
        }
    }
}