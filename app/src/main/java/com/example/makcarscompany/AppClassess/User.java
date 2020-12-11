package com.example.makcarscompany.AppClassess;

import java.util.ArrayList;

public class User {

    private String FullName;
    private String username;
    private String email;
    private String password;
    private long    Phonenb;

    private ArrayList<Long> Cart;

    public User(){

    }

    public User(String FullName, String username, String email, long Phonenb, String password) {
        this.FullName= FullName;
        this.username = username;
        this.email = email;
        this.Phonenb = Phonenb;
        this.password = password;
        this.Cart=new ArrayList<>();
    }
    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName){this.FullName= FullName; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhonenb() {
        return Phonenb;
    }

    public void setPhonenb(long phonenb) {
        Phonenb = phonenb;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Long> getCart() {
        return Cart;
    }

    public void setCart(ArrayList<Long> cart) {
        Cart = cart;
    }

}
