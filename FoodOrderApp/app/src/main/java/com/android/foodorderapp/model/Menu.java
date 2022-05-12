package com.android.foodorderapp.model;

import android.icu.text.DecimalFormat;
import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {
    private String name;
    private float price;
    private int totalInCart;
    private String url;

    public int getTotalInCart() {
        return totalInCart;
    }

    public void setTotalInCart(int totalInCart) {
        this.totalInCart = totalInCart;
    }

    //Constructor


    public Menu(String name, float price, int totalInCart, String url) {
        this.name = name;
        this.price = price;
        this.totalInCart = totalInCart;
        this.url = url;
    }

    protected Menu(Parcel in) {
        name = in.readString();
        price = in.readFloat();
        url = in.readString();
        totalInCart = in.readInt();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toVND(float value) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeString(url);
        dest.writeInt(totalInCart);
    }
}
