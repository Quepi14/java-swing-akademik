package akademik.model;

public class Dosen {
    private int id;
    private String nama;
    private String pendidikan;
    private String mataKuliah;
    private String email;
    private String telepon;
    
    public Dosen() {}
    
    public Dosen(int id, String nama, String pendidikan, String mataKuliah) {
        this.id = id;
        this.nama = nama;
        this.pendidikan = pendidikan;
        this.mataKuliah = mataKuliah;
    }
    
    public Dosen(String nama, String pendidikan, String mataKuliah, String email, String telepon) {
        this.nama = nama;
        this.pendidikan = pendidikan;
        this.mataKuliah = mataKuliah;
        this.email = email;
        this.telepon = telepon;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getPendidikan() { return pendidikan; }
    public void setPendidikan(String pendidikan) { this.pendidikan = pendidikan; }
    
    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
}