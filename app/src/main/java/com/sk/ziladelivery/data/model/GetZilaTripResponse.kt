package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName

data class GetZilaTripResponse(
    @SerializedName("ZilaTripMasterId") var ZilaTripMasterId: Int? = null,
    @SerializedName("IsFinalized") var IsFinalized: Boolean? = null,
    @SerializedName("CustomerList") var CustomerList: ArrayList<CustomerList> = arrayListOf()
)

data class CustomerList(
    @SerializedName("customerInfo") var customerInfo: CustomerInfo? = CustomerInfo(),
    @SerializedName("customerOrderInfo") var customerOrderInfo: ArrayList<CustomerOrderInfo> = arrayListOf()

)

data class CustomerInfo(
    @SerializedName("Skcode") var Skcode: String? = null,
    @SerializedName("ShopName") var ShopName: String? = null,
    @SerializedName("BillingAddress") var BillingAddress: String? = null,
    @SerializedName("GrossAmount") var GrossAmount: Int? = null,
    @SerializedName("CustomerId") var CustomerId: Int? = null
)


data class CustomerOrderInfo(
    @SerializedName("OrderId") var OrderId: Int? = null,
    @SerializedName("GrossAmount") var GrossAmount: Int? = null,
    @SerializedName("Status") var Status: String? = null,
    @SerializedName("DeliveryIssuanceId") var DeliveryIssuanceId: Int? = null,
    @SerializedName("ReDispatchCount") var ReDispatchCount: Int? = null,
    @SerializedName("WorkingStatus") var WorkingStatus: Int? = null,
    @SerializedName("WarehouseId") var WarehouseId: Int? = null

)
