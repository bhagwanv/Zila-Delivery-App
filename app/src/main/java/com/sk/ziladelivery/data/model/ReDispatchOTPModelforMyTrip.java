package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class ReDispatchOTPModelforMyTrip {
    @SerializedName("Otp")
    String Otp;
    @SerializedName("TripPlannerConfirmedDetailId")
    long TripPlannerConfirmedDetailId;

    @SerializedName("Reason")
    String Reason;
    @SerializedName("lat")
    double lat;
    @SerializedName("lg")
    double lg;
    @SerializedName("NextRedispatchedDate")
    String NextRedispatchedDate;

    @SerializedName("ReAttempt")
    boolean ReAttempt;
    @SerializedName("userId")
    private  int userId;

    public ReDispatchOTPModelforMyTrip(String otp, int tripPlannerConfirmedDetailId,
                                       String comments, double deliveryLat, double deliveryLng,
                                       String nextRedispatchedDate, boolean reAttempt, int userId) {
        this.Otp = otp;
        this.TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        this.Reason = comments;
        this.lat = deliveryLat;
        this.lg = deliveryLng;
        this.NextRedispatchedDate = nextRedispatchedDate;
        this.ReAttempt = reAttempt;
        this.userId = userId;
    }
}
