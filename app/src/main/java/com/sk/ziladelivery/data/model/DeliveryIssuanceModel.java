package com.sk.ziladelivery.data.model;

public class DeliveryIssuanceModel {

    private String DeliveryIssuanceId;

    public DeliveryIssuanceModel(String deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public String getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(String deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }
}
