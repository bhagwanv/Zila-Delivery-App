package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {

    @Expose
    @SerializedName("orderDetails")
    private List<OrderDispatchedObj> orderDetails;
    @Expose
    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;
    @Expose
    @SerializedName("ClusterId")
    private int ClusterId;
    @Expose
    @SerializedName("lg")
    private double lg;
    @Expose
    @SerializedName("lat")
    private double lat;
    @Expose
    @SerializedName("check")
    private boolean check;
    @Expose
    @SerializedName("OrderDate")
    private String OrderDate;
    @Expose
    @SerializedName("deliveryCharge")
    private double deliveryCharge;
    @Expose
    @SerializedName("DivisionId")
    private int DivisionId;
    @Expose
    @SerializedName("Deleted")
    private boolean Deleted;
    @Expose
    @SerializedName("UpdatedDate")
    private String UpdatedDate;
    @Expose
    @SerializedName("Deliverydate")
    private String Deliverydate;
    @Expose
    @SerializedName("CreatedDate")
    private String CreatedDate;
    @Expose
    @SerializedName("active")
    private boolean active;
    @Expose
    @SerializedName("WarehouseName")
    private String WarehouseName;
    @Expose
    @SerializedName("WarehouseId")
    private int WarehouseId;
    @Expose
    @SerializedName("CityId")
    private int CityId;
    @Expose
    @SerializedName("ReDispatchCount")
    private int ReDispatchCount;
    @Expose
    @SerializedName("DboyMobileNo")
    private String DboyMobileNo;
    @Expose
    @SerializedName("DboyName")
    private String DboyName;
    @Expose
    @SerializedName("RecivedAmount")
    private double RecivedAmount;
    @Expose
    @SerializedName("PaymentAmount")
    private double PaymentAmount;
    @Expose
    @SerializedName("CashAmount")
    private double CashAmount;
    @Expose
    @SerializedName("ElectronicAmount")
    private double ElectronicAmount;
    @Expose
    @SerializedName("CheckAmount")
    private double CheckAmount;
    @Expose
    @SerializedName("TaxAmount")
    private double TaxAmount;
    @Expose
    @SerializedName("DiscountAmount")
    private double DiscountAmount;
    @Expose
    @SerializedName("GrossAmount")
    private double GrossAmount;
    @Expose
    @SerializedName("TotalAmount")
    private double TotalAmount;
    @Expose
    @SerializedName("ShippingAddress")
    private String ShippingAddress;
    @Expose
    @SerializedName("BillingAddress")
    private String BillingAddress;
    @Expose
    @SerializedName("Customerphonenum")
    private String Customerphonenum;
    @Expose
    @SerializedName("CustomerCategoryId")
    private int CustomerCategoryId;
    @Expose
    @SerializedName("invoice_no")
    private String invoice_no;
    @Expose
    @SerializedName("Status")
    private String Status;
    @Expose
    @SerializedName("Skcode")
    private String Skcode;
    @Expose
    @SerializedName("ShopName")
    private String ShopName;
    @Expose
    @SerializedName("CustomerName")
    private String CustomerName;
    @Expose
    @SerializedName("CustomerId")
    private int CustomerId;
    @Expose
    @SerializedName("CompanyId")
    private int CompanyId;
    @Expose
    @SerializedName("OrderSeq")
    private int OrderSeq;
    @Expose
    @SerializedName("OrderId")
    private int OrderId;
    @Expose
    @SerializedName("OrderDispatchedMasterId")
    private int OrderDispatchedMasterId;

    @Expose
    @SerializedName("ShortItemAssignment")
    private ArrayList<ShortItemeModel> shortItemeAl;


    @SerializedName("PaymentDetails")
    private ArrayList<PaymentDetails> PaymentDetails;

    public ArrayList<PaymentDetails> getPaymentDetails() {
        return PaymentDetails;
    }

    public void setPaymentDetails(ArrayList<PaymentDetails> paymentDetails) {
        PaymentDetails = paymentDetails;
    }

    public ArrayList<ShortItemeModel> getShortItemeAl() {
        return shortItemeAl;
    }

    public void setShortItemeAl(ArrayList<ShortItemeModel> shortItemeAl) {
        this.shortItemeAl = shortItemeAl;
    }

    public List<OrderDispatchedObj> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDispatchedObj> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
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



}


