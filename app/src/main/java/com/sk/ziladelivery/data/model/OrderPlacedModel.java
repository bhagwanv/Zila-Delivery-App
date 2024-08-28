package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderPlacedModel {
    @SerializedName("OrderDispatchedMasterId")
    int OrderDispatchedMasterId;

    @SerializedName("OrderId")
    int OrderId;

    @SerializedName("Status")
    String Status;

    @SerializedName("comments")
    String comments;

    @SerializedName("DeliveryIssuanceId")
    int DeliveryIssuanceId;

    @SerializedName("CashAmount")
    String CashAmount;

    @SerializedName("RecivedAmount")
    double RecivedAmount;

    @SerializedName("OnlineServiceTax")
    double OnlineServiceTax;

    @SerializedName("Signimg")
    String Signimg;

    @SerializedName("DboyMobileNo")
    String DboyMobileNo;

    @SerializedName("DboyName")
    String DboyName;

    @SerializedName("ReDispatchCount")
    int ReDispatchCount;

    @SerializedName("WarehouseId")
    int WarehouseId;


    @SerializedName("paymentMode")
    String paymentMode;

    @SerializedName("DSignimg ")
    byte[] DSignimg;

    @SerializedName("paymentThrough")
    String paymentThrough;


    @SerializedName("DeliveryLat")
    String DeliveryLat;


    @SerializedName("DeliveryLng")
    String DeliveryLng;


    @SerializedName("ReDispatchedDate")
    String ReDispatchedDate;


    @SerializedName("DeliveryPayments")
    private ArrayList<DeliveryPayments> payment;

    @SerializedName("DeliveryOptimizationDc")
    public DeliveryOptimizationDc deliveryOptimizationDc;

    public String getReDispatchedDate() {
        return ReDispatchedDate;
    }

    public void setReDispatchedDate(String reDispatchedDate) {
        ReDispatchedDate = reDispatchedDate;
    }
    public ArrayList<DeliveryPayments> getPayment() {
        return payment;
    }

    public void setPayment(ArrayList<DeliveryPayments> payment) {
        this.payment = payment;
    }


    public OrderPlacedModel(int orderDispatchedMasterId, int orderId, String status, String comments, int deliveryIssuanceId, String cashAmount, double recivedAmount, double onlineServiceTax, String signimg, String dboyMobileNo, String dboyName, int reDispatchCount, int warehouseId, String paymentThrough, String DeliveryLat, String DeliveryLng, ArrayList<DeliveryPayments> payment,DeliveryOptimizationDc deliveryOptimizationDc,String ReDispatchedDate) {
        this.OrderDispatchedMasterId = orderDispatchedMasterId;
        this.OrderId = orderId;
        this.Status = status;
        this.comments = comments;
        this.DeliveryIssuanceId = deliveryIssuanceId;
        this.CashAmount = cashAmount;
        this.RecivedAmount = recivedAmount;
        this.OnlineServiceTax = onlineServiceTax;
        this.Signimg = signimg;
        this.DboyMobileNo = dboyMobileNo;
        this.DboyName = dboyName;
        this.ReDispatchCount = reDispatchCount;
        this.WarehouseId = warehouseId;
        this.paymentThrough = paymentThrough;
        this.DeliveryLat = DeliveryLat;
        this.DeliveryLng = DeliveryLng;
        this.payment = payment;
        this.ReDispatchedDate = ReDispatchedDate;
        this.deliveryOptimizationDc = deliveryOptimizationDc;

    }

 public OrderPlacedModel(int orderDispatchedMasterId, int orderId, String status, String comments, int deliveryIssuanceId, String cashAmount, double recivedAmount, double onlineServiceTax, String signimg, String dboyMobileNo, String dboyName, int reDispatchCount, int warehouseId, String paymentThrough, String DeliveryLat, String DeliveryLng, ArrayList<DeliveryPayments> payment) {
        this.OrderDispatchedMasterId = orderDispatchedMasterId;
        this.OrderId = orderId;
        this.Status = status;
        this.comments = comments;
        this.DeliveryIssuanceId = deliveryIssuanceId;
        this.CashAmount = cashAmount;
        this.RecivedAmount = recivedAmount;
        this.OnlineServiceTax = onlineServiceTax;
        this.Signimg = signimg;
        this.DboyMobileNo = dboyMobileNo;
        this.DboyName = dboyName;
        this.ReDispatchCount = reDispatchCount;
        this.WarehouseId = warehouseId;
        this.paymentThrough = paymentThrough;
        this.DeliveryLat = DeliveryLat;
        this.DeliveryLng = DeliveryLng;
        this.payment = payment;

    }




}
