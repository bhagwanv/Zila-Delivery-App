package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem

data class PickedReturnOrderByDBoyRequestModel(var OrderDetaileList :List<OrderDetaileList>,
                                               val OTP   : String, val TripPlannerConfirmedMasterId :Int,val DeliveryLat  :Double,val DeliveryLng  :Double,val PeopleId :Int)

data class OrderDetaileList(val NewOrderId : Int, val NewOrderDetailId :Int,val Qty  :Int,val unitprice  :Double,val PickItemImage  :String="",val BatchCode:String,val BatchId:Int,val KKRRRequestId:Int)