package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class VoiceRecodingModel {
    @SerializedName("Id")
    private int Id;

    @SerializedName("TripId")
    private int TripId;

    @SerializedName("CustomerId")
    private int CustomerId;

    @SerializedName("RecordingUrl")
    private String RecordingUrl;

    @SerializedName("Comment")
    private String Comment;

    @SerializedName("IsActive")
    private boolean IsActive;

    @SerializedName("IsDeleted")
    private boolean IsDeleted;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTripId() {
        return TripId;
    }

    public void setTripId(int tripId) {
        TripId = tripId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getRecordingUrl() {
        return RecordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        RecordingUrl = recordingUrl;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }
}
