package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryDboyRatingOrderModel {

    @SerializedName("OrderId")
    private int OrderId;
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("Shopimage")
    private String  Shopimage;

    @SerializedName("ShopName")
    private String  ShopName;
    @SerializedName("ShippingAddress")
    private String  ShippingAddress;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getShopimage() {
        return Shopimage;
    }

    public void setShopimage(String shopimage) {
        Shopimage = shopimage;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }
}
