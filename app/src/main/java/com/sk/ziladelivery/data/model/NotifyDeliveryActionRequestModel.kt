package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

data class NotifyDeliveryActionRequestModel(
    @SerializedName("TripPlannerConfirmedDetailId")
    var TripPlannerConfirmedDetailId: Long? = null,
    @SerializedName("Reason")
    var Reason: String? = null,
    @SerializedName("OrderId")
    var OrderId: Int? = null,
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lg")
    var lg: Double? = null,
    @SerializedName("VideoUrl")
    var VideoUrl: String? = null,
    @SerializedName("Action")
    var Action: String? = null,
    @SerializedName("NextRedispatchedDate")
    var NextRedispatchedDate: String? = null,
    )
