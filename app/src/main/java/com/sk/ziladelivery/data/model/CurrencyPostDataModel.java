package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public  class CurrencyPostDataModel {


    @SerializedName("onlineCollections")
    private ArrayList<PostOnlineCollectionModel> onlineCollections;

    @SerializedName("chequeCollections")
    private ArrayList<PostChequeCollectionModel> chequeCollections;

    @SerializedName("cashCollections")
    private ArrayList<PostCashCollection> cashCollections;

    @SerializedName("createdBy")
    private int createdBy;

    @SerializedName("totalDeliveryissueAmt")
    private double totalDeliveryissueAmt;

    @SerializedName("totalCheckAmt")
    private double totalCheckAmt;

    @SerializedName("totalOnlineAmt")
    private double totalOnlineAmt;

    @SerializedName("totalCashAmt")
    private double totalCashAmt;

    @SerializedName("deliveryissueid")
    private String deliveryissueid;

    @SerializedName("dBoyPeopleId")
    private int dBoyPeopleId;

    @SerializedName("warehouseid")
    private int warehouseid;

    @SerializedName("id")
    private int id;
    public CurrencyPostDataModel(ArrayList<PostOnlineCollectionModel> onlineCollections, ArrayList<PostChequeCollectionModel> chequeCollections, ArrayList<PostCashCollection> cashCollections, int createdBy, double totalDeliveryissueAmt, double totalCheckAmt, double totalOnlineAmt, double totalCashAmt, String deliveryissueid, int dBoyPeopleId, int warehouseid, int id) {
        this.onlineCollections = onlineCollections;
        this.chequeCollections = chequeCollections;
        this.cashCollections = cashCollections;
        this.createdBy = createdBy;
        this.totalDeliveryissueAmt = totalDeliveryissueAmt;
        this.totalCheckAmt = totalCheckAmt;
        this.totalOnlineAmt = totalOnlineAmt;
        this.totalCashAmt = totalCashAmt;
        this.deliveryissueid = deliveryissueid;
        this.dBoyPeopleId = dBoyPeopleId;
        this.warehouseid = warehouseid;
        this.id = id;
    }
    public ArrayList<PostOnlineCollectionModel> getOnlineCollections() {
        return onlineCollections;
    }

    public void setOnlineCollections(ArrayList<PostOnlineCollectionModel> onlineCollections) {
        this.onlineCollections = onlineCollections;
    }

    public ArrayList<PostChequeCollectionModel> getChequeCollections() {
        return chequeCollections;
    }

    public void setChequeCollections(ArrayList<PostChequeCollectionModel> chequeCollections) {
        this.chequeCollections = chequeCollections;
    }

    public ArrayList<PostCashCollection> getCashCollections() {
        return cashCollections;
    }

    public void setCashCollections(ArrayList<PostCashCollection> cashCollections) {
        this.cashCollections = cashCollections;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public double getTotalDeliveryissueAmt() {
        return totalDeliveryissueAmt;
    }

    public void setTotalDeliveryissueAmt(double totalDeliveryissueAmt) {
        this.totalDeliveryissueAmt = totalDeliveryissueAmt;
    }

    public double getTotalCheckAmt() {
        return totalCheckAmt;
    }

    public void setTotalCheckAmt(double totalCheckAmt) {
        this.totalCheckAmt = totalCheckAmt;
    }

    public double getTotalOnlineAmt() {
        return totalOnlineAmt;
    }

    public void setTotalOnlineAmt(double totalOnlineAmt) {
        this.totalOnlineAmt = totalOnlineAmt;
    }

    public double getTotalCashAmt() {
        return totalCashAmt;
    }

    public void setTotalCashAmt(double totalCashAmt) {
        this.totalCashAmt = totalCashAmt;
    }

    public String getDeliveryissueid() {
        return deliveryissueid;
    }

    public void setDeliveryissueid(String deliveryissueid) {
        this.deliveryissueid = deliveryissueid;
    }

    public int getDBoyPeopleId() {
        return dBoyPeopleId;
    }

    public void setDBoyPeopleId(int dBoyPeopleId) {
        this.dBoyPeopleId = dBoyPeopleId;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
