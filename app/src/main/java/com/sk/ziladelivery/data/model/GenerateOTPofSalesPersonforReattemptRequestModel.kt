package com.sk.ziladelivery.data.model

data class GenerateOTPofSalesPersonforReattemptRequestModel(
    var OrderIds: List<Int>,
    var Status: String,
    var lat: Double,
    var lg: Double,
    var Reason: String? = null,
    var VideoUrl: String? = null
)
