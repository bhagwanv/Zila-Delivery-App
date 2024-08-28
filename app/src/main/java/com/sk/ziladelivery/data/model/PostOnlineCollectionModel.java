package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class PostOnlineCollectionModel {
    @SerializedName("Orderid")
    private int orderid;

    @SerializedName("PaymentGetwayAmt")
    private double paymentGetwayAmt;

    @SerializedName("MPOSAmt")
    private double mposAmt;

    @SerializedName("CurrencyCollectionId")
    private int currencyCollectionId;

    @SerializedName("Id")
    private int id;

    @SerializedName("MPOSReferenceNo")
    private String mposReferenceNo;

    @SerializedName("PaymentReferenceNO")
    private String paymentReferenceNO;

    private String DeliveryDate;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("IsEditRTGS")
    private boolean IsEditRTGS;

    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    @SerializedName("PaymentResponseRetailerAppId")
    public int paymentResponseRetailerAppId;


    public PostOnlineCollectionModel(int orderid, double paymentGetwayAmt, double mposAmt, int currencyCollectionId, int id, String mposReferenceNo, String paymentReferenceNO, String PaymentFrom, boolean isEditRTGS, int deliveryIssuanceId, int paymentResponseRetailerAppId) {
        this.orderid = orderid;
        this.paymentGetwayAmt = paymentGetwayAmt;
        this.mposAmt = mposAmt;
        this.currencyCollectionId = currencyCollectionId;
        this.id = id;
        this.mposReferenceNo = mposReferenceNo;
        this.paymentReferenceNO = paymentReferenceNO;
        this.PaymentFrom = PaymentFrom;
        this.IsEditRTGS = isEditRTGS;
        this.DeliveryIssuanceId = deliveryIssuanceId;
        this.paymentResponseRetailerAppId = paymentResponseRetailerAppId;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public boolean isEditRTGS() {
        return IsEditRTGS;
    }

    public void setEditRTGS(boolean editRTGS) {
        IsEditRTGS = editRTGS;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getMposReferenceNo() {
        return mposReferenceNo;
    }

    public void setMposReferenceNo(String mposReferenceNo) {
        this.mposReferenceNo = mposReferenceNo;
    }

    public String getPaymentReferenceNO() {
        return paymentReferenceNO;
    }

    public void setPaymentReferenceNO(String paymentReferenceNO) {
        this.paymentReferenceNO = paymentReferenceNO;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public double getPaymentGetwayAmt() {
        return paymentGetwayAmt;
    }

    public void setPaymentGetwayAmt(double paymentGetwayAmt) {
        this.paymentGetwayAmt = paymentGetwayAmt;
    }

    public double getMposAmt() {
        return mposAmt;
    }

    public void setMposAmt(double mposAmt) {
        this.mposAmt = mposAmt;
    }

    public int getCurrencyCollectionId() {
        return currencyCollectionId;
    }

    public void setCurrencyCollectionId(int currencyCollectionId) {
        this.currencyCollectionId = currencyCollectionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}