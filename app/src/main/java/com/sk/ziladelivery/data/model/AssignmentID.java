package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AssignmentID implements Serializable {

    @Expose
    @SerializedName("DeliveryIssuanceDetailDcs")
    private ArrayList<DeliveryIssuanceDetail> DeliveryIssuanceDetailDcs;
    @Expose
    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    public ArrayList<DeliveryIssuanceDetail> getDeliveryIssuanceDetailDcs() {
        return DeliveryIssuanceDetailDcs;
    }

    public void setDeliveryIssuanceDetailDcs(ArrayList<DeliveryIssuanceDetail> DeliveryIssuanceDetailDcs) {
        this.DeliveryIssuanceDetailDcs = DeliveryIssuanceDetailDcs;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }


}
