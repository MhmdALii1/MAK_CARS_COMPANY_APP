package com.example.makcarscompany.AppClassess;

public class Payments {

    private String username;
    private String Country;
    private String City;
    private String Address;
    private String NameOnCreditCard;
    private String brand;
    private long cid ;
    private long price;
    private long CardNumber;

    public Payments(){

    }

    public Payments(String Country,String Address,String City,String NameOnCreditCard,long CardNumber,String username, long cid, long price) {

        this.username = username;
        this.cid = cid;
        this.Address=Address;
        this.Country = Country;
        this.City = City;
        this.CardNumber = CardNumber;
        this.NameOnCreditCard = NameOnCreditCard;
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNameOnCreditCard() {
        return NameOnCreditCard;
    }

    public void setNameOnCreditCard(String nameOnCreditCard) {
        NameOnCreditCard = nameOnCreditCard;
    }

    public long getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String tostring() {
        return
                "username: " + username +"\n\n"+
                "Country: " + Country +"\n\n"+
                "City: " + City +"\n\n"+
                "Address: " + Address +"\n\n"+
                "NameOnCreditCard: " + NameOnCreditCard +"\n\n"+
                "brand: " + brand +"\n\n"+
                "cid: " + cid +"\n\n"+
                "price: " + price +"\n\n"+
                "CardNumber: " + CardNumber ;
    }

}
