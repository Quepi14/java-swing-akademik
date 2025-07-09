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
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("admin");
        info.addStringPermission("admin:*");
        return info;
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) 
            throws AuthenticationException {
        
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        
        if (username == null) {
            throw new UnknownAccountException("Username tidak boleh kosong");
        }
        
        Admin admin = adminService.getAdminByUsername(username);
        if (admin == null) {
            throw new UnknownAccountException("Username tidak ditemukan: " + username);
        }
        
        return new SimpleAuthenticationInfo(username, admin.getPassword(), getName());
    }
}