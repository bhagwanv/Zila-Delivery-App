package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class CancelOrderModel {

    @SerializedName("TripPlannerConfirmedDetailId")
    long TripPlannerConfirmedDetailId ;

    @SerializedName("Reason")
    String Reason;

    @SerializedName("OrderId")
    int OrderId;

    @SerializedName("lat")
    double lat;

    @SerializedName("lg")
    double lg;

    public CancelOrderModel(long tripPlannerConfirmedDetailId, String reason, int orderId, double lat, double lg) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        Reason = reason;
        OrderId = orderId;
        this.lat = lat;
        this.lg = lg;
    }

    public long getTripPlannerConfirmedDetailId() {
        return TripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(long tripPlannerConfirmedDetailId) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }
}
