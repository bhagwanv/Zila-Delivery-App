package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class ShortIssuanceModel {



    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("CreatedBy")
    private String CreatedBy;

    @SerializedName("Deleted")
    private boolean Deleted;

    @SerializedName("UpdatedDate")
    private String UpdatedDate;

    @SerializedName("CreatedDate")
    private String CreatedDate;

    @SerializedName("DboyId")
    private int DboyId;

    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    @SerializedName("NotInStockComment")
    private String NotInStockComment;

    @SerializedName("DamageComment")
    private String DamageComment;

    @SerializedName("NotinStockQty")
    private int NotinStockQty;

    @SerializedName("DamageStockQty")
    private int DamageStockQty;

    @SerializedName("Orderqty")
    private int Orderqty;

    @SerializedName("itemname")
    private String itemname;

    @SerializedName("itemNumber")
    private String itemNumber;

    @SerializedName("ItemId")
    private int ItemId;

    @SerializedName("Id")
    private int Id;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public boolean getDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean Deleted) {
        this.Deleted = Deleted;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public int getDboyId() {
        return DboyId;
    }

    public void setDboyId(int DboyId) {
        this.DboyId = DboyId;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }

    public String getNotInStockComment() {
        return NotInStockComment;
    }

    public void setNotInStockComment(String NotInStockComment) {
        this.NotInStockComment = NotInStockComment;
    }

    public String getDamageComment() {
        return DamageComment;
    }

    public void setDamageComment(String DamageComment) {
        this.DamageComment = DamageComment;
    }

    public int getNotinStockQty() {
        return NotinStockQty;
    }

    public void setNotinStockQty(int NotinStockQty) {
        this.NotinStockQty = NotinStockQty;
    }

    public int getDamageStockQty() {
        return DamageStockQty;
    }

    public void setDamageStockQty(int DamageStockQty) {
        this.DamageStockQty = DamageStockQty;
    }

    public int getOrderqty() {
        return Orderqty;
    }

    public void setOrderqty(int Orderqty) {
        this.Orderqty = Orderqty;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int ItemId) {
        this.ItemId = ItemId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}
