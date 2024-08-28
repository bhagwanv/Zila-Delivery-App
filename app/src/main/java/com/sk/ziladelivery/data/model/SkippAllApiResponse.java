package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class SkippAllApiResponse {
    @SerializedName("TripDashboardDC")
    String TripDashboardDC;

    @SerializedName("Status")
    boolean Status;

    @SerializedName("Message")
    private String Message;

    public String getTripDashboardDC() {
        return TripDashboardDC;
    }

    public void setTripDashboardDC(String tripDashboardDC) {
        TripDashboardDC = tripDashboardDC;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
