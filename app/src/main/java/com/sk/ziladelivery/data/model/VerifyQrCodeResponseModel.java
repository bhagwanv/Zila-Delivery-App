package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class VerifyQrCodeResponseModel {

    public boolean isCaptured() {
        return IsCaptured;
    }

    public String getDeliveryBoyFcmId() {
        return DeliveryBoyFcmId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getAmountCaptured() {
        return AmountCaptured;
    }

    @SerializedName("IsCaptured")
    private boolean IsCaptured;

    @SerializedName("DeliveryBoyFcmId")
    private String DeliveryBoyFcmId;

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("AmountCaptured")
    private int AmountCaptured;
}
