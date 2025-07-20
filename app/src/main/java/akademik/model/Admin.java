package akademik.model;

import java.time.LocalDate;

public class Admin {
    private int id;
    private String username;
    private String password;
    private String role;
    private String namaLengkap;
    private String email;
    private LocalDate tanggalDibuat;
    private String statusAkun;

    // Constructors
    public Admin() {}

    public Admin(int id, String username, String password, String role, 
                String namaLengkap, String email, LocalDate tanggalDibuat, 
                String statusAkun) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.tanggalDibuat = tanggalDibuat;
        this.statusAkun = statusAkun;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getEmail() { return email; }
    public LocalDate getTanggalDibuat() { return tanggalDibuat; }
    public String getStatusAkun() { return statusAkun; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public void setEmail(String email) { this.email = email; }
    public void setTanggalDibuat(LocalDate tanggalDibuat) { this.tanggalDibuat = tanggalDibuat; }
    public void setStatusAkun(String statusAkun) { this.statusAkun = statusAkun; }

    // Helper methods
    public boolean isActive() {
        return "active".equalsIgnoreCase(statusAkun);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", email='" + email + '\'' +
                ", statusAkun='" + statusAkun + '\'' +
                '}';
    }
}