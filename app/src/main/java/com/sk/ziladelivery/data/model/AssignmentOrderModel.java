package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AssignmentOrderModel {

    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("Order")
    private ArrayList<OrderModel> orderDelMastAssignments;

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

    public ArrayList<OrderModel> getOrderDelMastAssignments() {
        return orderDelMastAssignments;
    }

    public void setOrderDelMastAssignments(ArrayList<OrderModel> orderDelMastAssignments) {
        this.orderDelMastAssignments = orderDelMastAssignments;
    }
}
