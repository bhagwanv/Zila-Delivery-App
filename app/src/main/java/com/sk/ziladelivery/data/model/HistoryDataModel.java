
package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class HistoryDataModel {


    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("OrderHistory")
    private ArrayList<OrderHistory> OrderHistory;

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

    public ArrayList<OrderHistory> getOrderHistory() {
        return OrderHistory;
    }

    public void setOrderHistory(ArrayList<OrderHistory> OrderHistory) {
        this.OrderHistory = OrderHistory;
    }
}
