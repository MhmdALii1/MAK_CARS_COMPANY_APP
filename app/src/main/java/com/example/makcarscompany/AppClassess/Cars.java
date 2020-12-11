package com.example.makcarscompany.AppClassess;

import java.io.Serializable;

public class Cars implements Serializable {

    private String brand;
    private String model;
    private String color;
    private String image;
    private String status;
    private long year;
    private long mileage;
    private long price;
    private long cid;

    public Cars(){

    }

    public Cars( String brand,long cid, String color, String image, long mileage, String model, long price,String status,long year) {

        this.brand = brand;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
        this.year=year;
        this.image=image;
        this.cid=cid;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCid() { return cid; }

    public void setCid(long cid) { this.cid = cid; }

    public String getimage() { return image; }

    public void setimage(String image) { this.image = image; }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getyear() { return year; }

    public void setyear(long year) { this.year= year; }

public String tostring(){

        return   "Brand: "+getBrand()+"\n"
                +"Model: "+getModel()+"\n"
                +"Year: "+getyear()+"\n"
                +"Mileage: "+getMileage()+" mpg"+"\n"
                +"Price: "+getPrice()+" $";

}


}
