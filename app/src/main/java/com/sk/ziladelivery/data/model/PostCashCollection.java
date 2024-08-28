package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class PostCashCollection
{

    @SerializedName("Id")
    private int id;

    @SerializedName("CurrencyDenominationId")
    private int currencyDenominationId;

    @SerializedName("CurrencyCountByDBoy")
    private int currencyCountByDBoy;

    @SerializedName("currencyType")
    private String currencyType;

    @SerializedName("Title")
    private String Title;

    @SerializedName("Value")
    private String Value;



    public PostCashCollection(int id, int currencyDenominationId, int currencyCountByDBoy,String Value,String currencyType,String Title)
    {
        this.id = id;
        this.currencyDenominationId = currencyDenominationId;
        this.currencyCountByDBoy = currencyCountByDBoy;
        this.Value = Value;
        this.currencyType = currencyType;
        this.Title = Title;

    }









    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }



    public int getCurrencyCountByDBoy() {
        return currencyCountByDBoy;
    }

    public void setCurrencyCountByDBoy(int currencyCountByDBoy) {
        this.currencyCountByDBoy = currencyCountByDBoy;
    }

    public int getCurrencyDenominationId() {
        return currencyDenominationId;
    }

    public void setCurrencyDenominationId(int currencyDenominationId) {
        this.currencyDenominationId = currencyDenominationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
