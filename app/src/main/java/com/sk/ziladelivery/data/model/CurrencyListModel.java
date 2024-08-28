package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class CurrencyListModel {

    @SerializedName("Message")
     String Message;

    @SerializedName("status")
     boolean status;

    @SerializedName("CurrencyDenomination")
    ArrayList<CurrencyDenomination> CurrencyDenomination;




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

    public ArrayList<CurrencyDenomination> getCurrencyDenomination() {
        return CurrencyDenomination;
    }

    public void setCurrencyDenomination(ArrayList<CurrencyDenomination> CurrencyDenomination) {
        this.CurrencyDenomination = CurrencyDenomination;
    }



}
