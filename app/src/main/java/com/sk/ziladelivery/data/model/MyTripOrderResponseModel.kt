package com.sk.ziladelivery.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyTripOrderResponseModel {
    @Expose
    @SerializedName("ZorderList")
    var orderlist: ArrayList<OrderlistEntity>? = null

    @Expose
    @SerializedName("DeliveryIssuanceId")
    var deliveryissuanceid = 0

    @Expose
    @SerializedName("UnloadingTime")
    var unloadingtime = 0

    @Expose
    @SerializedName("OrderCompletionTime")
    var ordercompletiontime = 0

    @Expose
    @SerializedName("NoOfItems")
    var noofitems = 0

    @Expose
    @SerializedName("CustomerAddress")
    var customeraddress: String? = null

    @Expose
    @SerializedName("CustomerMobile")
    var customerMobile: String? = null

    @Expose
    @SerializedName("WarehouseAddress")
    var warehouseaddress: String? = null

    @Expose
    @SerializedName("TotalOrderAmount")
    var totalorderamount = 0

    @Expose
    @SerializedName("ShopName")
    var shopname: String? = null

    @Expose
    @SerializedName("CustomerId")
    var customerId = 0

    @Expose
    @SerializedName("Skcode")
    var skcode: String? = null

    @SerializedName("ZilaTripDetailId")
    var tripPlannerConfirmedDetailId = 0

    @Expose
    @SerializedName("RemaningTimeInMins")
    var remaningTimeInMins: Long = 0
    var isPlaying = false

    @Expose
    @SerializedName("Reason")
    var reason: String? = ""

    @Expose
    @SerializedName("IsOTPSent")
    var isOTPSent = false

    @Expose
    @SerializedName("OTPSentRemaningTimeInSec")
    var oTPSentRemaningTimeInSec = 0

    @Expose
    @SerializedName("WorkingStatus")
    var workingStatus = 0

    @Expose
    @SerializedName("IsReAttemptShow")
    var isReAttemptShow = false

    @Expose
    @SerializedName("IsReDispatchShow")
    var isReDispatchShow = false

    @Expose
    @SerializedName("RecordingUrl")
    var recordingUrl: String? = null

    @Expose
    @SerializedName("IsVisible")
    var isVisible = false

    @Expose
    @SerializedName("IsSkip")
    var isSkip = false

    @Expose
    @SerializedName("IsNotLastMileTrip")
    var isNotLastMileTrip = false

    @Expose
    @SerializedName("IsLocationEnabled")
    var isLocationEnabled = false

    @Expose
    @SerializedName("IsAnyTripRunning")
    var isAnyTripRunning = false

    @Expose
    @SerializedName("IsProcess")
    var isProcess = false

    @Expose
    @SerializedName("CustomerTripStatus")
    var customerTripStatus = 0

    @Expose
    @SerializedName("CRMTags")
    var cRMTags: String? = null

    @Expose
    @SerializedName("TripTypeEnum")
    var tripTypeEnum = 0
    var isAllDelivered = false

    @Expose
    @SerializedName("IsReturnOrder")
    var isReturnOrder = false

    @Expose
    @SerializedName("IsGeneralOrder")
    var isGeneralOrder = false
    inner class OrderlistEntity {
        @Expose
        @SerializedName("DeliveryIssuanceId")
        var deliveryissuanceid = 0

        @Expose
        @SerializedName("TimeInMins")
        var timeinmins = 0

        @Expose
        @SerializedName("TotalTimeInMins")
        var totaltimeinmins = 0

        @Expose
        @SerializedName("Amount")
        var amount = 0

        @Expose
        @SerializedName("Status")
        var status: String? = ""

        @Expose
        @SerializedName("OrderId")
        var orderid = 0

        @SerializedName("NoOfItems")
        var noofitems = 0

        @Expose
        @SerializedName("ReDispatchCount")
        var reDispatchCount = 0

        @Expose
        @SerializedName("ReAttemptCount")
        var reAttemptCount = 0

        @Expose
        @SerializedName("OrderType")
        var orderType: String? = ""
    }
}