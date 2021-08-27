package com.mlms.mobilelaundrymanagementsystemadmin.models;

import java.io.Serializable;

public class LaundryModel implements Serializable {

    String billID, customerName, employeeID, paymentMethod, status, start_date, start_time, status_date, status_time;
    Double totalPrice, totalWeight;
    int total_additional_qty;

    public LaundryModel(){ }

    public LaundryModel(String billID, String customerName, String employeeID, String paymentMethod, String status, String start_date, String start_time, String status_date, String status_time, Double totalPrice, Double totalWeight, int total_additional_qty) {
        this.billID=billID;
        this.customerName = customerName;
        this.employeeID = employeeID;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.start_date = start_date;
        this.start_time = start_time;
        this.status_date = status_date;
        this.status_time = status_time;
        this.totalPrice = totalPrice;
        this.totalWeight = totalWeight;
        this.total_additional_qty = total_additional_qty;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStatus_date() {
        return status_date;
    }

    public void setStatus_date(String status_date) {
        this.status_date = status_date;
    }

    public String getStatus_time() {
        return status_time;
    }

    public void setStatus_time(String status_time) {
        this.status_time = status_time;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getTotal_additional_qty() {
        return total_additional_qty;
    }

    public void setTotal_additional_qty(int total_additional_qty) {
        this.total_additional_qty = total_additional_qty;
    }
}
