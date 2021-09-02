package com.mlms.mobilelaundrymanagementsystemadmin.models;

public class ListOfPaketModel {
    String paket_description, paket_name, paket_id;
    Double paket_price;

    public ListOfPaketModel(){ }

    public ListOfPaketModel(String paket_description, String paket_name, String paket_id, Double paket_price) {
        this.paket_description = paket_description;
        this.paket_name = paket_name;
        this.paket_id = paket_id;
        this.paket_price = paket_price;
    }

    public String getPaket_description() {
        return paket_description;
    }

    public void setPaket_description(String paket_description) {
        this.paket_description = paket_description;
    }

    public String getPaket_name() {
        return paket_name;
    }

    public void setPaket_name(String paket_name) {
        this.paket_name = paket_name;
    }

    public String getPaket_id() {
        return paket_id;
    }

    public void setPaket_id(String paket_id) {
        this.paket_id = paket_id;
    }

    public Double getPaket_price() {
        return paket_price;
    }

    public void setPaket_price(Double paket_price) {
        this.paket_price = paket_price;
    }
}
