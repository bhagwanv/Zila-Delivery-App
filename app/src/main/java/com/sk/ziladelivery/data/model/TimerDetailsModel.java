package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class TimerDetailsModel {

    @SerializedName("DeliveryIssuanceId")
    public int DeliveryIssuanceId  ;

    public TimerDetailsModel(int deliveryIssuanceId)
    {
        this.DeliveryIssuanceId = deliveryIssuanceId;

    }
}

