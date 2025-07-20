package akademik.model;

public class MataKuliah {
    private String kodeMk;
    private String namaMk;
    private int sks;
    private int semester;
    private String deskripsi;

    // Constructors
    public MataKuliah() {}

    public MataKuliah(String kodeMk, String namaMk, int sks, int semester, String deskripsi) {
        this.kodeMk = kodeMk;
        this.namaMk = namaMk;
        this.sks = sks;
        this.semester = semester;
        this.deskripsi = deskripsi;
    }

    // Getters and setters
    public String getKodeMk() { return kodeMk; }
    public String getNamaMk() { return namaMk; }
    public int getSks() { return sks; }
    public int getSemester() { return semester; }
    public String getDeskripsi() { return deskripsi; }
    
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    public void setSks(int sks) { this.sks = sks; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}