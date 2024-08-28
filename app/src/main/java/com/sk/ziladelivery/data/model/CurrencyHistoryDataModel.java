package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class CurrencyHistoryDataModel {

    @SerializedName("Message")
    private String Message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("CurrencyCollectionDc")
    private CurrencyCollectionDc CurrencyCollectionDc;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CurrencyCollectionDc getCurrencyCollectionDc() {
        return CurrencyCollectionDc;
    }

    public void setCurrencyCollectionDc(CurrencyCollectionDc CurrencyCollectionDc) {
        this.CurrencyCollectionDc = CurrencyCollectionDc;
    }


}
