package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChnagePasswordResponseModel : Serializable {

    @SerializedName("Status")
    var status: Boolean? = false

    @SerializedName("ErrorMessage")
    var errorMessage: String? = ""

    @SerializedName("Message")
    var Message: String? = ""
}