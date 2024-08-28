package com.sk.ziladelivery.data.localdatabase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userLatLngTable")
public class UserLatLngModel {

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private  int TripPlannerVehicleId;
    private double Lat;
    private double Lng;
    private  long CurrentServingOrderId;
    private  int RecordStatus;
    private  String RecordTime;
    private  long TripPlannerConfirmedDetailId;
    private  long DistanceInMeter;
    private  boolean IsCalculationRecord;


    public UserLatLngModel() {
    }

    public UserLatLngModel(int tripPlannerVehicleId, double latitude, double longitude, long currentServingOrderId, int recordStatus, String recordTime, long tripPlannerConfirmedDetailId, long distanceInMeter, boolean isCalculationRecord) {
        TripPlannerVehicleId = tripPlannerVehicleId;
        this.Lat = latitude;
        this.Lng = longitude;
        CurrentServingOrderId = currentServingOrderId;
        RecordStatus = recordStatus;
        RecordTime = recordTime;
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        DistanceInMeter = distanceInMeter;
        IsCalculationRecord = isCalculationRecord;
    }



    public boolean getIsCalculationRecord() {
        return IsCalculationRecord;
    }

    public void setIsCalculationRecord(boolean calculationRecord) {
        IsCalculationRecord = calculationRecord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTripPlannerVehicleId() {
        return TripPlannerVehicleId;
    }

    public void setTripPlannerVehicleId(int tripPlannerVehicleId) {
        TripPlannerVehicleId = tripPlannerVehicleId;
    }


    public long getCurrentServingOrderId() {
        return CurrentServingOrderId;
    }

    public void setCurrentServingOrderId(long currentServingOrderId) {
        CurrentServingOrderId = currentServingOrderId;
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

    public long getTripPlannerConfirmedDetailId() {
        return TripPlannerConfirmedDetailId;
    }

    public void setTripPlannerConfirmedDetailId(long tripPlannerConfirmedDetailId) {
        TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
    }

    public long getDistanceInMeter() {
        return DistanceInMeter;
    }

    public void setDistanceInMeter(long distanceInMeter) {
        DistanceInMeter = distanceInMeter;
    }

}
