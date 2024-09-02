package com.sk.ziladelivery.listener

import com.sk.ziladelivery.data.model.CustomerInfo

interface LisnerAllOrder {
    fun onButtonClick(allTripModel: CustomerInfo)
}