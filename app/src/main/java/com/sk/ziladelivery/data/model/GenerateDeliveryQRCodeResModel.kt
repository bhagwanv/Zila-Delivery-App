package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

data class GenerateDeliveryQRCodeResModel(
    @SerializedName("Status") var Status: Boolean? = null,
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("QRCodeurl") var QRCodeurl: String? = null,
    @SerializedName("Amount") var Amount: Int? = null,
    @SerializedName("OrderId") var OrderId: Int? = null,
    @SerializedName("UPITxnID") var UPITxnID: String? = null
)