package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class ReDispatchModel {

    @SerializedName("IsReAttempt")
    boolean IsReAttempt;

    @SerializedName("Reason")
    String Reason;

    @SerializedName("OrderId")
    int OrderId;

    @SerializedName("NextRedispatchedDate")
    String NextRedispatchedDate;

    @SerializedName("lat")
    double lat;

    @SerializedName("lg")
    double lg;

    @SerializedName("TripPlannerConfirmedDetailId")
    long TripPlannerConfirmedDetailId;

    @SerializedName("VideoUrl")
    String VideoUrl = null;


    @Override
    public String toString() {
        return "ReDispatchModel{" +
                "IsReAttempt=" + IsReAttempt +
                ", Reason='" + Reason + '\'' +
                ", OrderId=" + OrderId +
                ", NextRedispatchedDate='" + NextRedispatchedDate + '\'' +
                ", lat=" + lat +
                ", lg=" + lg +
                ", TripPlannerConfirmedDetailId=" + TripPlannerConfirmedDetailId +
                ", VideoUrl='" + VideoUrl + '\'' +
                '}';
    }

    public ReDispatchModel(boolean IsReAttempt, String reason, int orderId, String nextRedispatchedDate, double lat, double lg, long TripPlannerConfirmedDetailId, String VideoUrl) {
        this.IsReAttempt = IsReAttempt;
        this.Reason = reason;
        this.OrderId = orderId;
        this.NextRedispatchedDate = nextRedispatchedDate;
        this.lat = lat;
        this.lg = lg;
        this.TripPlannerConfirmedDetailId = TripPlannerConfirmedDetailId;
        this.VideoUrl = VideoUrl;
    }

    public ReDispatchModel(boolean IsReAttempt, String reason, String nextRedispatchedDate, double lat, double lg, long TripPlannerConfirmedDetailId) {
        this.IsReAttempt = IsReAttempt;
        this.Reason = reason;
        this.NextRedispatchedDate = nextRedispatchedDate;
        this.lat = lat;
        this.lg = lg;
        this.TripPlannerConfirmedDetailId = TripPlannerConfirmedDetailId;
    }
}
