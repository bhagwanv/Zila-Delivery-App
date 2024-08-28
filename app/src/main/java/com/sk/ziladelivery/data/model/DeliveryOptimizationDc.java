package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryOptimizationDc {
   @SerializedName("TripPlannerConfirmedOrderId")
   int TripPlannerConfirmedOrderId;
   @SerializedName("OrderId")
   int OrderId;
   @SerializedName("lat")
   double lat;
   @SerializedName("lng")
   double lng;

   public DeliveryOptimizationDc(int tripPlannerConfirmedOrderId, int orderId, double lat, double lng) {
       this.TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
       this.OrderId = orderId;
       this.lat = lat;
       this.lng = lng;
   }
}
