package akademik.model;

public class Kelas {
    private int id;
    private String kodeMk;
    private int idDosen;
    private String tahunAjaran;
    private String semester;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String ruangan;

    // Constructors
    public Kelas() {}

    public Kelas(int id, String kodeMk, int idDosen, String tahunAjaran, 
                String semester, String hari, String jamMulai, 
                String jamSelesai, String ruangan) {
        this.id = id;
        this.kodeMk = kodeMk;
        this.idDosen = idDosen;
        this.tahunAjaran = tahunAjaran;
        this.semester = semester;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.ruangan = ruangan;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getKodeMk() { return kodeMk; }
    public int getIdDosen() { return idDosen; }
    public String getTahunAjaran() { return tahunAjaran; }
    public String getSemester() { return semester; }
    public String getHari() { return hari; }
    public String getJamMulai() { return jamMulai; }
    public String getJamSelesai() { return jamSelesai; }
    public String getRuangan() { return ruangan; }
    
    public void setId(int id) { this.id = id; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    public void setIdDosen(int idDosen) { this.idDosen = idDosen; }
    public void setTahunAjaran(String tahunAjaran) { this.tahunAjaran = tahunAjaran; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setHari(String hari) { this.hari = hari; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }
}