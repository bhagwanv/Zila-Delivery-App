package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class QRCodeResponceModel {

    @SerializedName("TxnAmount")
    Double TxnAmount;

    @SerializedName("TxnNo")
    String TxnNo;

    @SerializedName("UPITxnID")
    String UPITxnID;



    @SerializedName("TxnStatus")
    String TxnStatus;

    @SerializedName("TxnDate")
    String TxnDate;

    public Double getTxnAmount() {
        return TxnAmount;
    }

    public void setTxnAmount(Double txnAmount) {
        TxnAmount = txnAmount;
    }

    public String getTxnNo() {
        return TxnNo;
    }

    public void setTxnNo(String txnNo) {
        TxnNo = txnNo;
    }

    public String getTxnStatus() {
        return TxnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        TxnStatus = txnStatus;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String txnDate) {
        TxnDate = txnDate;
    }

    public String getUPITxnID() {
        return UPITxnID;
    }

    public void setUPITxnID(String UPITxnID) {
        this.UPITxnID = UPITxnID;
    }
}
