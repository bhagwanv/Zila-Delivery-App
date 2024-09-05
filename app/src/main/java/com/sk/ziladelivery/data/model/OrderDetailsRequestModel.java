package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsRequestModel {

    @Expose
    @SerializedName("zilaTripDetailId")
    private int tripPlannerConfirmedDetailId;

    @Expose
    @SerializedName("OrderId")
    private List<Integer> OrderId;

    public OrderDetailsRequestModel(int tripPlannerConfirmedDetailId, List<Integer> orderId) {
        this.tripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        OrderId = orderId;
    }

    public int getTripPlannerConfirmedDetailId() {
        return tripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
        this.tripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }

    public List<Integer> getOrderId() {
        return OrderId;
    }

    public void setOrderId(List<Integer> orderId) {
        OrderId = orderId;
    }
}
