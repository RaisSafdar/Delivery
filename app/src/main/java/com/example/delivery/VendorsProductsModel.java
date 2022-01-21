package com.example.delivery;

public class VendorsProductsModel {
    String image,productname,status,id;

    public VendorsProductsModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VendorsProductsModel(String image, String productname, String status,String id) {
        this.image = image;
        this.productname = productname;
        this.status = status;
        this.id =id;
    }
}
