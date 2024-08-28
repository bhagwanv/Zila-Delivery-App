package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class CurrencyModel {
    @SerializedName("id")
    public int currencyId;

    @SerializedName("Type")
    public int cyrrencyType;

    @SerializedName("qty")
    public int qty;

    @SerializedName("image")
    public int image;

    @SerializedName("totalAmt")
    public int totalAmt;

    @SerializedName("totalQty")
    public int totalQty;

    public CurrencyModel(int currencyId, int cyrrencyType, int qty, int image) {
        this.currencyId = currencyId;
        this.cyrrencyType = cyrrencyType;
        this.qty = qty;
        this.image = image;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getCyrrencyType() {
        return cyrrencyType;
    }

    public void setCyrrencyType(int cyrrencyType) {
        this.cyrrencyType = cyrrencyType;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getTotalAmt() {
        return totalAmt;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public void setTotalAmt(int totalAmt) {
        this.totalAmt = totalAmt;
    }
}
