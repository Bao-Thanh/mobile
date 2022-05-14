package com.android.foodorderapp.model;

import java.util.List;

public class Orders {

    //unique IDs
    private String orderid;
    private String customerid;
    private String fname;

    //Customer ordering detail
    private String phno;
    private String email;
    private String thanhpho;
    private String quanhuyen;
    private String sonha;
    private String sstk;

    //additional food detail
    private String orderat;
    private String image;
    private List<Menu> menu;
    private float fdelivery;
    private float totalPrice, subTotal;
    private String status;
    private String dateorder;
    private String otp;
    private int isFinish;

    //Constructor
    public Orders() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public Orders(String orderid, String customerid, String fname,
                  String phno, String email, String thanhpho, String quanhuyen,
                  String sonha, String sstk, String orderat, String image,
                  List<Menu> menu, float fdelivery, float totalPrice,
                  String status, String dateorder, String otp, float subTotal, int isFinish) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.fname = fname;
        this.phno = phno;
        this.email = email;
        this.thanhpho = thanhpho;
        this.quanhuyen = quanhuyen;
        this.sonha = sonha;
        this.sstk = sstk;
        this.orderat = orderat;
        this.image = image;
        this.menu = menu;
        this.fdelivery = fdelivery;
        this.totalPrice = totalPrice;
        this.status = status;
        this.dateorder = dateorder;
        this.otp = otp;
        this.subTotal = subTotal;
        this.isFinish = isFinish;
    }

    //Getter setter


    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getFdelivery() {
        return fdelivery;
    }

    public void setFdelivery(float fdelivery) {
        this.fdelivery = fdelivery;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrderat() {
        return orderat;
    }

    public void setOrderat(String orderat) {
        this.orderat = orderat;
    }

    public String getDateorder() {
        return dateorder;
    }

    public void setDateorder(String dateorder) {
        this.dateorder = dateorder;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getThanhpho() {
        return thanhpho;
    }

    public void setThanhpho(String thanhpho) {
        this.thanhpho = thanhpho;
    }

    public String getQuanhuyen() {
        return quanhuyen;
    }

    public void setQuanhuyen(String quanhuyen) {
        this.quanhuyen = quanhuyen;
    }

    public String getSonha() {
        return sonha;
    }

    public void setSonha(String sonha) {
        this.sonha = sonha;
    }

    public String getSstk() {
        return sstk;
    }

    public void setSstk(String sstk) {
        this.sstk = sstk;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
