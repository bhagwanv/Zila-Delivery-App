package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class UnloadLocationModel {

    @SerializedName("CustomerId")
    int CustomerId;
    @SerializedName("ShopImageUrl")
    String ShopImageUrl;
    @SerializedName("latitude")
    double  latitude;
    @SerializedName("Longitude")
    double  Longitude;
    @SerializedName("UserId")
    int  UserId;

    public UnloadLocationModel(int customerId, String shopImageUrl, double latitude, double longitude, int userId) {
        CustomerId = customerId;
        ShopImageUrl = shopImageUrl;
        this.latitude = latitude;
        Longitude = longitude;
        UserId = userId;
    }
}
