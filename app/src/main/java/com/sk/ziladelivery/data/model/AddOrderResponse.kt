package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

data class AddOrderResponse(
    @SerializedName("Message") var message: String? = null,
    @SerializedName("Status") var status: Boolean? = null,
    @SerializedName("Data") var data: ArrayList<String> = arrayListOf()
)
