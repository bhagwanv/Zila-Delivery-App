package com.zila.storelastmile.listener

import com.zila.storelastmile.data.model.CustomerInfo

interface LisnerAllOrder {
    fun onButtonClick(allTripModel: CustomerInfo)
}