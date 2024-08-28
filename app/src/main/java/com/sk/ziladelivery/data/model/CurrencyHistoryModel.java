package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class CurrencyHistoryModel {
    @SerializedName("Message")
    private String Message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("CurrencyCollectionSummaryDcs")
    private ArrayList<CashSummaryDetail> CurrencyCollectionSummaryDcs;

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

    public ArrayList<CashSummaryDetail> getCurrencyCollectionSummaryDcs() {
        return CurrencyCollectionSummaryDcs;
    }

    public void setCurrencyCollectionSummaryDcs(ArrayList<CashSummaryDetail> CurrencyCollectionSummaryDcs) {
        this.CurrencyCollectionSummaryDcs = CurrencyCollectionSummaryDcs;
    }
}
