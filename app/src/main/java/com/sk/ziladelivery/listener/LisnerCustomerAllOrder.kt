package com.sk.ziladelivery.listener
import com.sk.ziladelivery.data.model.CustomerOrderInfo

interface LisnerCustomerAllOrder {
    fun onLisnerCustomerAllOrderClick(allTripModel: CustomerOrderInfo)
}