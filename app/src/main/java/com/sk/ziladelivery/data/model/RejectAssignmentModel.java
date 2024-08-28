package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class RejectAssignmentModel {


    @Expose
    @SerializedName("ReasonOfreject")
    private String ReasonOfreject;
    @Expose
    @SerializedName("userid")
    private int userid;
    @Expose
    @SerializedName("TravelDistance")
    private double TravelDistance;
    @Expose
    @SerializedName("IdealTime")
    private double IdealTime;
    @Expose
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @Expose
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @Expose
    @SerializedName("IsActive")
    private boolean IsActive;
    @Expose
    @SerializedName("Acceptance")
    private boolean Acceptance;
    @Expose
    @SerializedName("VehicleId")
    private int VehicleId;
    @Expose
    @SerializedName("PeopleID")
    private int PeopleID;
    @Expose
    @SerializedName("Cityid")
    private int Cityid;
    @Expose
    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    public String getReasonOfreject() {
        return ReasonOfreject;
    }

    public void setReasonOfreject(String ReasonOfreject) {
        this.ReasonOfreject = ReasonOfreject;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getTravelDistance() {
        return TravelDistance;
    }

    public void setTravelDistance(double TravelDistance) {
        this.TravelDistance = TravelDistance;
    }

    public double getIdealTime() {
        return IdealTime;
    }

    public void setIdealTime(double IdealTime) {
        this.IdealTime = IdealTime;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(boolean IsActive) {
        this.IsActive = IsActive;
    }

    public boolean getAcceptance() {
        return Acceptance;
    }

    public void setAcceptance(boolean Acceptance) {
        this.Acceptance = Acceptance;
    }

    public int getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(int VehicleId) {
        this.VehicleId = VehicleId;
    }

    public int getPeopleID() {
        return PeopleID;
    }

    public void setPeopleID(int PeopleID) {
        this.PeopleID = PeopleID;
    }

    public int getCityid() {
        return Cityid;
    }

    public void setCityid(int Cityid) {
        this.Cityid = Cityid;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }
}
