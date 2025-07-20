package akademik.model;

import java.time.LocalDate;

public class Mahasiswa {
    private int id;
    private String nim;
    private String namaLengkap;
    private JenisKelamin jenisKelamin;
    private String alamat;
    private String noTelp;
    private String email;
    private String username;
    private String password;
    private String statusAkun;
    private LocalDate tanggalMasuk;
    private int idDosenWali;

    // Constructors
    public Mahasiswa() {}

    public Mahasiswa(int id, String nim, String namaLengkap, JenisKelamin jenisKelamin,
                    String alamat, String noTelp, String email, String username,
                    String password, String statusAkun, LocalDate tanggalMasuk, int idDosenWali) {
        this.id = id;
        this.nim = nim;
        this.namaLengkap = namaLengkap;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.email = email;
        this.username = username;
        this.password = password;
        this.statusAkun = statusAkun;
        this.tanggalMasuk = tanggalMasuk;
        this.idDosenWali = idDosenWali;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getNim() { return nim; }
    public String getNamaLengkap() { return namaLengkap; }
    public JenisKelamin getJenisKelamin() { return jenisKelamin; }
    public String getAlamat() { return alamat; }
    public String getNoTelp() { return noTelp; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getStatusAkun() { return statusAkun; }
    public LocalDate getTanggalMasuk() { return tanggalMasuk; }
    public int getIdDosenWali() { return idDosenWali; }
    
    public void setId(int id) { this.id = id; }
    public void setNim(String nim) { this.nim = nim; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public void setJenisKelamin(JenisKelamin jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setStatusAkun(String statusAkun) { this.statusAkun = statusAkun; }
    public void setTanggalMasuk(LocalDate tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }
    public void setIdDosenWali(int idDosenWali) { this.idDosenWali = idDosenWali; }
}