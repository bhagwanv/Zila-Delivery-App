package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class AssignmentSetModel {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Status")
    private boolean Status;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
