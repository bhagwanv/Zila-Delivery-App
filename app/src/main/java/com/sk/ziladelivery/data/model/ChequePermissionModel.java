package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class ChequePermissionModel {

    @SerializedName("IsChequeAccepted")
    private boolean IsChequeAccepted ;

    @SerializedName("msg")
    private String msg ;


    @SerializedName("ChequeLimit")
    private double ChequeLimit  ;

    public boolean isChequeAccepted() {
        return IsChequeAccepted;
    }

    public String getMsg() {
        return msg;
    }

    public double getChequeLimit() {
        return ChequeLimit;
    }
}