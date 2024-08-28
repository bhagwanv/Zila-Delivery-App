package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

class QRCodeModel {


    @SerializedName("Status")
    var status: Boolean? = null

    @SerializedName("msg")
    var msg: String? = null

    @SerializedName("QRCodeurl")
    var qRCodeurl: String? = null

    @SerializedName("Amount")
    var Amount: Double? = null

    @SerializedName("OrderIds")
    var OrderIds: List<Int>? = null
}