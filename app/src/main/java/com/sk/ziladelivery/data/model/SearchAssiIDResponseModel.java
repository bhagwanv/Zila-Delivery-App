package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchAssiIDResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("DI")
    public ArrayList<AssinmentDetail> DI;


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<AssinmentDetail> getDI() {
        return DI;
    }

    public void setDI(ArrayList<AssinmentDetail> DI) {
        this.DI = DI;
    }
}
