package com.example.delivery;

public class HistoryModel {
    String id,date,price,delivery,vendorname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }



    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }



    public HistoryModel(String id, String date, String price,String delivery,String vendorname) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.delivery = delivery;
        this.vendorname = vendorname;
    }

    public HistoryModel() {
    }
}
