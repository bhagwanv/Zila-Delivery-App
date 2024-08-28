package com.sk.ziladelivery.ui.views.viewmodels;

public class ItemDetailsInfo {
    String srNo;
    String qty;
    String price;
    String itemName;
    String orderid;
    String shopname;
    String checkamount;
    String cashamount;
    String recivedAmount;
    String status;
    String comments;
    String GrossAmount;
    String ElectronicPaymentNo;
    String ElectronicAmount;

    public String getElectronicPaymentNo() {
        return ElectronicPaymentNo;
    }

    public void setElectronicPaymentNo(String electronicPaymentNo) {
        ElectronicPaymentNo = electronicPaymentNo;
    }

    public String getElectronicAmount() {
        return ElectronicAmount;
    }

    public void setElectronicAmount(String electronicAmount) {
        ElectronicAmount = electronicAmount;
    }

    public String getGrossAmount() {
        return GrossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        GrossAmount = grossAmount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getCheckamount() {
        return checkamount;
    }

    public void setCheckamount(String checkamount) {
        this.checkamount = checkamount;
    }

    public String getCashamount() {
        return cashamount;
    }

    public void setCashamount(String cashamount) {
        this.cashamount = cashamount;
    }

    public String getRecivedAmount() {
        return recivedAmount;
    }

    public void setRecivedAmount(String recivedAmount) {
        this.recivedAmount = recivedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
