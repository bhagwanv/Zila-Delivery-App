package com.sk.ziladelivery.data.model

data class GenerateDeliveryQRCodeReqModel(
    val OrderId: Int,
    val amount: Double,
    val peopleId : Int,

)