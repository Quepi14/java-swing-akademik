package akademik.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;

public class ShiroConfig {
    public static void initializeShiro() {
        Realm realm = new DatabaseRealm();
        DefaultSecurityManager securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);
    }
}
