package akademik.model;

import java.util.Date;

public class Mahasiswa {
    private int nim;
    private String nama;
    private JenisKelamin jenisKelamin;
    private Date ttl;
    private String prodi;
    private String fakultas;
    private int semester;
    private Status status;

    public Mahasiswa(int nim, String nama, JenisKelamin jenisKelamin, Date ttl,
                     String prodi, String fakultas, int semester, Status status) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.ttl = ttl;
        this.prodi = prodi;
        this.fakultas = fakultas;
        this.semester = semester;
        this.status = status;
    }

    public int getNim() {
        return nim;
    }

    public void setNim(int nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
