package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

public  class OrderItemDetails {
    @SerializedName("Status")
    private String status;
    @SerializedName("Deleted")
    private boolean deleted;
    @SerializedName("UpdatedDate")
    private String updateddate;
    @SerializedName("CreatedDate")
    private String createddate;
    @SerializedName("HSNCode")
    private String hsncode;
    @SerializedName("itemNumber")
    private String itemnumber;
    @SerializedName("UnitId")
    private int unitid;
    @SerializedName("TotalAmt")
    private double totalamt;
    @SerializedName("TaxAmmount")
    private double taxammount;
    @SerializedName("CGSTTaxAmmount")
    private double cgsttaxammount;
    @SerializedName("CGSTTaxPercentage")
    private double cgsttaxpercentage;
    @SerializedName("SGSTTaxAmmount")
    private double sgsttaxammount;
    @SerializedName("SGSTTaxPercentage")
    private double sgsttaxpercentage;
    @SerializedName("TaxPercentage")
    private double taxpercentage;
    @SerializedName("NetAmtAfterDis")
    private double netamtafterdis;
    @SerializedName("DiscountAmmount")
    private double discountammount;
    @SerializedName("DiscountPercentage")
    private double discountpercentage;
    @SerializedName("NetAmmount")
    private double netammount;
    @SerializedName("TotalAmountAfterTaxDisc")
    private double totalamountaftertaxdisc;
    @SerializedName("AmtWithoutAfterTaxDisc")
    private double amtwithoutaftertaxdisc;
    @SerializedName("AmtWithoutTaxDisc")
    private double amtwithouttaxdisc;
    @SerializedName("Noqty")
    private int noqty;
    @SerializedName("qty")
    private int qty;
    @SerializedName("MinOrderQtyPrice")
    private double minorderqtyprice;
    @SerializedName("MinOrderQty")
    private int minorderqty;
    @SerializedName("OnlineServiceTax")
    private double onlineservicetax;
    @SerializedName("Purchaseprice")
    private double purchaseprice;
    @SerializedName("UnitPrice")
    private double unitprice;
    @SerializedName("price")
    private double price;
    @SerializedName("Barcode")
    private String barcode;
    @SerializedName("itemcode")
    private String itemcode;
    @SerializedName("SellingUnitName")
    private String sellingunitname;
    @SerializedName("itemname")
    private String itemname;
    @SerializedName("Itempic")
    private String itempic;
    @SerializedName("ItemId")
    private int itemid;
    @SerializedName("isDeleted")
    private boolean isdeleted;
    @SerializedName("CategoryName")
    private String categoryname;
    @SerializedName("SubsubcategoryName")
    private String subsubcategoryname;
    @SerializedName("SubcategoryName")
    private String subcategoryname;
    @SerializedName("WarehouseName")
    private String warehousename;
    @SerializedName("WarehouseId")
    private int warehouseid;
    @SerializedName("SizePerUnit")
    private double sizeperunit;
    @SerializedName("CityId")
    private int cityid;
    @SerializedName("CompanyId")
    private int companyid;
    @SerializedName("OrderDate")
    private String orderdate;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("City")
    private String city;
    @SerializedName("CustomerName")
    private String customername;
    @SerializedName("CustomerId")
    private int customerid;
    @SerializedName("OrderDispatchedMasterId")
    private int orderdispatchedmasterid;
    @SerializedName("QtyChangeReason")
    private String qtychangereason;
    @SerializedName("OrderId")
    private int orderid;
    @SerializedName("OrderDetailsId")
    private int orderdetailsid;
    @SerializedName("OrderDispatchedDetailsId")
    private int orderdispatcheddetailsid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(String updateddate) {
        this.updateddate = updateddate;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getHsncode() {
        return hsncode;
    }

    public void setHsncode(String hsncode) {
        this.hsncode = hsncode;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public int getUnitid() {
        return unitid;
    }

    public void setUnitid(int unitid) {
        this.unitid = unitid;
    }

    public double getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(double totalamt) {
        this.totalamt = totalamt;
    }

    public double getTaxammount() {
        return taxammount;
    }

    public void setTaxammount(double taxammount) {
        this.taxammount = taxammount;
    }

    public double getCgsttaxammount() {
        return cgsttaxammount;
    }

    public void setCgsttaxammount(double cgsttaxammount) {
        this.cgsttaxammount = cgsttaxammount;
    }

    public double getCgsttaxpercentage() {
        return cgsttaxpercentage;
    }

    public void setCgsttaxpercentage(double cgsttaxpercentage) {
        this.cgsttaxpercentage = cgsttaxpercentage;
    }

    public double getSgsttaxammount() {
        return sgsttaxammount;
    }

    public void setSgsttaxammount(double sgsttaxammount) {
        this.sgsttaxammount = sgsttaxammount;
    }

    public double getSgsttaxpercentage() {
        return sgsttaxpercentage;
    }

    public void setSgsttaxpercentage(double sgsttaxpercentage) {
        this.sgsttaxpercentage = sgsttaxpercentage;
    }

    public double getTaxpercentage() {
        return taxpercentage;
    }

    public void setTaxpercentage(double taxpercentage) {
        this.taxpercentage = taxpercentage;
    }

    public double getNetamtafterdis() {
        return netamtafterdis;
    }

    public void setNetamtafterdis(double netamtafterdis) {
        this.netamtafterdis = netamtafterdis;
    }

    public double getDiscountammount() {
        return discountammount;
    }

    public void setDiscountammount(double discountammount) {
        this.discountammount = discountammount;
    }

    public double getDiscountpercentage() {
        return discountpercentage;
    }

    public void setDiscountpercentage(double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public double getNetammount() {
        return netammount;
    }

    public void setNetammount(double netammount) {
        this.netammount = netammount;
    }

    public double getTotalamountaftertaxdisc() {
        return totalamountaftertaxdisc;
    }

    public void setTotalamountaftertaxdisc(double totalamountaftertaxdisc) {
        this.totalamountaftertaxdisc = totalamountaftertaxdisc;
    }

    public double getAmtwithoutaftertaxdisc() {
        return amtwithoutaftertaxdisc;
    }

    public void setAmtwithoutaftertaxdisc(double amtwithoutaftertaxdisc) {
        this.amtwithoutaftertaxdisc = amtwithoutaftertaxdisc;
    }

    public double getAmtwithouttaxdisc() {
        return amtwithouttaxdisc;
    }

    public void setAmtwithouttaxdisc(double amtwithouttaxdisc) {
        this.amtwithouttaxdisc = amtwithouttaxdisc;
    }

    public int getNoqty() {
        return noqty;
    }

    public void setNoqty(int noqty) {
        this.noqty = noqty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getMinorderqtyprice() {
        return minorderqtyprice;
    }

    public void setMinorderqtyprice(double minorderqtyprice) {
        this.minorderqtyprice = minorderqtyprice;
    }

    public int getMinorderqty() {
        return minorderqty;
    }

    public void setMinorderqty(int minorderqty) {
        this.minorderqty = minorderqty;
    }

    public double getOnlineservicetax() {
        return onlineservicetax;
    }

    public void setOnlineservicetax(double onlineservicetax) {
        this.onlineservicetax = onlineservicetax;
    }

    public double getPurchaseprice() {
        return purchaseprice;
    }

    public void setPurchaseprice(double purchaseprice) {
        this.purchaseprice = purchaseprice;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getSellingunitname() {
        return sellingunitname;
    }

    public void setSellingunitname(String sellingunitname) {
        this.sellingunitname = sellingunitname;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItempic() {
        return itempic;
    }

    public void setItempic(String itempic) {
        this.itempic = itempic;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getSubsubcategoryname() {
        return subsubcategoryname;
    }

    public void setSubsubcategoryname(String subsubcategoryname) {
        this.subsubcategoryname = subsubcategoryname;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public int getWarehouseid() {
        return warehouseid;
    }

    public void setWarehouseid(int warehouseid) {
        this.warehouseid = warehouseid;
    }

    public double getSizeperunit() {
        return sizeperunit;
    }

    public void setSizeperunit(double sizeperunit) {
        this.sizeperunit = sizeperunit;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getOrderdispatchedmasterid() {
        return orderdispatchedmasterid;
    }

    public void setOrderdispatchedmasterid(int orderdispatchedmasterid) {
        this.orderdispatchedmasterid = orderdispatchedmasterid;
    }

    public String getQtychangereason() {
        return qtychangereason;
    }

    public void setQtychangereason(String qtychangereason) {
        this.qtychangereason = qtychangereason;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getOrderdetailsid() {
        return orderdetailsid;
    }

    public void setOrderdetailsid(int orderdetailsid) {
        this.orderdetailsid = orderdetailsid;
    }

    public int getOrderdispatcheddetailsid() {
        return orderdispatcheddetailsid;
    }

    public void setOrderdispatcheddetailsid(int orderdispatcheddetailsid) {
        this.orderdispatcheddetailsid = orderdispatcheddetailsid;
    }
}
