package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class SelectAllResponceModel {

    @SerializedName("IsUnloaded")
    var isUnloaded = false

    @SerializedName("TripPlannerConfirmedDetailId")
    var TripPlannerConfirmedDetailId = 0

    @SerializedName("ItemMultiMRPId")
    var unloadItemList: ArrayList<Int>? = null

    constructor(
        isUnloaded: Boolean,
        TripPlannerConfirmedDetailId: Int,
        unloadItemList: ArrayList<Int>?
    ) {
        this.isUnloaded = isUnloaded
        this.TripPlannerConfirmedDetailId = TripPlannerConfirmedDetailId
        this.unloadItemList = unloadItemList
    }
}