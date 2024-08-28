package com.sk.ziladelivery.ui.views.viewmodels;

public class ShortViewModel {

    private String ShortItemId;
    private String ItemId;
    private String Qty;
    private String ShortQty;
    private String ItemName;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShortItemId() {
        return ShortItemId;
    }

    public void setShortItemId(String shortItemId) {
        ShortItemId = shortItemId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getShortQty() {
        return ShortQty;
    }

    public void setShortQty(String shortQty) {
        ShortQty = shortQty;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
