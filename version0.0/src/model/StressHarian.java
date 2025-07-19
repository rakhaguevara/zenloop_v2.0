package model;

public class StressHarian {
    private String tanggal;
    private double rataRata;
    private String keterangan;
    private int quest1;
    private int quest2;
    private int quest3;
    private int quest4;
    private int quest5;

    // Constructor
    public StressHarian(String tanggal, double rataRata, String keterangan, int quest1, int quest2, int quest3,
            int quest4, int quest5) {
        this.tanggal = tanggal;
        this.rataRata = rataRata;
        this.keterangan = keterangan;
        this.quest1 = quest1;
        this.quest2 = quest2;
        this.quest3 = quest3;
        this.quest4 = quest4;
        this.quest5 = quest5;

    }

    // Getter methods
    public String getTanggal() {
        return tanggal;
    }

    public double getRataRata() {
        return rataRata;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public int getQuest1() {
        return quest1;
    }

    public int getQuest2() {
        return quest2;
    }

    public int getQuest3() {
        return quest3;
    }

    public int getQuest4() {
        return quest4;
    }

    public int getQuest5() {
        return quest5;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setQuest1(int quest1) {
        this.quest1 = quest1;
    }

    public void setQuest2(int quest2) {
        this.quest2 = quest2;
    }

    public void setQuest3(int quest3) {
        this.quest3 = quest3;
    }

    public void setQuest4(int quest4) {
        this.quest4 = quest4;
    }

    public void setQuest5(int quest5) {
        this.quest5 = quest5;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setRataRata(double rataRata) {
        this.rataRata = rataRata;
    }

}
