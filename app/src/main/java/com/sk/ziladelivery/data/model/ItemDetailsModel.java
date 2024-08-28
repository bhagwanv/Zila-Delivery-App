package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public class ItemDetailsModel {
        @SerializedName("ItemId")
        String ItemId;

        @SerializedName("qty")
        int qty;

        @SerializedName("WarehouseId")
        String WarehouseId;

        @SerializedName("CompanyId")
        String CompanyId;

    public ItemDetailsModel(String itemId, int qty, String warehouseId, String companyId) {
        ItemId = itemId;
        this.qty = qty;
        WarehouseId = warehouseId;
        CompanyId = companyId;
    }

    public String getItemId() {
            return ItemId;
        }

        public void setItemId(String itemId) {
            ItemId = itemId;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getWarehouseId() {
            return WarehouseId;
        }

        public void setWarehouseId(String warehouseId) {
            WarehouseId = warehouseId;
        }

        public String getCompanyId() {
            return CompanyId;
        }

        public void setCompanyId(String companyId) {
            CompanyId = companyId;
        }

}
