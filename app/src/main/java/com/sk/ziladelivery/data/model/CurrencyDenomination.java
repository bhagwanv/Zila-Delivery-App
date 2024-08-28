package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class CurrencyDenomination implements Comparable<CurrencyDenomination> {
    public static final int HEADER = 0;
    public static final int VIEW = 1;

    @SerializedName("Title")
    private String Title;
    @SerializedName("currencyImage")
    private String currencyImage;
    @SerializedName("currencyType")
    private String currencyType;
    @SerializedName("Id")
    private int Id;
    public int totalAmt;
    public int totalQty;
    @SerializedName("Value")
    public String Value;
    private boolean isSectionHeader;
    @SerializedName("CurrencyCountByDBoy")
    private int currencyCountByDBoy;

    public int getCurrencyCountByDBoy() {
        return currencyCountByDBoy;
    }

    public void setCurrencyCountByDBoy(int currencyCountByDBoy) {
        this.currencyCountByDBoy = currencyCountByDBoy;
    }

    public CurrencyDenomination(String title, String currencyImage, String currencyType, int id) {
        Title = title;
        this.currencyImage = currencyImage;
        this.currencyType = currencyType;
        Id = id;
    }

    public CurrencyDenomination(String title, String currencyImage, String currencyType, int id, int currencyCountByDBoy) {
        this.Title = title;
        this.currencyImage = currencyImage;
        this.currencyType = currencyType;
        this.Id = id;
        this.currencyCountByDBoy = currencyCountByDBoy;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(int totalAmt) {
        this.totalAmt = totalAmt;
    }

    public void setToSectionHeader() {
        isSectionHeader = true;
    }

    public boolean isSectionHeader() {
        return isSectionHeader;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCurrencyImage() {
        return currencyImage;
    }

    public void setCurrencyImage(String currencyImage) {
        this.currencyImage = currencyImage;
    }

    @Override
    public int compareTo(CurrencyDenomination other) {
        return other.currencyType.compareTo(currencyType);
    }
}