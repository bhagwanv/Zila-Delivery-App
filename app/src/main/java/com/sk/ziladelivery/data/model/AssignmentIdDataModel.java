package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public  class AssignmentIdDataModel implements Serializable {
    @Expose
    @SerializedName("DeliveryIssuanceDcs")
    private ArrayList<AssignmentID> DeliveryIssuanceDcs;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public ArrayList<AssignmentID> getDeliveryIssuanceDcs() {
        return DeliveryIssuanceDcs;
    }

    public void setDeliveryIssuanceDcs(ArrayList<AssignmentID> DeliveryIssuanceDcs) {
        this.DeliveryIssuanceDcs = DeliveryIssuanceDcs;
    }

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
}
