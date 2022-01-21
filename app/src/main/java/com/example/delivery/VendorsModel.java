package com.example.delivery;

public class VendorsModel {
    public VendorsModel() {
    }

    public VendorsModel(String name, String storename) {
        this.name = name;
        this.storename = storename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    String name, storename;
}
