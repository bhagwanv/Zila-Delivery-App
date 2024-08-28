package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class PaymentSubmitResponce {

    @SerializedName("Status")
    private boolean status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("TripPlannerConfirmedDetailId")
    private  int TripPlannerConfirmedDetailId;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getTripPlannerConfirmedDetailId() {
        return TripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }
}
