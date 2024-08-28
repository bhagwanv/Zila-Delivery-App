package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QRCodeResquestModel {

    @SerializedName("orderIds")
    List<Integer> OrderID;

    @SerializedName("peopleId")
    int peopleId;

    @SerializedName("amount")
    Double amount;

    public QRCodeResquestModel(List<Integer> orderID, int peopleId, Double amount) {
        OrderID = orderID;
        this.peopleId = peopleId;
        this.amount = amount;
    }
}
