package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AssignmentAcceptModel {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Status")
    private boolean status;

    @SerializedName("CheckAssignmentEwayBillAndIRNno")
    private ArrayList<AssignmentEwayBillModel> ewayBillModels;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<AssignmentEwayBillModel> getEwayBillModels() {
        return ewayBillModels;
    }

    public void setEwayBillModels(ArrayList<AssignmentEwayBillModel> ewayBillModels) {
        this.ewayBillModels = ewayBillModels;
    }
}
