package akademik.model;

public class Mahasiswa {
    private int id;
    private String nama;
    private String kelas;
    private String mataKuliah;
    private String nim;
    private String jenisKelamin;
    private String email;
    private String telepon;
    
    public Mahasiswa() {}
    
    public Mahasiswa(int id, String nama, String kelas, String mataKuliah) {
        this.id = id;
        this.nama = nama;
        this.kelas = kelas;
        this.mataKuliah = mataKuliah;
    }
    
    public Mahasiswa(String nama, String kelas, String mataKuliah, String nim, 
                    String jenisKelamin, String email, String telepon) {
        this.nama = nama;
        this.kelas = kelas;
        this.mataKuliah = mataKuliah;
        this.nim = nim;
        this.jenisKelamin = jenisKelamin;
        this.email = email;
        this.telepon = telepon;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    
    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
    
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
}