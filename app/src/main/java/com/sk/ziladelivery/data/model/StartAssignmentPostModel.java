package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class StartAssignmentPostModel
{
    @SerializedName("ZilaTripMasterId")
    public long ZilaTripMasterId;

    @SerializedName("CurrentLat")
    public double CurrentLat;

    @SerializedName("CurrentLng")
    public double CurrentLng;

    @SerializedName("ClosingKm")
    public int ClosingKm;

    @SerializedName("ClosingKMUrl")
    public String ClosingKMUrl;

    @SerializedName("IsClosingKmManualReading")
    public boolean IsClosingKmManualReading;

    @SerializedName("StartKmUrl")
    public String StartKmUrl;

    @SerializedName("StartKm")
    public int StartKm;

    @SerializedName("PeopleID")
    public int PeopleID;



    public StartAssignmentPostModel(long zilaTripMasterId, double currentLat, double currentLng,boolean IsClosingKmManualReading,int peopleID) {
       this. ZilaTripMasterId = zilaTripMasterId;
        this. CurrentLat = currentLat;
        this. CurrentLng = currentLng;
        this. ClosingKm = ClosingKm;
        this. ClosingKMUrl = ClosingKMUrl;
        this. IsClosingKmManualReading = IsClosingKmManualReading;
        this.PeopleID=peopleID;
    }

    public StartAssignmentPostModel(int zilaTripMasterId, Double currentLat, Double currentLng) {
        this. ZilaTripMasterId = zilaTripMasterId;
        this. CurrentLat = currentLat;
        this. CurrentLng = currentLng;

    }
    public StartAssignmentPostModel(int zilaTripMasterId, Double currentLat, Double currentLng,String startImageURl,int startKm,int peopleID) {
        this. ZilaTripMasterId = zilaTripMasterId;
        this. CurrentLat = currentLat;
        this. CurrentLng = currentLng;
        this.StartKmUrl= startImageURl;
        this.StartKm=startKm;
        this.PeopleID=peopleID;
    }
}
