package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

class UnloadItemListModel {
    @SerializedName("TripPlannerItemCheckListId")
    var tripPlannerItemCheckListId = 0

    @SerializedName("OrderId")
    var orderId = 0

    @SerializedName("ItemMultiMRPId")
    var itemMultiMRPId = 0

    @SerializedName("Itemname")
    var itemname: String? = null

    @SerializedName("Qty")
    var qty = 0

    @SerializedName("IsUnloaded")
    var isUnloaded = false

    @SerializedName("IsDone")
    var isDone = false

    @SerializedName("TripPlannerConfirmedDetailId")
    var tripPlannerConfirmedDetailId = 0

    var ischeck = false
}