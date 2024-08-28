package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

class AllItemSelectModel {
    @SerializedName("TripPlannerConfirmedDetailId")
    var tripPlannerConfirmedDetailId = 0


    @SerializedName("ItemMultiMRPId")
    var itemMultiMRPId = 0

    constructor(tripPlannerConfirmedDetailId: Int, itemMultiMRPId: Int) {
        this.tripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId
        this.itemMultiMRPId = itemMultiMRPId
    }
}