package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class CancelOrderSendOtpModel {

    @SerializedName("OrderId")
    int OrderId;
    @SerializedName("Otp")
    String Otp;
    @SerializedName("TripPlannerConfirmedDetailId")
    long TripPlannerConfirmedDetailId ;
    @SerializedName("lat")
    double lat;
    @SerializedName("lg")
    double lg;

    public CancelOrderSendOtpModel(int orderId, String otp, long tripPlannerConfirmedDetailId, double lat, double lg) {
        OrderId = orderId;
        Otp = otp;
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        this.lat = lat;
        this.lg = lg;
    }
}
