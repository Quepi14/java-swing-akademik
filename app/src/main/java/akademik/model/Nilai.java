package akademik.model;

import java.time.LocalDateTime;

public class Nilai {
    private int id;
    private int idKrs;
    private String kodeMk;
    private String nim;
    private Double nilaiUts;  
    private Double nilaiUas;   
    private Double nilaiAkhir; 
    private String grade;
    private Integer createdBy; 
    private LocalDateTime updatedAt;

    // Constructor
    public Nilai(int id, int idKrs, String kodeMk, String nim, Double nilaiUts, Double nilaiUas, 
                Double nilaiAkhir, String grade, Integer createdBy, LocalDateTime updatedAt) {
        this.id = id;
        this.idKrs = idKrs;
        this.kodeMk = kodeMk;
        this.nim = nim;
        this.nilaiUts = nilaiUts;
        this.nilaiUas = nilaiUas;
        this.nilaiAkhir = nilaiAkhir;
        this.grade = grade;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
    }

    // Minimal constructor
    public Nilai(int idKrs, String kodeMk, String nim, Double nilaiUts, Double nilaiUas, Integer createdBy) {
        this.idKrs = idKrs;
        this.kodeMk = kodeMk;
        this.nim = nim;
        this.nilaiUts = nilaiUts;
        this.nilaiUas = nilaiUas;
        this.createdBy = createdBy;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdKrs() { return idKrs; }
    public void setIdKrs(int idKrs) { this.idKrs = idKrs; }
    
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public Double getNilaiUts() { return nilaiUts; }
    public void setNilaiUts(Double nilaiUts) { this.nilaiUts = nilaiUts; }
    
    public Double getNilaiUas() { return nilaiUas; }
    public void setNilaiUas(Double nilaiUas) { this.nilaiUas = nilaiUas; }
    
    public Double getNilaiAkhir() { return nilaiAkhir; }
    public void setNilaiAkhir(Double nilaiAkhir) { this.nilaiAkhir = nilaiAkhir; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}