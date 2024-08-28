package com.sk.ziladelivery.listener

import com.sk.ziladelivery.data.model.AllTripModel

interface LisnerAllTrip {
    fun onButtonClick(allTripModel: AllTripModel)
}