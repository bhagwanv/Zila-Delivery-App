package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class CurrencyCollectionDc {

    @SerializedName("CreatedBy")
    private int CreatedBy;

    @SerializedName("TotalDeliveryissueAmt")
    private int TotalDeliveryissueAmt;

    @SerializedName("TotalCheckAmt")
    private int TotalCheckAmt;

    @SerializedName("TotalOnlineAmt")
    private int TotalOnlineAmt;

    @SerializedName("TotalCashAmt")
    private int TotalCashAmt;


    @SerializedName("TotalCollectionCashAmt")
    private int TotalCollectionCashAmt;

    @SerializedName("TotalCollectionOnlineAmt")
    private int TotalCollectionOnlineAmt;

    @SerializedName("TotalCollectionCheckAmt")
    private int TotalCollectionCheckAmt;

    @SerializedName("Deliveryissueid")
    private int Deliveryissueid;

    @SerializedName("DBoyPeopleId")
    private int DBoyPeopleId;

    @SerializedName("Warehouseid")
    private int Warehouseid;

    @SerializedName("Id")
    private int Id;

    @SerializedName("Status")
    private String Status;

    @SerializedName("OnlineCollections")
    private ArrayList<PostOnlineCollectionModel> onlineCollections;

    @SerializedName("ChequeCollections")
    private ArrayList<PostChequeCollectionModel> chequeCollections;

    @SerializedName("CashCollections")
    private ArrayList<PostCashCollection> cashCollections;

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public int getTotalDeliveryissueAmt() {
        return TotalDeliveryissueAmt;
    }

    public void setTotalDeliveryissueAmt(int TotalDeliveryissueAmt) {
        this.TotalDeliveryissueAmt = TotalDeliveryissueAmt;
    }

    public int getTotalCheckAmt() {
        return TotalCheckAmt;
    }

    public void setTotalCheckAmt(int TotalCheckAmt) {
        this.TotalCheckAmt = TotalCheckAmt;
    }

    public int getTotalOnlineAmt() {
        return TotalOnlineAmt;
    }

    public void setTotalOnlineAmt(int TotalOnlineAmt) {
        this.TotalOnlineAmt = TotalOnlineAmt;
    }

    public int getTotalCashAmt() {
        return TotalCashAmt;
    }

    public void setTotalCashAmt(int TotalCashAmt) {
        this.TotalCashAmt = TotalCashAmt;
    }

    public int getDeliveryissueid() {
        return Deliveryissueid;
    }

    public void setDeliveryissueid(int Deliveryissueid) {
        this.Deliveryissueid = Deliveryissueid;
    }

    public int getDBoyPeopleId() {
        return DBoyPeopleId;
    }

    public void setDBoyPeopleId(int DBoyPeopleId) {
        this.DBoyPeopleId = DBoyPeopleId;
    }

    public int getWarehouseid() {
        return Warehouseid;
    }

    public void setWarehouseid(int Warehouseid) {
        this.Warehouseid = Warehouseid;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getTotalCollectionCashAmt() {
        return TotalCollectionCashAmt;
    }

    public void setTotalCollectionCashAmt(int totalCollectionCashAmt) {
        TotalCollectionCashAmt = totalCollectionCashAmt;
    }

    public int getTotalCollectionOnlineAmt() {
        return TotalCollectionOnlineAmt;
    }

    public void setTotalCollectionOnlineAmt(int totalCollectionOnlineAmt) {
        TotalCollectionOnlineAmt = totalCollectionOnlineAmt;
    }

    public int getTotalCollectionCheckAmt() {
        return TotalCollectionCheckAmt;
    }

    public void setTotalCollectionCheckAmt(int totalCollectionCheckAmt) {
        TotalCollectionCheckAmt = totalCollectionCheckAmt;
    }
}