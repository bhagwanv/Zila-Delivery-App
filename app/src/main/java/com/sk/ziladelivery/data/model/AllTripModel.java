package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllTripModel {
    @Expose
    @SerializedName("ZilaTripMasterId")
    private int ZilaTripMasterId;

    @Expose
    @SerializedName("DboyId")
    private int DboyId;

    @Expose
    @SerializedName("OrderCount")
    private int OrderCount;

    @Expose
    @SerializedName("TotalAmount")
    private Double TotalAmount;


    @Expose
    @SerializedName("TripCurrentStatus")
    private String TripCurrentStatus;

    @Expose
    @SerializedName("IsFreezed")
    private boolean IsFreezed;

    public boolean getIsFreezed() {
        return IsFreezed;
    }

    public void setIsFreezed(boolean isFreezed) {
        IsFreezed = isFreezed;
    }

    public int getZilaTripMasterId() {
        return ZilaTripMasterId;
    }

    public void setZilaTripMasterId(int zilaTripMasterId) {
        ZilaTripMasterId = zilaTripMasterId;
    }

    public int getDboyId() {
        return DboyId;
    }

    public void setDboyId(int dboyId) {
        DboyId = dboyId;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getTripCurrentStatus() {
        return TripCurrentStatus;
    }

    public void setTripCurrentStatus(String tripCurrentStatus) {
        TripCurrentStatus = tripCurrentStatus;
    }
}
