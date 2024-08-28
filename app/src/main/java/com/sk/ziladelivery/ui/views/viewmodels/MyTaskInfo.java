package com.sk.ziladelivery.ui.views.viewmodels;

public class MyTaskInfo {
    String orderId;
    String customerName;
    String date;
    String skCode;
    String shopName;
    String address;
    String itemCount;
    String totalAmount;
    String WarehouseAddress;
    String status;
    String completionTime;
    String unLoadingTime;
    boolean IsSkip;

    String CRMTags;

    public String getCRMTags() {
        return CRMTags;
    }

    public void setCRMTags(String CRMTags) {
        this.CRMTags = CRMTags;
    }

    public boolean isSkip() {
        return IsSkip;
    }

    public void setSkip(boolean skip) {
        IsSkip = skip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getUnLoadingTime() {
        return unLoadingTime;
    }

    public void setUnLoadingTime(String unLoadingTime) {
        this.unLoadingTime = unLoadingTime;
    }

    public String getWarehouseAddress() {
        return WarehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        WarehouseAddress = warehouseAddress;
    }


    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSkCode() {
        return skCode;
    }

    public void setSkCode(String skCode) {
        this.skCode = skCode;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAssignmentId() {
        return date;
    }

    public void setAssignmentId(String assignmentId) {
        this.date = assignmentId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
