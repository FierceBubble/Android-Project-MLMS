package com.mlms.mobilelaundrymanagementsystemadmin.models;

public class ListOfItemsModel {
    String item_name;
    int item_qty;

    public ListOfItemsModel(){ }

    public ListOfItemsModel(String item_name, int item_qty) {
        this.item_name = item_name;
        this.item_qty = item_qty;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(int item_qty) {
        this.item_qty = item_qty;
    }
}
