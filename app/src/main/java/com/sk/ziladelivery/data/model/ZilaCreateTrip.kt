package com.sk.ziladelivery.data.model

data class ZilaCreateTrip(var ZilaTripMasterId:Int,var startingKm:Int,var reportingTime:String,var DriverId:Int,var VehicleId:Int,var AgentId:Int,var DboyId:Int,var TripDate:String)
class  FinalizedTripResponceModel{
    var Status:Boolean?=null
    var Message:String?=null
    var blockedOrderList: ArrayList<BlockedOrderListModel> = arrayListOf()
}
class BlockedOrderListModel{

}