package com.mlms.mobilelaundrymanagementsystemadmin.models;

public class ListOfItemsModel {
    String item_name, item_type;
    int item_qty;

    public ListOfItemsModel(){ }

    public ListOfItemsModel(String item_name, String item_type, int item_qty) {
        this.item_name = item_name;
        this.item_type = item_type;
        this.item_qty = item_qty;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(int item_qty) {
        this.item_qty = item_qty;
    }
}
