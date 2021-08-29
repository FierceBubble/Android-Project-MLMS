package com.mlms.mobilelaundrymanagementsystemadmin.models;

public class CabangListModel {
    String cabangName;

    public CabangListModel(){ }

    public CabangListModel(String cabangName) {
        this.cabangName = cabangName;
    }

    public String getCabangName() {
        return cabangName;
    }

    public void setCabangName(String cabangName) {
        this.cabangName = cabangName;
    }
}
