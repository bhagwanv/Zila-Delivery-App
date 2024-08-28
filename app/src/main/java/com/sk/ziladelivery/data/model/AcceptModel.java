package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class AcceptModel {

    @SerializedName("DeliveryIssuanceId")
    int DeliveryIssuanceId;

    @SerializedName("Acceptance")
    String Acceptance;

    @SerializedName("RejectReason")
    String RejectReason;

    public AcceptModel(int deliveryIssuanceId, String acceptance, String rejectReason) {
        DeliveryIssuanceId = deliveryIssuanceId;
        Acceptance = acceptance;
        RejectReason = rejectReason;
    }
}
