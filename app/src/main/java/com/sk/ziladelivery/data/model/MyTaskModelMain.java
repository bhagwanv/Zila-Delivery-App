package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyTaskModelMain {
    @SerializedName("status")
    private boolean status;
    @SerializedName("IsShippedAssingId")
    private boolean IsShippedAssingId;
    @SerializedName("Message")
    private String Message;
    @SerializedName("TotalOrderCount")
    private int TotalOrderCount;
    @SerializedName("OrderDispatchedObj")
    public ArrayList<MyTaskModel> OrderDispatchedObj;



    public boolean isShippedAssingId() {
        return IsShippedAssingId;
    }

    public void setShippedAssingId(boolean shippedAssingId) {
        IsShippedAssingId = shippedAssingId;
    }
    public int getTotalOrderCount() {
        return TotalOrderCount;
    }

    public void setTotalOrderCount(int totalOrderCount) {
        TotalOrderCount = totalOrderCount;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<MyTaskModel> getOrderDispatchedObj() {
        return OrderDispatchedObj;
    }

    public void setOrderDispatchedObj(ArrayList<MyTaskModel> orderDispatchedObj) {
        OrderDispatchedObj = orderDispatchedObj;
    }
}
