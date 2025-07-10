package akademik.config;

import akademik.model.Admin;
import akademik.service.AdminService;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class DatabaseRealm extends AuthorizingRealm {

    private AdminService adminService = new AdminService();

    /**
     * Memberikan hak akses (authorization) ke user
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // Karena hanya admin, kita set role dan permission statis
        info.addRole("admin");
        info.addStringPermission("admin:*");
        
        return info;
    }

    /**
     * Melakukan proses login dan verifikasi password (authentication)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();
        String inputPassword = new String(userToken.getPassword());

        if (username == null || inputPassword == null) {
            throw new AccountException("Username dan Password tidak boleh kosong.");
        }

        Admin admin = adminService.getAdminByUsername(username);

        if (admin == null) {
            throw new UnknownAccountException("Admin tidak ditemukan.");
        }

        if (!admin.getPassword().equals(inputPassword)) {
            throw new IncorrectCredentialsException("Password salah.");
        }

        return new SimpleAuthenticationInfo(
            admin.getUsername(), // principal
            admin.getPassword(), // credentials
            getName()            // realm name
        );
    }
}
