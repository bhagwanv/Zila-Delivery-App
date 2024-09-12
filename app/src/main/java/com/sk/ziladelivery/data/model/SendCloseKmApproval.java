package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class SendCloseKmApproval {

    @SerializedName("zilaTripMasterId")
    private int tripPlannerConfirmMasterId;

    @SerializedName("closeKm")
    private int closeKm;

    @SerializedName("closeKmUrl")
    private String closeKmUrl;

    @SerializedName("PeopleID")
    private int PeopleID;

    public SendCloseKmApproval(int tripPlannerConfirmMasterId, int closeKm, String closeKmUrl, int peopleID) {
        this.tripPlannerConfirmMasterId = tripPlannerConfirmMasterId;
        this.closeKm = closeKm;
        this.closeKmUrl = closeKmUrl;
        PeopleID = peopleID;
    }
}
