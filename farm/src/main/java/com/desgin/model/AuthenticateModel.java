package com.desgin.model;

public class AuthenticateModel {
    
    String name;
    String mail;
    String num;
    String password;
    String role;
    String town;
    String district;
    String state;
    String pincode;

    public AuthenticateModel() {}

    public AuthenticateModel(String name, String mail, String num, String password, String role) {
        this.name = name;
        this.mail = mail;
        this.num = num;
        this.password = password;
        this.role = role;
    }

    public AuthenticateModel(String name, String mail, String num, String password, String role, String town, String district, String state, String pincode) {
        this.name = name;
        this.mail = mail;
        this.num = num;
        this.password = password;
        this.role = role;
        this.town = town;
        this.district = district;
        this.state = state;
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
