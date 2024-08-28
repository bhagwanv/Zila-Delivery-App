package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

class UnloadItemCountModel {
    @SerializedName("TotalQty")
    var totalQty = 0

    @SerializedName("TotalItem")
    var totalItem = 0

    @SerializedName("TotalAmount")
    var totalAmount = 0
}