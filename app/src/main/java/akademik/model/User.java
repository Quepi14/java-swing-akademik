package akademik.model;

import java.time.LocalDate;

public class User {
    private final int id;
    private final String username;
    private final String password;
    private final String role;
    private final String namaLengkap;
    private final String email;
    private final LocalDate tanggalDibuat;
    private final boolean active;

    // Constructor - all fields must be initialized here
    public User(int id, String username, String password, String role, 
               String namaLengkap, String email, LocalDate tanggalDibuat, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.tanggalDibuat = tanggalDibuat;
        this.active = active;
    }

    // Getters only (no setters since fields are final)
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getEmail() { return email; }
    public LocalDate getTanggalDibuat() { return tanggalDibuat; }
    public boolean isActive() { return active; }

    // Optional: Add toString() for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", active=" + active +
                '}';
    }
}