package com.sk.ziladelivery.ui.views.viewmodels;

import com.google.gson.annotations.SerializedName;

public class HistoryAdapterViewModel {
    @SerializedName("OrderId")
    private String OrderId;

    @SerializedName("ShopName")
    private String ShopName;

    @SerializedName("OrderedDate")
    private String OrderedDate;

    @SerializedName("Status")
    private String Status;

    @SerializedName("ShippingAddress")
    private String ShippingAddress;


    @SerializedName("Delivered")
    private String Delivered;

     @SerializedName("Canceled")
     private String Canceled;

    @SerializedName("IssuanceStatus")
    private String IssuanceStatus;


    @SerializedName("DeliveryIssuanceId")
    private String DeliveryIssuanceId;

    public String getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(String deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    @SerializedName("TotalAmount")
    private String TotalAmount;

    @SerializedName("Date")
    private String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDelivered() {
        return Delivered;
    }

    public void setDelivered(String delivered) {
        Delivered = delivered;
    }

    public String getCanceled() {
        return Canceled;
    }

    public void setCanceled(String canceled) {
        Canceled = canceled;
    }

    public String getIssuanceStatus() {
        return IssuanceStatus;
    }

    public void setIssuanceStatus(String issuanceStatus) {
        IssuanceStatus = issuanceStatus;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }




    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getOrderedDate() {
        return OrderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        OrderedDate = orderedDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }
}
