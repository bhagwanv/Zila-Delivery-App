package com.sk.ziladelivery.data.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class UnloadModel {
    @SerializedName("UnloadItemListPage")
    var unloadItemList: ArrayList<UnloadItemListModel>? = null

    @SerializedName("unloadItemTotal")
    var unloadItemTotalModel: UnloadItemCountModel? = null
}