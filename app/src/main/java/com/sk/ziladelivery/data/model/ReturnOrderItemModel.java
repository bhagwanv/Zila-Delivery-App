package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnOrderItemModel {
    @Expose
    @SerializedName("IsReplaceable")
    private boolean isReplaceable;
    @Expose
    @SerializedName("IsReturnReplaced")
    private boolean isReturnReplaced;
    @Expose
    @SerializedName("RequestType")
    private int requestType;
    @Expose
    @SerializedName("Qty")
    private int qty;
    @Expose
    @SerializedName("price")
    private double price;
    @Expose
    @SerializedName("ItemName")
    private String itemName;
    @Expose
    @SerializedName("Itempic")
    private String itemPic;
    @Expose
    @SerializedName("ItemId")
    private String itemId;
    @Expose
    @SerializedName("OrderDetailsId")
    private int orderDetailsId;
    private int returnQty;
    private boolean isSelected;

    public boolean isReplaceable() {
        return isReplaceable;
    }

    public boolean isReturnReplaced() {
        return isReturnReplaced;
    }

    public int getRequestType() {
        return requestType;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public String getItemPic() {
        return itemPic;
    }

    public String getItemId() {
        return itemId;
    }

    public int getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(int returnQty) {
        this.returnQty = returnQty;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}