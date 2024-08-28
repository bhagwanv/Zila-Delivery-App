package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem

data class ReturnOrderCreditNoteRequestModel(val OrderId : Int,
                                             val OrderDetailId : Int,
                                             val qty : Int,
                                             val unitprice : Double,
                                             val PickItemImage  : String)
