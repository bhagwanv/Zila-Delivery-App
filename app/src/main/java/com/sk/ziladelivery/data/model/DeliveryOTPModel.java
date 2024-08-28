package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryOTPModel {

    @SerializedName("TripplannerConfirmdetailedId")
    private int TripplannerConfirmdetailedId;

    @SerializedName("Status")
    private String Status;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lg")
    private double lg;

    public DeliveryOTPModel(int tripplannerConfirmdetailedId, String status, double lat, double lg) {
        TripplannerConfirmdetailedId = tripplannerConfirmdetailedId;
        Status = status;
        this.lat = lat;
        this.lg = lg;
    }

    public int getTripplannerConfirmdetailedId() {
        return TripplannerConfirmdetailedId;
    }

    public void setTripplannerConfirmdetailedId(int tripplannerConfirmdetailedId) {
        TripplannerConfirmdetailedId = tripplannerConfirmdetailedId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
