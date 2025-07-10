package akademik.model;

import java.util.Date;

public class Dosen {
    private int nip;
    private String nama;
    private String gelar;
    private JenisKelamin jenisKelamin;
    private Date ttl;

    // Constructor lengkap sesuai kebutuhan
    public Dosen(int nip, String nama, String gelar, JenisKelamin jenisKelamin, Date ttl) {
        this.nip = nip;
        this.nama = nama;
        this.gelar = gelar;
        this.jenisKelamin = jenisKelamin;
        this.ttl = ttl;
    }

    // Getter & Setter
    public int getNip() {
        return nip;
    }

    public void setNip(int nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGelar() {
        return gelar;
    }

    public void setGelar(String gelar) {
        this.gelar = gelar;
    }

    public JenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(JenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public Date getTtl() {
        return ttl;
    }

    public void setTtl(Date ttl) {
        this.ttl = ttl;
    }
}
