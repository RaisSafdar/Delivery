package com.example.delivery;

public class OrderVendorModel {
String name,id,orderstatus,cphone,cstore,cfname,ccity,cadress;

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getCstore() {
        return cstore;
    }

    public void setCstore(String cstore) {
        this.cstore = cstore;
    }

    public String getCfname() {
        return cfname;
    }

    public void setCfname(String cfname) {
        this.cfname = cfname;
    }

    public String getCcity() {
        return ccity;
    }

    public void setCcity(String ccity) {
        this.ccity = ccity;
    }

    public String getCadress() {
        return cadress;
    }

    public void setCadress(String cadress) {
        this.cadress = cadress;
    }

    public OrderVendorModel(String name, String id, String orderstatus, String cphone, String cstore, String cfname, String ccity, String cadress) {
        this.name = name;
        this.id = id;
        this.orderstatus = orderstatus;
        this.cphone = cphone;
        this.cstore = cstore;
        this.cfname = cfname;
        this.ccity = ccity;
        this.cadress = cadress;
    }

    public String getName() {
        return name;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public OrderVendorModel() {
    }
}
