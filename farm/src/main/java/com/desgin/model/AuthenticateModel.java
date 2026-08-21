package com.desgin.model;

public class AuthenticateModel {
    
    String name;
    String mail;
    String num;
    String password;
    String role;

    public AuthenticateModel() {}

    public AuthenticateModel(String name,String mail,String num,String password,String role) {
        
        this.name = name;
        this.mail = mail;
        this.num = num;
        this.password = password;
        this.role = role;
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

    
    
}
