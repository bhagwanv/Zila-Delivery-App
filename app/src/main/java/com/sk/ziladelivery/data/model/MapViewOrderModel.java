package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapViewOrderModel implements Serializable {
    @SerializedName("lg")
    private double lg;
    @SerializedName("lat")
    private double lat;

    public MapViewOrderModel(double lg, double lat) {
        this.lg = lg;
        this.lat = lat;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
