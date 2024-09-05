package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryPayments {

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("TransId")
    private String TransId;

    @SerializedName("amount")
    private double amount;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("ChequeImageUrl")
    private String ChequeImageUrl;

    @SerializedName("ChequeBankName")
    private String ChequeBankName;

    @SerializedName("PaymentDate")
    private String PaymentDate;

    @SerializedName("IsVAN_RTGSNEFT")
    private boolean vanRTGS;

    private boolean isVeryfied;

    private boolean flag;


    public DeliveryPayments(int orderId, String transId, double amount, String paymentFrom, String chequeImageUrl, String chequeBankName, String paymentDate, boolean vanRTGS) {
        this. OrderId = orderId;
        this. TransId = transId;
        this.amount = amount;
        this. PaymentFrom = paymentFrom;
        this. ChequeImageUrl = chequeImageUrl;
        this.ChequeBankName = chequeBankName;
        this. PaymentDate = paymentDate;
        this. vanRTGS = vanRTGS;

    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public boolean isVeryfied() {
        return isVeryfied;
    }

    public void setVeryfied(boolean veryfied) {
        isVeryfied = veryfied;
    }
    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
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
        this. ChequeBankName = chequeBankName;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public boolean getVanRTGS() {
        return vanRTGS;
    }

    public void setVanRTGS(boolean vanRTGS) {
        this.vanRTGS = vanRTGS;
    }
}
