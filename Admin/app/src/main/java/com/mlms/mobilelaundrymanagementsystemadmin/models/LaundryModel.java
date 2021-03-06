package com.mlms.mobilelaundrymanagementsystemadmin.models;

import java.io.Serializable;
import java.util.List;

public class LaundryModel implements Serializable {

    String billID, customerName, employeeID, paymentMethod, status, start_date, start_time, status_date, status_time, employeeName;
    List<String> paket_choice;
    Double totalPrice, totalWeight;
    int total_additional_qty, total_qty, customerPhone;

    public LaundryModel(){ }

    public LaundryModel(List<String> paket_choice, String billID, String customerName, String employeeID, String employeeName, String paymentMethod, String status, String start_date, String start_time, String status_date, String status_time, Double totalPrice, Double totalWeight, int total_additional_qty, int total_qty, int customerPhone) {
        this.paket_choice=paket_choice;
        this.billID=billID;
        this.customerName = customerName;
        this.employeeID = employeeID;
        this.employeeName=employeeName;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.start_date = start_date;
        this.start_time = start_time;
        this.status_date = status_date;
        this.status_time = status_time;
        this.totalPrice = totalPrice;
        this.totalWeight = totalWeight;
        this.total_additional_qty = total_additional_qty;
        this.total_qty=total_qty;
        this.customerPhone=customerPhone;
    }

    public List<String> getPaket_choice() {
        return paket_choice;
    }

    public void setPaket_choice(List<String> paket_choice) {
        this.paket_choice = paket_choice;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public int getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public int getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(int customerPhone) {
        this.customerPhone = customerPhone;
    }
}
