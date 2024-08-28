package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem

data class ReturnItemListResponseModel(val ItemMultiMRPId: Int,
                                       val Itemname: String,
                                       val NewOrderDetailId: Int,
                                       val NewOrderId: Int,
                                       var Qty: Int,
                                       val TripPlannerConfirmedDetailId: Int,
                                       val UnitPrice: Double,
                                       val Status: String,
                                       val BatchId: Int,
                                       val BatchCode: String,
                                       var returnQty :Int= 0,
                                       var isChecked :Boolean = false,
                                       var ImageURl :String = "",
                                       var ImageURlmessage:String = "",
                                       val KKRRRequestId: Int
                                       )
