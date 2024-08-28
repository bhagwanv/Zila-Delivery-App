package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class RearrangModel {

    @SerializedName("Id")
    long Id;

    @SerializedName("Skcode")
    String Skcode;

    @SerializedName("ShopName")
    String ShopName;

    @SerializedName("OrderList")
    String OrderList;

    @SerializedName("ShippingAddress")
    String ShippingAddress;

    @SerializedName("SequenceNo")
    int SequenceNo;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String skcode) {
        Skcode = skcode;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getOrderList() {
        return OrderList;
    }

    public void setOrderList(String orderList) {
        OrderList = orderList;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public int getSequenceNo() {
        return SequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        SequenceNo = sequenceNo;
    }

    public RearrangModel(long id, String skcode, String shopName, String orderList, String shippingAddress, int sequenceNo) {
        Id = id;
        Skcode = skcode;
        ShopName = shopName;
        OrderList = orderList;
        ShippingAddress = shippingAddress;
        SequenceNo = sequenceNo;
    }
}

