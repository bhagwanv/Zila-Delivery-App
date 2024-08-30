package com.sk.ziladelivery.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sk.ziladelivery.data.model.CollectionPaymentModel.PaymentGroupwisesListModel
import com.sk.ziladelivery.data.model.CompanyInfoResponse.CompanyDetails
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