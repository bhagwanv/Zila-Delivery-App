package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyTaskModel implements Serializable {
    @SerializedName("ClusterId")
    private int ClusterId;
    @SerializedName("lg")
    private double lg;
    @SerializedName("lat")
    private double lat;
    @SerializedName("check")
    private boolean check;
    @SerializedName("OrderDetailsCount")
    private int OrderDetailsCount;
    @SerializedName("OrderDate")
    private String OrderDate;
    @SerializedName("deliveryCharge")
    private double deliveryCharge;
    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;
    @SerializedName("DivisionId")
    private int DivisionId;
    @SerializedName("Deleted")
    private boolean Deleted;
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @SerializedName("Deliverydate")
    private String Deliverydate;
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @SerializedName("active")
    private boolean active;
    @SerializedName("WarehouseName")
    private String WarehouseName;
    @SerializedName("WarehouseId")
    private int WarehouseId;
    @SerializedName("CityId")
    private int CityId;
    @SerializedName("ReDispatchCount")
    private int ReDispatchCount;
    @SerializedName("DboyMobileNo")
    private String DboyMobileNo;
    @SerializedName("DboyName")
    private String DboyName;
    @SerializedName("RecivedAmount")
    private double RecivedAmount;
    @SerializedName("PaymentAmount")
    private double PaymentAmount;
    @SerializedName("CashAmount")
    private double CashAmount;
    @SerializedName("ElectronicAmount")
    private double ElectronicAmount;
    @SerializedName("CheckAmount")
    private double CheckAmount;
    @SerializedName("TaxAmount")
    private double TaxAmount;
    @SerializedName("DiscountAmount")
    private double DiscountAmount;
    @SerializedName("GrossAmount")
    private double GrossAmount;
    @SerializedName("TotalAmount")
    private double TotalAmount;
    @SerializedName("ShippingAddress")
    private String ShippingAddress;
    @SerializedName("BillingAddress")
    private String BillingAddress;
    @SerializedName("Customerphonenum")
    private String Customerphonenum;
    @SerializedName("CustomerCategoryId")
    private int CustomerCategoryId;
    @SerializedName("invoice_no")
    private String invoice_no;
    @SerializedName("Status")
    private String Status;
    @SerializedName("Skcode")
    private String Skcode;
    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("CompanyId")
    private int CompanyId;
    @SerializedName("OrderSeq")
    private int OrderSeq;
    @SerializedName("OrderId")
    private int OrderId;
    @SerializedName("OrderDispatchedMasterId")
    private int OrderDispatchedMasterId;
    @SerializedName("ColourCode")
    private String colorCode;

    private double Distance;

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public int getClusterId() {
        return ClusterId;
    }

    public void setClusterId(int ClusterId) {
        this.ClusterId = ClusterId;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getOrderDetailsCount() {
        return OrderDetailsCount;
    }

    public void setOrderDetailsCount(int OrderDetailsCount) {
        this.OrderDetailsCount = OrderDetailsCount;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }

    public int getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(int DivisionId) {
        this.DivisionId = DivisionId;
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

    public String getDeliverydate() {
        return Deliverydate;
    }

    public void setDeliverydate(String Deliverydate) {
        this.Deliverydate = Deliverydate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String WarehouseName) {
        this.WarehouseName = WarehouseName;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int WarehouseId) {
        this.WarehouseId = WarehouseId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int CityId) {
        this.CityId = CityId;
    }

    public int getReDispatchCount() {
        return ReDispatchCount;
    }

    public void setReDispatchCount(int ReDispatchCount) {
        this.ReDispatchCount = ReDispatchCount;
    }

    public String getDboyMobileNo() {
        return DboyMobileNo;
    }

    public void setDboyMobileNo(String DboyMobileNo) {
        this.DboyMobileNo = DboyMobileNo;
    }

    public String getDboyName() {
        return DboyName;
    }

    public void setDboyName(String DboyName) {
        this.DboyName = DboyName;
    }

    public double getRecivedAmount() {
        return RecivedAmount;
    }

    public void setRecivedAmount(double RecivedAmount) {
        this.RecivedAmount = RecivedAmount;
    }

    public double getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(double PaymentAmount) {
        this.PaymentAmount = PaymentAmount;
    }

    public double getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(double CashAmount) {
        this.CashAmount = CashAmount;
    }

    public double getElectronicAmount() {
        return ElectronicAmount;
    }

    public void setElectronicAmount(double ElectronicAmount) {
        this.ElectronicAmount = ElectronicAmount;
    }

    public double getCheckAmount() {
        return CheckAmount;
    }

    public void setCheckAmount(double CheckAmount) {
        this.CheckAmount = CheckAmount;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double TaxAmount) {
        this.TaxAmount = TaxAmount;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(double DiscountAmount) {
        this.DiscountAmount = DiscountAmount;
    }

    public double getGrossAmount() {
        return GrossAmount;
    }

    public void setGrossAmount(double GrossAmount) {
        this.GrossAmount = GrossAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String ShippingAddress) {
        this.ShippingAddress = ShippingAddress;
    }

    public String getBillingAddress() {
        return BillingAddress;
    }

    public void setBillingAddress(String BillingAddress) {
        this.BillingAddress = BillingAddress;
    }

    public String getCustomerphonenum() {
        return Customerphonenum;
    }

    public void setCustomerphonenum(String Customerphonenum) {
        this.Customerphonenum = Customerphonenum;
    }

    public int getCustomerCategoryId() {
        return CustomerCategoryId;
    }

    public void setCustomerCategoryId(int CustomerCategoryId) {
        this.CustomerCategoryId = CustomerCategoryId;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String Skcode) {
        this.Skcode = Skcode;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int CustomerId) {
        this.CustomerId = CustomerId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int CompanyId) {
        this.CompanyId = CompanyId;
    }

    public int getOrderSeq() {
        return OrderSeq;
    }

    public void setOrderSeq(int OrderSeq) {
        this.OrderSeq = OrderSeq;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    public int getOrderDispatchedMasterId() {
        return OrderDispatchedMasterId;
    }

    public void setOrderDispatchedMasterId(int OrderDispatchedMasterId) {
        this.OrderDispatchedMasterId = OrderDispatchedMasterId;
    }

    public String getColorCode() {
        return colorCode;
    }
}
