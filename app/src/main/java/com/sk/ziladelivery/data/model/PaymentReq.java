package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class PaymentReq {

    @SerializedName("EncString")
    private String EncString;

    public PaymentReq(String encString) {
        EncString = encString;
    }
}