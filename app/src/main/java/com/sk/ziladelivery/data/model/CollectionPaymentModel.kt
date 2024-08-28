package com.sk.ziladelivery.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class CollectionPaymentModel {
    @Expose
    @SerializedName("PaymentGroupwisesList")
    var groupwisesListModels: ArrayList<PaymentGroupwisesListModel>? = null

    @Expose
    @SerializedName("customerInfo")
    var customerinfo: CustomerinfoEntity? = null

    inner class CustomerorderinfoEntity {
        @Expose
        @SerializedName("Status")
        var status: String? = null

        @Expose
        @SerializedName("NoOfItems")
        var noofitems = 0

        @Expose
        @SerializedName("GrossAmount")
        var grossamount = 0.0

        @Expose
        @SerializedName("OrderId")
        var orderid = 0

        @Expose
        @SerializedName("ReDispatchCount")
        var reDispatchCount = 0

        @Expose
        @SerializedName("DeliveryIssuanceId")
        var deliveryIssuanceId = 0

        @Expose
        @SerializedName("OnilnePayment")
        var isOnilnePayment = false

        @Expose
        @SerializedName("PaymentFrom")
        var paymentFrom: String? = null

        @Expose
        @SerializedName("OnlineAmount")
        var onlineAmount = 0.0

        @Expose
        @SerializedName("RemaningOrderAmount")
        var remaningOrderAmount = 0.0

        @Expose
        @SerializedName("TripPlannerConfirmedOrderId")
        var tripPlannerConfirmedOrderId = 0

        @Expose
        @SerializedName("boolWorkingStatus")
        var isBoolWorkingStatus = false

        @Expose
        @SerializedName("IsPaymantDone")
        var isPaymantDone = false

        @Expose
        @SerializedName("OrderDetails")
        var orderDetailsModel: ArrayList<ItemOrderDetailModel>? = null
    }

    class CustomerinfoEntity {
        @Expose
        @SerializedName("ShopName")
        var shopname: String? = null

        @Expose
        @SerializedName("Skcode")
        var skcode: String? = null

        @Expose
        @SerializedName("BillingAddress")
        var billingAddress: String? = null

        @Expose
        @SerializedName("GrossAmount")
        var grossAmount = 0.0

        @Expose
        @SerializedName("CustomerId")
        var customerId = 0

        @Expose
        @SerializedName("DeliveryIssuanceId")
        var deliveryIssuanceId = 0

        @Expose
        @SerializedName("OTPSentRemaningTimeInSec")
        var oTPSentRemaningTimeInSec = 0

        @Expose
        @SerializedName("IsOTPSent")
        var isOTPSent = false

        @Expose
        @SerializedName("isAllPaymentDone")
        var isAllPaymentDone = false

        @Expose
        @SerializedName("IsQREnabled")
        var IsQREnabled = false
        @Expose
        @SerializedName("CRMTags")
        var CRMTags: String? = null


    }

    inner class ItemOrderDetailModel {
        @Expose
        @SerializedName("OrderId")
        var orderId = 0

        @Expose
        @SerializedName("itemname")
        var itemname: String? = null

        @Expose
        @SerializedName("TotalAmt")
        var totalAmt = 0.0

        @Expose
        @SerializedName("qty")
        var qty = 0

        @Expose
        @SerializedName("Status")
        var status: String? = null
    }

    inner class PaymentGroupwisesListModel {
        @Expose
        @SerializedName("PaymentMode")
        var paymentMode: String? = null

        @Expose
        @SerializedName("customerOrderInfo")
        var customerorderinfo: ArrayList<CustomerorderinfoEntity>? = null
    }
}