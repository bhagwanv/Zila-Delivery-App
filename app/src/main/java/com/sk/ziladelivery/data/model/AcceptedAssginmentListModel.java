package com.sk.ziladelivery.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "AcceptedAssginmentListModel")
public  class AcceptedAssginmentListModel {
    @Expose
    @SerializedName("TotalUnloadingDuration")
    private int TotalUnloadingDuration;
    @Expose
    @SerializedName("ReturnDuration")
    private int ReturnDuration;
    @Expose
    @SerializedName("AssignmentDuration")
    private int AssignmentDuration;
    @Expose
    @SerializedName("ReturnDistance")
    private double ReturnDistance;
    @Expose
    @SerializedName("AssignmentDistance")
    private double AssignmentDistance;
    @Expose
    @SerializedName("IsDirectionExist")
    private boolean IsDirectionExist;
    @Expose
    @SerializedName("AssignmentDate")
    private String AssignmentDate;
    @Expose
    @SerializedName("End")
    private boolean End;
    @Expose
    @SerializedName("Start")
    private boolean Start;
    @Expose
    @SerializedName("StartDateTime")
    private String StartDateTime;

    @PrimaryKey(autoGenerate = true)
    @Expose
    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    public int getTotalUnloadingDuration() {
        return TotalUnloadingDuration;
    }

    public void setTotalUnloadingDuration(int TotalUnloadingDuration) {
        this.TotalUnloadingDuration = TotalUnloadingDuration;
    }

    public int getReturnDuration() {
        return ReturnDuration;
    }

    public void setReturnDuration(int ReturnDuration) {
        this.ReturnDuration = ReturnDuration;
    }

    public int getAssignmentDuration() {
        return AssignmentDuration;
    }

    public void setAssignmentDuration(int AssignmentDuration) {
        this.AssignmentDuration = AssignmentDuration;
    }

    public double getReturnDistance() {
        return ReturnDistance;
    }

    public void setReturnDistance(double ReturnDistance) {
        this.ReturnDistance = ReturnDistance;
    }

    public double getAssignmentDistance() {
        return AssignmentDistance;
    }

    public void setAssignmentDistance(double AssignmentDistance) {
        this.AssignmentDistance = AssignmentDistance;
    }

    public boolean getIsDirectionExist() {
        return IsDirectionExist;
    }

    public void setIsDirectionExist(boolean IsDirectionExist) {
        this.IsDirectionExist = IsDirectionExist;
    }

    public String getAssignmentDate() {
        return AssignmentDate;
    }

    public void setAssignmentDate(String AssignmentDate) {
        this.AssignmentDate = AssignmentDate;
    }

    public boolean getEnd() {
        return End;
    }

    public void setEnd(boolean End) {
        this.End = End;
    }

    public boolean getStart() {
        return Start;
    }

    public void setStart(boolean Start) {
        this.Start = Start;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String StartDateTime) {
        this.StartDateTime = StartDateTime;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }
}
