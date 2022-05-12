package com.android.foodorderapp.model;

public class User {
//    Chưa sử dụng


    private String uid;
    private String email;
    private String password;
    private String fname;
    private String lname;
    private String tel;
    private String address;
    private String STK;

    //Constructor

    public User(String uid,String email, String password, String fname, String lname, String tel, String address, String STK) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.tel = tel;
        this.address = address;
        this.STK = STK;
    }

    //Getter / Setter

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSTK() {
        return STK;
    }

    public void setSTK(String STK) {
        this.STK = STK;
    }
}
