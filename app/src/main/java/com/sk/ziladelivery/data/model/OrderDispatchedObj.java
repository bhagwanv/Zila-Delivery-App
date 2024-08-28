package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDispatchedObj {
    @SerializedName("DeliveryIssuanceId")
    private int deliveryissuanceid;
    @SerializedName("ClusterId")
    private int clusterid;
    @SerializedName("lg")
    private double lg;
    @SerializedName("lat")
    private double lat;
    @SerializedName("check")
    private boolean check;
    @SerializedName("OrderDate")
    private String orderdate;
    @SerializedName("deliveryCharge")
    private double deliverycharge;
    @SerializedName("DivisionId")
    private int divisionid;
    @SerializedName("Deleted")
    private boolean deleted;
    @SerializedName("UpdatedDate")
    private String updateddate;
    @SerializedName("Deliverydate")
    private String deliverydate;
    @SerializedName("CreatedDate")
    private String createddate;
    @SerializedName("active")
    private boolean active;
    @SerializedName("WarehouseName")
    private String warehousename;
    @SerializedName("WarehouseId")
    private int warehouseid;
    @SerializedName("CityId")
    private int cityid;
    @SerializedName("ReDispatchCount")
    private int redispatchcount;
    @SerializedName("DboyMobileNo")
    private String dboymobileno;
    @SerializedName("DboyName")
    private String dboyname;
    @SerializedName("RecivedAmount")
    private double recivedamount;
    @SerializedName("PaymentAmount")
    private double paymentamount;
    @SerializedName("CashAmount")
    private double cashamount;
    @SerializedName("ElectronicAmount")
    private double electronicamount;
    @SerializedName("CheckAmount")
    private double checkamount;
    @SerializedName("TaxAmount")
    private double taxamount;
    @SerializedName("DiscountAmount")
    private double discountamount;
    @SerializedName("GrossAmount")
    private double grossamount;
    @SerializedName("TotalAmount")
    private double totalamount;
    @SerializedName("ShippingAddress")
    private String shippingaddress;
    @SerializedName("BillingAddress")
    private String billingaddress;
    @SerializedName("Customerphonenum")
    private String customerphonenum;
    @SerializedName("CustomerCategoryId")
    private int customercategoryid;
    @SerializedName("invoice_no")
    private String invoiceNo;
    @SerializedName("Status")
    private String status;
    @SerializedName("Skcode")
    private String skcode;
    @SerializedName("ShopName")
    private String shopname;
    @SerializedName("CustomerName")
    private String customername;
    @SerializedName("CustomerId")
    private int customerid;
    @SerializedName("SalesPerson")
    private String salesperson;
    @SerializedName("SalesPersonId")
    private int salespersonid;
    @SerializedName("CompanyId")
    private int companyid;
    @SerializedName("OrderSeq")
    private int orderseq;
    @SerializedName("OrderId")
    private int orderid;
    @SerializedName("Trupay")
    private String Trupay;
    @SerializedName("OrderDispatchedMasterId")
    private int orderdispatchedmasterid;

    @SerializedName("ElectronicPaymentNo")
    String ElectronicPaymentNo;

    @SerializedName("paymentThrough")
    String paymentThrough;


    @SerializedName("orderDetails")
    private ArrayList<OrderItemDetails> orderDetails;

    public ArrayList<OrderItemDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderItemDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getDeliveryissuanceid() {
        return deliveryissuanceid;
    }

    public void setDeliveryissuanceid(int deliveryissuanceid) {
        this.deliveryissuanceid = deliveryissuanceid;
    }

    public int getClusterid() {
        return clusterid;
    }

    public void setClusterid(int clusterid) {
        this.clusterid = clusterid;
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

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public double getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(double deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public int getDivisionid() {
        return divisionid;
    }

    public void setDivisionid(int divisionid) {
        this.divisionid = divisionid;
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

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public int getRedispatchcount() {
        return redispatchcount;
    }

    public void setRedispatchcount(int redispatchcount) {
        this.redispatchcount = redispatchcount;
    }

    public String getDboymobileno() {
        return dboymobileno;
    }

    public void setDboymobileno(String dboymobileno) {
        this.dboymobileno = dboymobileno;
    }

    public String getDboyname() {
        return dboyname;
    }

    public void setDboyname(String dboyname) {
        this.dboyname = dboyname;
    }

    public double getRecivedamount() {
        return recivedamount;
    }

    public void setRecivedamount(double recivedamount) {
        this.recivedamount = recivedamount;
    }

    public double getPaymentamount() {
        return paymentamount;
    }

    public void setPaymentamount(double paymentamount) {
        this.paymentamount = paymentamount;
    }

    public double getCashamount() {
        return cashamount;
    }

    public void setCashamount(double cashamount) {
        this.cashamount = cashamount;
    }

    public double getElectronicamount() {
        return electronicamount;
    }

    public void setElectronicamount(double electronicamount) {
        this.electronicamount = electronicamount;
    }

    public double getCheckamount() {
        return checkamount;
    }

    public void setCheckamount(double checkamount) {
        this.checkamount = checkamount;
    }

    public double getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(double taxamount) {
        this.taxamount = taxamount;
    }

    public double getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(double discountamount) {
        this.discountamount = discountamount;
    }

    public double getGrossamount() {
        return grossamount;
    }

    public void setGrossamount(double grossamount) {
        this.grossamount = grossamount;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public String getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    public String getBillingaddress() {
        return billingaddress;
    }

    public void setBillingaddress(String billingaddress) {
        this.billingaddress = billingaddress;
    }

    public String getCustomerphonenum() {
        return customerphonenum;
    }

    public void setCustomerphonenum(String customerphonenum) {
        this.customerphonenum = customerphonenum;
    }

    public int getCustomercategoryid() {
        return customercategoryid;
    }

    public void setCustomercategoryid(int customercategoryid) {
        this.customercategoryid = customercategoryid;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSkcode() {
        return skcode;
    }

    public void setSkcode(String skcode) {
        this.skcode = skcode;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
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

    public String getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(String salesperson) {
        this.salesperson = salesperson;
    }

    public int getSalespersonid() {
        return salespersonid;
    }

    public void setSalespersonid(int salespersonid) {
        this.salespersonid = salespersonid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public int getOrderseq() {
        return orderseq;
    }

    public void setOrderseq(int orderseq) {
        this.orderseq = orderseq;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getOrderdispatchedmasterid() {
        return orderdispatchedmasterid;
    }

    public void setOrderdispatchedmasterid(int orderdispatchedmasterid) {
        this.orderdispatchedmasterid = orderdispatchedmasterid;
    }

    public String getTrupay() {
        return Trupay;
    }

    public void setTrupay(String trupay) {
        Trupay = trupay;
    }

    public String getElectronicPaymentNo() {
        return ElectronicPaymentNo;
    }

    public void setElectronicPaymentNo(String electronicPaymentNo) {
        ElectronicPaymentNo = electronicPaymentNo;
    }

    public String getPaymentThrough() {
        return paymentThrough;
    }

    public void setPaymentThrough(String paymentThrough) {
        this.paymentThrough = paymentThrough;
    }

}
