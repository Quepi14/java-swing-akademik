package akademik.model;

import java.time.LocalDateTime;

public class KRS {
    private int id;
    private int idMahasiswa;
    private int idKelas;
    private LocalDateTime tanggalDaftar;
    private String status;
    private int disetujuiOleh;
    private LocalDateTime tanggalDisetujui;
    private String catatanPenolakan;

    // Constructors
    public KRS() {}

    public KRS(int id, int idMahasiswa, int idKelas, LocalDateTime tanggalDaftar, 
              String status, int disetujuiOleh, LocalDateTime tanggalDisetujui, 
              String catatanPenolakan) {
        this.id = id;
        this.idMahasiswa = idMahasiswa;
        this.idKelas = idKelas;
        this.tanggalDaftar = tanggalDaftar;
        this.status = status;
        this.disetujuiOleh = disetujuiOleh;
        this.tanggalDisetujui = tanggalDisetujui;
        this.catatanPenolakan = catatanPenolakan;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getIdMahasiswa() { return idMahasiswa; }
    public int getIdKelas() { return idKelas; }
    public LocalDateTime getTanggalDaftar() { return tanggalDaftar; }
    public String getStatus() { return status; }
    public int getDisetujuiOleh() { return disetujuiOleh; }
    public LocalDateTime getTanggalDisetujui() { return tanggalDisetujui; }
    public String getCatatanPenolakan() { return catatanPenolakan; }
    
    public void setId(int id) { this.id = id; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }
    public void setIdKelas(int idKelas) { this.idKelas = idKelas; }
    public void setTanggalDaftar(LocalDateTime tanggalDaftar) { this.tanggalDaftar = tanggalDaftar; }
    public void setStatus(String status) { this.status = status; }
    public void setDisetujuiOleh(int disetujuiOleh) { this.disetujuiOleh = disetujuiOleh; }
    public void setTanggalDisetujui(LocalDateTime tanggalDisetujui) { this.tanggalDisetujui = tanggalDisetujui; }
    public void setCatatanPenolakan(String catatanPenolakan) { this.catatanPenolakan = catatanPenolakan; }
}