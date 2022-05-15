package com.android.foodorderapp.model;

public class User {
//    Chưa sử dụng


    private String userid;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phno;
    private String address;
    private String numberaccount;

    //Constructor

    public User(String userid, String email, String password, String firstname, String lastname, String phno, String address, String numberaccount) {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phno = phno;
        this.address = address;
        this.numberaccount = numberaccount;
    }


    //Getter / Setter

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberaccount() {
        return numberaccount;
    }

    public void setNumberaccount(String numberaccount) {
        this.numberaccount = numberaccount;
    }
}
