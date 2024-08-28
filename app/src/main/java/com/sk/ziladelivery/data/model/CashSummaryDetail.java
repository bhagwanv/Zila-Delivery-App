package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class CashSummaryDetail {

    @SerializedName("Status")
    private String Status;

    @SerializedName("DeclineNote")
    private String DeclineNote;

    @SerializedName("CreatedBy")
    private int CreatedBy;

    @SerializedName("TotalDeliveryissueAmt")
    private double TotalDeliveryissueAmt;

    @SerializedName("TotalCollectionAmt")
    private double TotalCollectionAmt;

    @SerializedName("TotalCheckAmt")
    private double TotalCheckAmt;

    @SerializedName("TotalOnlineAmt")
    private double TotalOnlineAmt;

    @SerializedName("TotalCashAmt")
    private double TotalCashAmt;

    @SerializedName("Deliveryissueid")
    private int Deliveryissueid;

    @SerializedName("DBoyPeopleId")
    private int DBoyPeopleId;

    @SerializedName("Warehouseid")
    private int Warehouseid;

    @SerializedName("CurrencyCollectionId")
    private int CurrencyCollectionId;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public double getTotalDeliveryissueAmt() {
        return TotalDeliveryissueAmt;
    }

    public void setTotalDeliveryissueAmt(double TotalDeliveryissueAmt) {
        this.TotalDeliveryissueAmt = TotalDeliveryissueAmt;
    }

    public double getTotalCollectionAmt() {
        return TotalCollectionAmt;
    }

    public void setTotalCollectionAmt(double TotalCollectionAmt) {
        this.TotalCollectionAmt = TotalCollectionAmt;
    }

    public double getTotalCheckAmt() {
        return TotalCheckAmt;
    }

    public void setTotalCheckAmt(double TotalCheckAmt) {
        this.TotalCheckAmt = TotalCheckAmt;
    }

    public double getTotalOnlineAmt() {
        return TotalOnlineAmt;
    }

    public void setTotalOnlineAmt(double TotalOnlineAmt) {
        this.TotalOnlineAmt = TotalOnlineAmt;
    }

    public double getTotalCashAmt() {
        return TotalCashAmt;
    }

    public void setTotalCashAmt(double TotalCashAmt) {
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

    public int getCurrencyCollectionId() {
        return CurrencyCollectionId;
    }

    public void setCurrencyCollectionId(int CurrencyCollectionId) {
        this.CurrencyCollectionId = CurrencyCollectionId;
    }
    public String getDeclineNote() {
        return DeclineNote;
    }

    public void setDeclineNote(String declineNote) {
        DeclineNote = declineNote;
    }
}
