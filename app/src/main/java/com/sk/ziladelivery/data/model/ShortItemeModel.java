package com.sk.ziladelivery.data.model;

public class ShortItemeModel {


    private String Id;
    private String ItemId;
    private String itemNumber;
    private String itemname;
    private String ItemAssignmentQty;
    private String ItemShortQty;
    private String DeliveryIssuanceId;
    private String DboyId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemAssignmentQty() {
        return ItemAssignmentQty;
    }

    public void setItemAssignmentQty(String itemAssignmentQty) {
        ItemAssignmentQty = itemAssignmentQty;
    }

    public String getItemShortQty() {
        return ItemShortQty;
    }

    public void setItemShortQty(String itemShortQty) {
        ItemShortQty = itemShortQty;
    }

    public String getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(String deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public String getDboyId() {
        return DboyId;
    }

    public void setDboyId(String dboyId) {
        DboyId = dboyId;
    }



}
