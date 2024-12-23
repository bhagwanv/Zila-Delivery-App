package com.zila.storelastmile.listener

import com.zila.storelastmile.data.model.AllTripModel

interface LisnerAllTrip {
    fun onButtonClick(allTripModel: AllTripModel)
}