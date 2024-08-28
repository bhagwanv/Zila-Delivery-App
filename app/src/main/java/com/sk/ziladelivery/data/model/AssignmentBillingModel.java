package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AssignmentBillingModel {

    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("DeliveryIssuance")
    private ArrayList<DeliveryIssuanceModel> deliveryIssuanceAL;



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

    public ArrayList<DeliveryIssuanceModel> getDeliveryIssuanceAL() {
        return deliveryIssuanceAL;
    }

    public void setDeliveryIssuanceAL(ArrayList<DeliveryIssuanceModel> deliveryIssuanceAL) {
        this.deliveryIssuanceAL = deliveryIssuanceAL;
    }

}
