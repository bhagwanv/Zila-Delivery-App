package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PendingTaskModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("Message")
    private String Message;

    @SerializedName("DeliveryIssuance")
    public ArrayList<PendingModel> DI;


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<PendingModel> getDI() {
        return DI;
    }

    public void setDI(ArrayList<PendingModel> DI) {
        this.DI = DI;
    }
}
