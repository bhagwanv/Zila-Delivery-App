package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class PostChequeCollectionModel {

    @SerializedName("UsedChequeAmt")
    private double usedChequeAmt;

    @SerializedName("ChequeDate")
    private String chequeDate;

    @SerializedName("Orderid")
    private String orderid;

    @SerializedName("ChequeAmt")
    private double chequeAmt;

    @SerializedName("Id")
    private int id;

    @SerializedName("ChequeNumber")
    private String chequeNumber;

    @SerializedName("ChequeimagePath")
    private String ChequeimagePath;

    @SerializedName("ChequeNote")
    private String ChequeNote;


    @SerializedName("ChequeBankName")
    private String ChequeBankName;


    public PostChequeCollectionModel() {
    }

    public PostChequeCollectionModel(String orderid) {
        this.orderid = orderid;
    }

    public PostChequeCollectionModel(double usedChequeAmt, String chequeDate, String orderid, double chequeAmt, int id, String chequeNumber,String ChequeimagePath,String ChequeNote,String ChequeBankName) {
        this.usedChequeAmt = usedChequeAmt;
        this.chequeDate = chequeDate;
        this.orderid = orderid;
        this.chequeAmt = chequeAmt;
        this.id = id;
        this.chequeNumber = chequeNumber;
        this.ChequeimagePath = ChequeimagePath;
        this.ChequeNote = ChequeNote;
        this.ChequeBankName = ChequeBankName;
    }
    public String getChequeBankName() {
        return ChequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        ChequeBankName = chequeBankName;
    }

    public String getChequeNote() {
        return ChequeNote;
    }

    public void setChequeNote(String chequeNote) {
        ChequeNote = chequeNote;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public double getUsedChequeAmt() {
        return usedChequeAmt;
    }

    public void setUsedChequeAmt(double usedChequeAmt) {
        this.usedChequeAmt = usedChequeAmt;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public double getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(double chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChequeimagePath() {
        return ChequeimagePath;
    }

    public void setChequeimagePath(String chequeimagePath) {
        ChequeimagePath = chequeimagePath;
    }
}