package akademik.app;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;

import akademik.auth.DatabaseRealm;
import akademik.gui.LoginForm;

public class App {
    public static void main(String[] args) {
        // Inisialisasi Shiro menggunakan DatabaseRealm
        DatabaseRealm databaseRealm = new DatabaseRealm();
        DefaultSecurityManager securityManager = new DefaultSecurityManager(databaseRealm);
        SecurityUtils.setSecurityManager(securityManager);

        // Tampilkan GUI login
        new LoginForm().setVisible(true);
    }
}
