package com.sk.ziladelivery.data.model;

public class LatLongModel {
    public double lat;
    public double Long;
    public int SalesPersonId;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public int getSalesPersonId() {
        return SalesPersonId;
    }

    public void setSalesPersonId(int salesPersonId) {
        SalesPersonId = salesPersonId;
    }

    public LatLongModel(double lat, double aLong, int salesPersonId) {
        this.lat = lat;
        Long = aLong;
        SalesPersonId = salesPersonId;
    }
}
