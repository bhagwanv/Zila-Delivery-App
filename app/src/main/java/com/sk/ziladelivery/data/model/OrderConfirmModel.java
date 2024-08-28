package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderConfirmModel {

    @SerializedName("Otp")
    private String Otp;

    @SerializedName("TripPlannerConfirmedDetailId")
    private int TripPlannerConfirmedDetailId;

    @SerializedName("DboyMobileNo")
    private String DboyMobileNo;

    @SerializedName("Status")
    private String Status;

    @SerializedName("comments")
    private String comments;

    @SerializedName("DeliveryLat")
    private double DeliveryLat;

    @SerializedName("DeliveryLng")
    private double DeliveryLng;

    @SerializedName("NextRedispatchedDate")
    private String NextRedispatchedDate;

    @SerializedName("userId")
    private int userId;

    @SerializedName("ReAttempt")
    private boolean ReAttempt;

    public OrderConfirmModel(String otp, int tripPlannerConfirmedDetailId, String dboyMobileNo, String status, String comments, double deliveryLat, double deliveryLng, String nextRedispatchedDate, int userId, boolean reAttempt) {
        Otp = otp;
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        DboyMobileNo = dboyMobileNo;
        Status = status;
        this.comments = comments;
        DeliveryLat = deliveryLat;
        DeliveryLng = deliveryLng;
        NextRedispatchedDate = nextRedispatchedDate;
        this.userId = userId;
        ReAttempt = reAttempt;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public int getTripPlannerConfirmedDetailId() {
        return TripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }

    public String getDboyMobileNo() {
        return DboyMobileNo;
    }

    public void setDboyMobileNo(String dboyMobileNo) {
        DboyMobileNo = dboyMobileNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getDeliveryLat() {
        return DeliveryLat;
    }

    public void setDeliveryLat(double deliveryLat) {
        DeliveryLat = deliveryLat;
    }

    public double getDeliveryLng() {
        return DeliveryLng;
    }

    public void setDeliveryLng(double deliveryLng) {
        DeliveryLng = deliveryLng;
    }

    public String getNextRedispatchedDate() {
        return NextRedispatchedDate;
    }

    public void setNextRedispatchedDate(String nextRedispatchedDate) {
        NextRedispatchedDate = nextRedispatchedDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isReAttempt() {
        return ReAttempt;
    }

    public void setReAttempt(boolean reAttempt) {
        ReAttempt = reAttempt;
    }
}
