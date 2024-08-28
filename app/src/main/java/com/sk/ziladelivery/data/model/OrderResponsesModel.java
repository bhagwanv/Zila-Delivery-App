package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class OrderResponsesModel {

    @SerializedName("Status")
    private boolean Status;

    @SerializedName("Message")
    private String Message;

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
