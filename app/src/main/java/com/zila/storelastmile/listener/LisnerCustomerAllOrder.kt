package com.zila.storelastmile.listener
import com.zila.storelastmile.data.model.CustomerOrderInfo

interface LisnerCustomerAllOrder {
    fun onLisnerCustomerAllOrderClick(allTripModel: CustomerOrderInfo)
}