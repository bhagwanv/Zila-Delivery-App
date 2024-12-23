package com.zila.storelastmile.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class TripOrderList {

    @Expose
    @SerializedName("CustomerList")
    var CustomerList: ArrayList<CustomerListModel>? = null

    @SerializedName("IsFinalized")
    val IsFinalized = false

    @SerializedName("ZilaTripMasterId")
    val message: Int? = null
}

class CustomerListModel {


}