package iqzal.app.bimbingan.ruangdosen;

public class ListDosen {
    private String ppurl;
    private String fullname;
    private String diruangan;
    private String ruang;
    private String waktu;
    private String catatan;

    public ListDosen() {
    }

    public ListDosen(String ppurl, String fullname, String diruangan, String ruang, String waktu, String catatan) {
        this.ppurl = ppurl;
        this.fullname = fullname;
        this.diruangan = diruangan;
        this.ruang = ruang;
        this.waktu = waktu;
        this.catatan = catatan;
    }

    public String getPpurl() {
        return ppurl;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDiruangan() {
        return diruangan;
    }

    public String getRuang() {
        return ruang;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getCatatan() {
        return catatan;
    }
}
