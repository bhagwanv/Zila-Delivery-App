package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CollectPaymentRequestModel implements Serializable {

    @SerializedName("TripPlannerConfirmedOrderId")
    private int TripPlannerConfirmedOrderId;

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("RemaningOrderAmount")
    private double RemaningOrderAmount;

    @SerializedName("PaymentMode")
    private String PaymentMode;

    @SerializedName("PaymentRefId")
    private String PaymentRefId;


    public CollectPaymentRequestModel(int tripPlannerConfirmedOrderId, int orderId, double remaningOrderAmount, String paymentMode, String paymentRefId) {
        TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
        OrderId = orderId;
        RemaningOrderAmount = remaningOrderAmount;
        PaymentMode = paymentMode;
        PaymentRefId = paymentRefId;
    }

    public int getTripPlannerConfirmedOrderId() {
        return TripPlannerConfirmedOrderId;
    }

    public void setTripPlannerConfirmedOrderId(int tripPlannerConfirmedOrderId) {
        TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getRemaningOrderAmount() {
        return RemaningOrderAmount;
    }

    public void setRemaningOrderAmount(double remaningOrderAmount) {
        RemaningOrderAmount = remaningOrderAmount;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getPaymentRefId() {
        return PaymentRefId;
    }

    public void setPaymentRefId(String paymentRefId) {
        PaymentRefId = paymentRefId;
    }
}
