package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class Payments {

    @SerializedName("UpdatedDate ")
    private String UpdatedDate;

    @SerializedName("CreatedDate ")
    private String CreatedDate;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusDesc")
    private String statusDesc;

    @SerializedName("status")
    private String status;

    @SerializedName("category")
    private String category;

    @SerializedName("currencyCode")
    private String currencyCode;

    @SerializedName("amount")
    private double amount;

    @SerializedName("eplOrderId")
    private String eplOrderId;

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("marketplaceOrderId")
    private String marketplaceOrderId;

    @SerializedName("mCode")
    private String mCode;

    @SerializedName("Skcode")
    private String Skcode;

    @SerializedName("GatewayTransId")
    private String GatewayTransId;

    @SerializedName("ChequeImageUrl")
    private String ChequeImageUrl;


    @SerializedName("ChequeBankName")
    private String ChequeBankName;
    @SerializedName("IsOnline")
    private boolean IsOnline;

    public boolean isOnline() {
        return IsOnline;
    }

    public void setOnline(boolean online) {
        IsOnline = online;
    }





    @SerializedName("id")
    private int id;
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
    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGatewayTransId() {
        return GatewayTransId;
    }

    public void setGatewayTransId(String gatewayTransId) {
        GatewayTransId = gatewayTransId;
    }

    public String getEplOrderId() {
        return eplOrderId;
    }

    public void setEplOrderId(String eplOrderId) {
        this.eplOrderId = eplOrderId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getMarketplaceOrderId() {
        return marketplaceOrderId;
    }

    public void setMarketplaceOrderId(String marketplaceOrderId) {
        this.marketplaceOrderId = marketplaceOrderId;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String skcode) {
        Skcode = skcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
