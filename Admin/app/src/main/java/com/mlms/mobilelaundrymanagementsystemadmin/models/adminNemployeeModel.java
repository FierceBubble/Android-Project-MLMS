package com.mlms.mobilelaundrymanagementsystemadmin.models;

public class adminNemployeeModel {
    String name, password, address, email, role,
            dob, gender, status, doJ, phone, NIK;

    public adminNemployeeModel(){}

    public adminNemployeeModel(String name, String password, String address, String email, String role, String dob, String gender, String status, String doJ, String phone, String NIK) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.email = email;
        this.role = role;
        this.dob = dob;
        this.gender = gender;
        this.status = status;
        this.doJ = doJ;
        this.phone = phone;
        this.NIK = NIK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoJ() {
        return doJ;
    }

    public void setDoJ(String doJ) {
        this.doJ = doJ;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }
}
