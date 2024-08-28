package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BackgroundServiceModel {
    public int getTripPlannerVehicleId() {
        return TripPlannerVehicleId;
    }

    public void setTripPlannerVehicleId(int tripPlannerVehicleId) {
        TripPlannerVehicleId = tripPlannerVehicleId;
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

    public int getRecordStatus() {
        return RecordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        RecordStatus = recordStatus;
    }

    public String getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(String recordTime) {
        RecordTime = recordTime;
    }

    public int getTripPlannerConfirmedDetailId() {
        return TripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }

    @Expose
    @SerializedName("TripPlannerVehicleId")
    private int TripPlannerVehicleId;
    @Expose
    @SerializedName("Lat")
    private double Lat;
    @Expose
    @SerializedName("Lng")
    private double Lng;
    @Expose
    @SerializedName("RecordStatus")
    private int RecordStatus;
    @Expose
    @SerializedName("RecordTime")
    private String RecordTime;

    @Expose
    @SerializedName("TripPlannerConfirmedDetailId")
    private int TripPlannerConfirmedDetailId;

    @Expose
    @SerializedName("DistanceInMeter")
    private long DistanceInMeter ;

    @Expose
    @SerializedName("IsCalculationRecord")
    private boolean IsCalculationRecord;






    public BackgroundServiceModel(int tripPlannerVehicleId, double lat, double lng, int recordStatus, String recordTime, int tripPlannerConfirmedDetailId,long DistanceInMeter/*,boolean IsCalculationRecord*/) {
        this.TripPlannerVehicleId = tripPlannerVehicleId;
        this.Lat = lat;
        this.Lng = lng;
        this.RecordStatus = recordStatus;
        this.RecordTime = recordTime;
        this.TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        this.DistanceInMeter = DistanceInMeter;
        //this.IsCalculationRecord = IsCalculationRecord;
    }
}
