package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class ReDispatchOTPModel {

    @SerializedName("Otp")
    String Otp;

    @SerializedName("OrderId")
    int OrderId;
    @SerializedName("TripPlannerConfirmedDetailId")
    long  TripPlannerConfirmedDetailId;
    @SerializedName("DboyMobileNo")
    String  DboyMobileNo;
    @SerializedName("Status")
    String  Status;
    @SerializedName("comments")
    String  comments;
    @SerializedName("DeliveryLat")
    double  DeliveryLat;
    @SerializedName("DeliveryLng")
    double  DeliveryLng;
    @SerializedName("NextRedispatchedDate")
    String  NextRedispatchedDate;
    @SerializedName("userId")
    int  userId;
    @SerializedName("ReAttempt")
    boolean  ReAttempt;

    boolean  IsReturnOrder;

    @Override
    public String toString() {
        return "ReDispatchOTPModel{" +
                "Otp='" + Otp + '\'' +
                ", OrderId=" + OrderId +
                ", TripPlannerConfirmedDetailId=" + TripPlannerConfirmedDetailId +
                ", DboyMobileNo='" + DboyMobileNo + '\'' +
                ", Status='" + Status + '\'' +
                ", comments='" + comments + '\'' +
                ", DeliveryLat=" + DeliveryLat +
                ", DeliveryLng=" + DeliveryLng +
                ", NextRedispatchedDate='" + NextRedispatchedDate + '\'' +
                ", userId=" + userId +
                ", ReAttempt=" + ReAttempt +
                ", IsReturnOrder=" + IsReturnOrder +
                '}';
    }

    public ReDispatchOTPModel(String otp, int orderId, long tripPlannerConfirmedDetailId, String dboyMobileNo,
                              String status, String comments, double deliveryLat, double deliveryLng,
                              String nextRedispatchedDate, int userId, boolean reAttempt, boolean isReturnOrder) {
        Otp = otp;
        OrderId = orderId;
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        DboyMobileNo = dboyMobileNo;
        Status = status;
        this.comments = comments;
        DeliveryLat = deliveryLat;
        DeliveryLng = deliveryLng;
        NextRedispatchedDate = nextRedispatchedDate;
        this.userId = userId;
        ReAttempt = reAttempt;
        IsReturnOrder = isReturnOrder;
    }
}
