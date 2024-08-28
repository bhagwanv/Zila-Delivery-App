package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class AssignmentEwayBillModel {

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("Reson")
    private String Reson;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getReson() {
        return Reson;
    }

    public void setReson(String reson) {
        Reson = reson;
    }
}
