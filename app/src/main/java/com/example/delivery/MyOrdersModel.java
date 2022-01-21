package com.example.delivery;

import android.content.Context;

import java.util.List;

public class MyOrdersModel {
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    String id,date,delivery,vendor_id,name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public MyOrdersModel(String id, String date, String delivery, String vendor_id,String name) {
        this.id = id;
        this.date = date;
        this.delivery = delivery;
        this.vendor_id = vendor_id;
        this.name = name;
    }

    public MyOrdersModel() {
    }
}
