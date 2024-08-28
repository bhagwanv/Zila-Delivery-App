package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderConfirmOtpModel {

    @SerializedName("OrderConfirmOtpDc")
    private OrderConfirmModel model;

    @SerializedName("Status")
    private boolean Status;

    @SerializedName("Message")
    private String Message;

    public OrderConfirmModel getModel() {
        return model;
    }

    public void setModel(OrderConfirmModel model) {
        this.model = model;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
