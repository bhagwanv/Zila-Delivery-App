
package com.sk.ziladelivery.data.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OrderDetail implements Serializable
{

    @SerializedName("OrderDispatchedDetailsId")

    private Integer orderDispatchedDetailsId;
    @SerializedName("OrderDetailsId")

    private Integer orderDetailsId;
    @SerializedName("OrderId")

    private Integer orderId;
    @SerializedName("QtyChangeReason")

    private String qtyChangeReason;
    @SerializedName("OrderDispatchedMasterId")

    private Integer orderDispatchedMasterId;
    @SerializedName("CustomerId")

    private Integer customerId;
    @SerializedName("CustomerName")

    private String customerName;
    @SerializedName("City")

    private String city;
    @SerializedName("Mobile")

    private String mobile;
    @SerializedName("OrderDate")

    private String orderDate;
    @SerializedName("CompanyId")

    private Integer companyId;
    @SerializedName("CityId")

    private Integer cityId;
    @SerializedName("SizePerUnit")

    private double sizePerUnit;
    @SerializedName("WarehouseId")

    private Integer warehouseId;
    @SerializedName("WarehouseName")

    private String warehouseName;
    @SerializedName("SubcategoryName")

    private String subcategoryName;
    @SerializedName("SubsubcategoryName")

    private String subsubcategoryName;
    @SerializedName("CategoryName")

    private String categoryName;
    @SerializedName("isDeleted")

    private Boolean isDeleted;
    @SerializedName("ItemId")

    private Integer itemId;
    @SerializedName("SellingSku")

    private Object sellingSku;
    @SerializedName("Itempic")

    private String itempic;
    @SerializedName("itemname")

    private String itemname;
    @SerializedName("SellingUnitName")

    private String sellingUnitName;
    @SerializedName("itemcode")

    private String itemcode;
    @SerializedName("Barcode")

    private String barcode;
    @SerializedName("price")

    private double price;
    @SerializedName("UnitPrice")

    private double unitPrice;
    @SerializedName("Purchaseprice")

    private double purchaseprice;
    @SerializedName("OnlineServiceTax")

    private Integer onlineServiceTax;
    @SerializedName("MinOrderQty")

    private Integer minOrderQty;
    @SerializedName("MinOrderQtyPrice")

    private double minOrderQtyPrice;
    @SerializedName("qty")

    private Integer qty;
    @SerializedName("Noqty")

    private Integer noqty;
    @SerializedName("AmtWithoutTaxDisc")

    private double amtWithoutTaxDisc;
    @SerializedName("AmtWithoutAfterTaxDisc")

    private double amtWithoutAfterTaxDisc;
    @SerializedName("TotalAmountAfterTaxDisc")

    private String totalAmountAfterTaxDisc;
    @SerializedName("NetAmmount")

    private Integer netAmmount;
    @SerializedName("DiscountPercentage")

    private Integer discountPercentage;
    @SerializedName("DiscountAmmount")

    private Integer discountAmmount;
    @SerializedName("NetAmtAfterDis")

    private Integer netAmtAfterDis;
    @SerializedName("TaxPercentage")

    private Integer taxPercentage;
    @SerializedName("SGSTTaxPercentage")

    private Double sGSTTaxPercentage;
    @SerializedName("SGSTTaxAmmount")

    private double sGSTTaxAmmount;
    @SerializedName("CGSTTaxPercentage")

    private Double cGSTTaxPercentage;
    @SerializedName("CGSTTaxAmmount")

    private double cGSTTaxAmmount;
    @SerializedName("TaxAmmount")

    private double taxAmmount;
    @SerializedName("TotalAmt")

    private double totalAmt;
    @SerializedName("UnitId")

    private Integer unitId;
    @SerializedName("Unitname")

    private Object unitname;
    @SerializedName("itemNumber")

    private String itemNumber;
    @SerializedName("HSNCode")

    private String hSNCode;
    @SerializedName("CreatedDate")

    private String createdDate;
    @SerializedName("UpdatedDate")

    private String updatedDate;
    @SerializedName("Deleted")

    private Boolean deleted;
    @SerializedName("Status")

    private String status;
    @SerializedName("itemMaster")

    private Object itemMaster;
    private final static long serialVersionUID = -3370646679405412521L;





    public Integer getOrderDispatchedDetailsId() {
        return orderDispatchedDetailsId;
    }

    public void setOrderDispatchedDetailsId(Integer orderDispatchedDetailsId) {
        this.orderDispatchedDetailsId = orderDispatchedDetailsId;
    }

    public Integer getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Integer orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getQtyChangeReason() {
        return qtyChangeReason;
    }

    public void setQtyChangeReason(String qtyChangeReason) {
        this.qtyChangeReason = qtyChangeReason;
    }

    public Integer getOrderDispatchedMasterId() {
        return orderDispatchedMasterId;
    }

    public void setOrderDispatchedMasterId(Integer orderDispatchedMasterId) {
        this.orderDispatchedMasterId = orderDispatchedMasterId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public double getSizePerUnit() {
        return sizePerUnit;
    }

    public void setSizePerUnit(double sizePerUnit) {
        this.sizePerUnit = sizePerUnit;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getSubsubcategoryName() {
        return subsubcategoryName;
    }

    public void setSubsubcategoryName(String subsubcategoryName) {
        this.subsubcategoryName = subsubcategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Object getSellingSku() {
        return sellingSku;
    }

    public void setSellingSku(Object sellingSku) {
        this.sellingSku = sellingSku;
    }

    public String getItempic() {
        return itempic;
    }

    public void setItempic(String itempic) {
        this.itempic = itempic;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getSellingUnitName() {
        return sellingUnitName;
    }

    public void setSellingUnitName(String sellingUnitName) {
        this.sellingUnitName = sellingUnitName;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getPurchaseprice() {
        return purchaseprice;
    }

    public void setPurchaseprice(double purchaseprice) {
        this.purchaseprice = purchaseprice;
    }

    public Integer getOnlineServiceTax() {
        return onlineServiceTax;
    }

    public void setOnlineServiceTax(Integer onlineServiceTax) {
        this.onlineServiceTax = onlineServiceTax;
    }

    public Integer getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(Integer minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public double getMinOrderQtyPrice() {
        return minOrderQtyPrice;
    }

    public void setMinOrderQtyPrice(double minOrderQtyPrice) {
        this.minOrderQtyPrice = minOrderQtyPrice;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getNoqty() {
        return noqty;
    }

    public void setNoqty(Integer noqty) {
        this.noqty = noqty;
    }

    public double getAmtWithoutTaxDisc() {
        return amtWithoutTaxDisc;
    }

    public void setAmtWithoutTaxDisc(double amtWithoutTaxDisc) {
        this.amtWithoutTaxDisc = amtWithoutTaxDisc;
    }

    public double getAmtWithoutAfterTaxDisc() {
        return amtWithoutAfterTaxDisc;
    }

    public void setAmtWithoutAfterTaxDisc(double amtWithoutAfterTaxDisc) {
        this.amtWithoutAfterTaxDisc = amtWithoutAfterTaxDisc;
    }

    public String getTotalAmountAfterTaxDisc() {
        return totalAmountAfterTaxDisc;
    }

    public void setTotalAmountAfterTaxDisc(String totalAmountAfterTaxDisc) {
        this.totalAmountAfterTaxDisc = totalAmountAfterTaxDisc;
    }

    public Integer getNetAmmount() {
        return netAmmount;
    }

    public void setNetAmmount(Integer netAmmount) {
        this.netAmmount = netAmmount;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getDiscountAmmount() {
        return discountAmmount;
    }

    public void setDiscountAmmount(Integer discountAmmount) {
        this.discountAmmount = discountAmmount;
    }

    public Integer getNetAmtAfterDis() {
        return netAmtAfterDis;
    }

    public void setNetAmtAfterDis(Integer netAmtAfterDis) {
        this.netAmtAfterDis = netAmtAfterDis;
    }

    public Integer getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Integer taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Double getSGSTTaxPercentage() {
        return sGSTTaxPercentage;
    }

    public void setSGSTTaxPercentage(Double sGSTTaxPercentage) {
        this.sGSTTaxPercentage = sGSTTaxPercentage;
    }

    public double getSGSTTaxAmmount() {
        return sGSTTaxAmmount;
    }

    public void setSGSTTaxAmmount(double sGSTTaxAmmount) {
        this.sGSTTaxAmmount = sGSTTaxAmmount;
    }

    public Double getCGSTTaxPercentage() {
        return cGSTTaxPercentage;
    }

    public void setCGSTTaxPercentage(Double cGSTTaxPercentage) {
        this.cGSTTaxPercentage = cGSTTaxPercentage;
    }

    public double getCGSTTaxAmmount() {
        return cGSTTaxAmmount;
    }

    public void setCGSTTaxAmmount(double cGSTTaxAmmount) {
        this.cGSTTaxAmmount = cGSTTaxAmmount;
    }

    public double getTaxAmmount() {
        return taxAmmount;
    }

    public void setTaxAmmount(double taxAmmount) {
        this.taxAmmount = taxAmmount;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Object getUnitname() {
        return unitname;
    }

    public void setUnitname(Object unitname) {
        this.unitname = unitname;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getHSNCode() {
        return hSNCode;
    }

    public void setHSNCode(String hSNCode) {
        this.hSNCode = hSNCode;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getItemMaster() {
        return itemMaster;
    }

    public void setItemMaster(Object itemMaster) {
        this.itemMaster = itemMaster;
    }

}
