package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryIssuanceM {

    @SerializedName("DeliveryIssuanceId")
    int DeliveryIssuanceId;

    @SerializedName("ReasonOfreject")
    String RejectReason;

    @SerializedName("PeopleID")
    private int peopleID;

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public String getRejectReason() {
        return RejectReason;
    }

    public void setRejectReason(String rejectReason) {
        RejectReason = rejectReason;
    }

    public int getPeopleID() {
        return peopleID;
    }

    public void setPeopleID(int peopleID) {
        this.peopleID = peopleID;
    }

    public DeliveryIssuanceM(int deliveryIssuanceId, String rejectReason, int peopleID) {
        DeliveryIssuanceId = deliveryIssuanceId;
        RejectReason = rejectReason;
        this.peopleID = peopleID;
    }
}
