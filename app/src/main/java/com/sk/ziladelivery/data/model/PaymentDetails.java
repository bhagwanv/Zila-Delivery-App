package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;


public class PaymentDetails {

    @SerializedName("TransDate")
    private String TransDate;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("Amount")
    private double Amount;

    @SerializedName("TransRefNo")
    private String TransRefNo;

    @SerializedName("ChequeImageUrl")
    private String ChequeImageUrl;

    @SerializedName("ChequeBankName")
    private String ChequeBankName;


    @SerializedName("IsEditRTGS")
    private boolean IsEditRTGS;

    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    @SerializedName("PaymentResponseRetailerAppId")
    public int paymentResponseRetailerAppId;



    public boolean isEditRTGS() {
        return IsEditRTGS;
    }

    public void setEditRTGS(boolean editRTGS) {
        IsEditRTGS = editRTGS;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public String getChequeImageUrl() {
        return ChequeImageUrl;
    }

    public void setChequeImageUrl(String chequeImageUrl) {
        ChequeImageUrl = chequeImageUrl;
    }

    public String getChequeBankName() {
        return ChequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        ChequeBankName = chequeBankName;
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String TransDate) {
        this.TransDate = TransDate;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String PaymentFrom) {
        this.PaymentFrom = PaymentFrom;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public String getTransRefNo() {
        return TransRefNo;
    }

    public void setTransRefNo(String TransRefNo) {
        this.TransRefNo = TransRefNo;
    }
}
