package com.sk.ziladelivery.data.model

data class OrderDetailsItem(val status: String = "",
                            val itemname: String = "",
                            val totalAmt: Double = 0.0,
                            val qty: Int = 0,
                            val orderId: Int = 0)