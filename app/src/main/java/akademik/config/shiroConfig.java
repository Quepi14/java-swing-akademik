package akademik.config;

import akademik.auth.DatabaseRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.mgt.SecurityManager;

/**
 * Konfigurasi Shiro untuk aplikasi akademik
 */
public class ShiroConfig {

    /**
     * Inisialisasi Shiro dengan realm DatabaseRealm
     */
    public static void initializeShiro() {
        // Buat instance realm kustom: DatabaseRealm
        Realm realm = new DatabaseRealm();

        // Inisialisasi DefaultSecurityManager dan set realm-nya
        SecurityManager securityManager = new DefaultSecurityManager(realm);

        // Set securityManager ke SecurityUtils agar bisa diakses global
        SecurityUtils.setSecurityManager(securityManager);
    }
}
