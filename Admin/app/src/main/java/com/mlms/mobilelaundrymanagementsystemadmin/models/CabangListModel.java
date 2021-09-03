package com.mlms.mobilelaundrymanagementsystemadmin.models;

public class CabangListModel {
    String cabangName;
    int total_paket;

    public CabangListModel(){ }

    public CabangListModel(String cabangName, int total_paket) {
        this.cabangName = cabangName; this.total_paket=total_paket;
    }

    public String getCabangName() {
        return cabangName;
    }

    public void setCabangName(String cabangName) {
        this.cabangName = cabangName;
    }

    public int getTotal_paket() {
        return total_paket;
    }

    public void setTotal_paket(int total_paket) {
        this.total_paket = total_paket;
    }
}
