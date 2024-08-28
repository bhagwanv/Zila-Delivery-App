package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class PostUnloadingModel {

    @SerializedName("TripPlannerConfirmedMasterId")
    private int TripPlannerConfirmedMasterId;

    @SerializedName("TripPlannerConfirmedOrderId")
    private int TripPlannerConfirmedOrderId;

    @SerializedName("TripPlannerConfirmedDetailId")
    private int TripPlannerConfirmedDetailId;

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("CurrentServingOrderId")
    private int CurrentServingOrderId;

    public PostUnloadingModel(int tripPlannerConfirmedMasterId, int tripPlannerConfirmedOrderId, int tripPlannerConfirmedDetailId, int orderId, double lat, double lng, int currentServingOrderId) {
        this.TripPlannerConfirmedMasterId = tripPlannerConfirmedMasterId;
        this.TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
        this.TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        this.OrderId = orderId;
        this.lat = lat;
        this.lng = lng;
        this.CurrentServingOrderId = currentServingOrderId;
    }


}
