package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class PaymentGatewayModel {

    @SerializedName("OrderId")
    int OrderId;

    @SerializedName("GatewayOrderId")
    String GatewayOrderId;

    @SerializedName("GatewayTransId")
    String GatewayTransId;

    @SerializedName("amount")
    double amount;

    @SerializedName("currencyCode")
    String currencyCode;

    @SerializedName("status")
    String status;

    @SerializedName("statusDesc")
    String statusDesc;

    @SerializedName("statusCode")
    String statusCode;

    @SerializedName("PaymentFrom")
    String PaymentFrom;

    @SerializedName("GatewayRequest")
    String GatewayRequest;

    @SerializedName("GatewayResponse")
    String GatewayResponse;

    @SerializedName("PaymentThrough")
    String PaymentThrough;

    public PaymentGatewayModel(int orderId, String gatewayOrderId, String gatewayTransId, double amount, String currencyCode, String status, String statusDesc, String statusCode, String paymentFrom, String gatewayRequest, String gatewayResponse, String paymentThrough) {
        OrderId = orderId;
        GatewayOrderId = gatewayOrderId;
        GatewayTransId = gatewayTransId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.status = status;
        this.statusDesc = statusDesc;
        this.statusCode = statusCode;
        PaymentFrom = paymentFrom;
        GatewayRequest = gatewayRequest;
        GatewayResponse = gatewayResponse;
        PaymentThrough = paymentThrough;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getGatewayOrderId() {
        return GatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        GatewayOrderId = gatewayOrderId;
    }

    public String getGatewayTransId() {
        return GatewayTransId;
    }

    public void setGatewayTransId(String gatewayTransId) {
        GatewayTransId = gatewayTransId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }


    public String getPaymentThrough() {
        return PaymentThrough;
    }

    public void setPaymentThrough(String paymentThrough) {
        PaymentThrough = paymentThrough;
    }
}