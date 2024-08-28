package com.sk.ziladelivery.data.model;

public class SortOrderModel  {



    private int DeliveryIssuanceId;
    private double Lat;
    private double Lng;

    public SortOrderModel(int deliveryIssuanceId, double lat, double lng) {
        DeliveryIssuanceId = deliveryIssuanceId;
        Lat = lat;
        Lng = lng;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }


}
