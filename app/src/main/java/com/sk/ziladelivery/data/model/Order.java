
package com.sk.ziladelivery.data.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Order implements Serializable
{

    @SerializedName("OrderDispatchedMasterId")
    private int orderDispatchedMasterId;

    @SerializedName("OrderId")
    private int orderId;

    @SerializedName("CompanyId")

    private int companyId;
    @SerializedName("SalesPersonId")

    private int salesPersonId;
    @SerializedName("SalesPerson")

    private String salesPerson;
    @SerializedName("SalesMobile")

    private String salesMobile;
    @SerializedName("CustomerId")

    private int customerId;
    @SerializedName("CustomerName")

    private String customerName;
    @SerializedName("ShopName")

    private String shopName;
    @SerializedName("Skcode")

    private String skcode;
    @SerializedName("Status")

    private String status;
    @SerializedName("ReDispatchedStatus")

    private String reDispatchedStatus;
    @SerializedName("CanceledStatus")

    private Object canceledStatus;
    @SerializedName("invoice_no")

    private String invoiceNo;
    @SerializedName("Trupay")

    private String trupay;
    @SerializedName("CustomerCategoryId")

    private int customerCategoryId;
    @SerializedName("CustomerCategoryName")

    private Object customerCategoryName;
    @SerializedName("CustomerType")

    private Object customerType;
    @SerializedName("Customerphonenum")

    private String customerphonenum;
    @SerializedName("BillingAddress")

    private String billingAddress;
    @SerializedName("ShippingAddress")

    private String shippingAddress;
    @SerializedName("comments")

    private String comments;
    @SerializedName("deliveryCharge")

    private double deliveryCharge;
    @SerializedName("TotalAmount")

    private double totalAmount;
    @SerializedName("GrossAmount")

    private double grossAmount;
    @SerializedName("DiscountAmount")

    private double discountAmount;
    @SerializedName("TaxAmount")

    private double taxAmount;
    @SerializedName("SGSTTaxAmmount")

    private double sGSTTaxAmmount;
    @SerializedName("CGSTTaxAmmount")

    private double cGSTTaxAmmount;
    @SerializedName("CheckNo")

    private String checkNo;
    @SerializedName("CheckAmount")

    private double checkAmount;
    @SerializedName("ElectronicPaymentNo")

    private String electronicPaymentNo;
    @SerializedName("ElectronicAmount")

    private double electronicAmount;
    @SerializedName("CashAmount")

    private double cashAmount;
    @SerializedName("PaymentAmount")

    private double paymentAmount;
    @SerializedName("RecivedAmount")

    private double recivedAmount;
    @SerializedName("DboyName")

    private String dboyName;
    @SerializedName("DboyMobileNo")

    private String dboyMobileNo;
    @SerializedName("ReDispatchCount")

    private double reDispatchCount;
    @SerializedName("CityId")

    private double cityId;
    @SerializedName("WarehouseId")

    private double warehouseId;
    @SerializedName("WarehouseName")

    private String warehouseName;
    @SerializedName("active")

    private Boolean active;
    @SerializedName("OrderedDate")

    private String orderedDate;
    @SerializedName("CreatedDate")

    private String createdDate;
    @SerializedName("Deliverydate")

    private String deliverydate;
    @SerializedName("UpdatedDate")

    private String updatedDate;
    @SerializedName("Deleted")

    private Boolean deleted;
    @SerializedName("DivisionId")

    private double divisionId;
    @SerializedName("ClusterId")

    private double clusterId;
    @SerializedName("Signimg")

    private Object signimg;
    @SerializedName("ClusterName")

    private String clusterName;
    @SerializedName("OrderDate")

    private String orderDate;
    @SerializedName("check")

    private Boolean check;
    @SerializedName("lat")

    private double lat;
    @SerializedName("lg")

    private double lg;
    @SerializedName("cash")

    private Boolean cash;
    @SerializedName("electronic")

    private Boolean electronic;
    @SerializedName("cheq")

    private Boolean cheq;
    @SerializedName("DboycheckRecived")

    private Boolean dboycheckRecived;
    @SerializedName("BounceCheqAmount")

    private double bounceCheqAmount;
    @SerializedName("WalletAmount")

    private double walletAmount;
    @SerializedName("RewardPoint")

    private double rewardPoint;
    @SerializedName("ShortAmount")

    private double shortAmount;
    @SerializedName("OnlineServiceTax")

    private double onlineServiceTax;
    @SerializedName("OrderTakenSalesPersonId")

    private double orderTakenSalesPersonId;
    @SerializedName("OrderTakenSalesPerson")

    private String orderTakenSalesPerson;
    @SerializedName("Tin_No")

    private String tinNo;
    @SerializedName("orderDetails")

    private List<OrderDetail> orderDetails = null;
    @SerializedName("AssignedOrders")

    private Object assignedOrders;
    @SerializedName("DeliveryIssuanceId")

    private double deliveryIssuanceId;
    @SerializedName("userid")

    private double userid;
    @SerializedName("Savingamount")

    private Double savingamount;
    @SerializedName("orderProcess")

    private Boolean orderProcess;

    @SerializedName("DeliveryIssuanceIdOrderDeliveryMaster")
    private double deliveryIssuanceIdOrderDeliveryMaster;

    private final static long serialVersionUID = -8890359181953612986L;
    @SerializedName("paymentThrough")

    private String paymentThrough;

    public String getPaymentThrough() {
        return paymentThrough;
    }

    public void setPaymentThrough(String paymentThrough) {
        this.paymentThrough = paymentThrough;
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Order() {
    }


    public int getOrderDispatchedMasterId() {
        return orderDispatchedMasterId;
    }

    public void setOrderDispatchedMasterId(int orderDispatchedMasterId) {
        this.orderDispatchedMasterId = orderDispatchedMasterId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(int salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public String getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getSalesMobile() {
        return salesMobile;
    }

    public void setSalesMobile(String salesMobile) {
        this.salesMobile = salesMobile;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSkcode() {
        return skcode;
    }

    public void setSkcode(String skcode) {
        this.skcode = skcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReDispatchedStatus() {
        return reDispatchedStatus;
    }

    public void setReDispatchedStatus(String reDispatchedStatus) {
        this.reDispatchedStatus = reDispatchedStatus;
    }

    public Object getCanceledStatus() {
        return canceledStatus;
    }

    public void setCanceledStatus(Object canceledStatus) {
        this.canceledStatus = canceledStatus;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTrupay() {
        return trupay;
    }

    public void setTrupay(String trupay) {
        this.trupay = trupay;
    }

    public int getCustomerCategoryId() {
        return customerCategoryId;
    }

    public void setCustomerCategoryId(int customerCategoryId) {
        this.customerCategoryId = customerCategoryId;
    }

    public Object getCustomerCategoryName() {
        return customerCategoryName;
    }

    public void setCustomerCategoryName(Object customerCategoryName) {
        this.customerCategoryName = customerCategoryName;
    }

    public Object getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Object customerType) {
        this.customerType = customerType;
    }

    public String getCustomerphonenum() {
        return customerphonenum;
    }

    public void setCustomerphonenum(String customerphonenum) {
        this.customerphonenum = customerphonenum;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getSGSTTaxAmmount() {
        return sGSTTaxAmmount;
    }

    public void setSGSTTaxAmmount(Double sGSTTaxAmmount) {
        this.sGSTTaxAmmount = sGSTTaxAmmount;
    }

    public Double getCGSTTaxAmmount() {
        return cGSTTaxAmmount;
    }

    public void setCGSTTaxAmmount(Double cGSTTaxAmmount) {
        this.cGSTTaxAmmount = cGSTTaxAmmount;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public double getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(double checkAmount) {
        this.checkAmount = checkAmount;
    }

    public String getElectronicPaymentNo() {
        return electronicPaymentNo;
    }

    public void setElectronicPaymentNo(String electronicPaymentNo) {
        this.electronicPaymentNo = electronicPaymentNo;
    }

    public double getElectronicAmount() {
        return electronicAmount;
    }

    public void setElectronicAmount(double electronicAmount) {
        this.electronicAmount = electronicAmount;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public double getRecivedAmount() {
        return recivedAmount;
    }

    public void setRecivedAmount(double recivedAmount) {
        this.recivedAmount = recivedAmount;
    }

    public String getDboyName() {
        return dboyName;
    }

    public void setDboyName(String dboyName) {
        this.dboyName = dboyName;
    }

    public String getDboyMobileNo() {
        return dboyMobileNo;
    }

    public void setDboyMobileNo(String dboyMobileNo) {
        this.dboyMobileNo = dboyMobileNo;
    }

    public double getReDispatchCount() {
        return reDispatchCount;
    }

    public void setReDispatchCount(double reDispatchCount) {
        this.reDispatchCount = reDispatchCount;
    }

    public double getCityId() {
        return cityId;
    }

    public void setCityId(double cityId) {
        this.cityId = cityId;
    }

    public double getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(double warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
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

    public double getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(double divisionId) {
        this.divisionId = divisionId;
    }

    public double getClusterId() {
        return clusterId;
    }

    public void setClusterId(double clusterId) {
        this.clusterId = clusterId;
    }

    public Object getSignimg() {
        return signimg;
    }

    public void setSignimg(Object signimg) {
        this.signimg = signimg;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Boolean getElectronic() {
        return electronic;
    }

    public void setElectronic(Boolean electronic) {
        this.electronic = electronic;
    }

    public Boolean getCheq() {
        return cheq;
    }

    public void setCheq(Boolean cheq) {
        this.cheq = cheq;
    }

    public Boolean getDboycheckRecived() {
        return dboycheckRecived;
    }

    public void setDboycheckRecived(Boolean dboycheckRecived) {
        this.dboycheckRecived = dboycheckRecived;
    }

    public double getBounceCheqAmount() {
        return bounceCheqAmount;
    }

    public void setBounceCheqAmount(double bounceCheqAmount) {
        this.bounceCheqAmount = bounceCheqAmount;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public double getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(double rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public double getShortAmount() {
        return shortAmount;
    }

    public void setShortAmount(double shortAmount) {
        this.shortAmount = shortAmount;
    }

    public double getOnlineServiceTax() {
        return onlineServiceTax;
    }

    public void setOnlineServiceTax(double onlineServiceTax) {
        this.onlineServiceTax = onlineServiceTax;
    }

    public double getOrderTakenSalesPersonId() {
        return orderTakenSalesPersonId;
    }

    public void setOrderTakenSalesPersonId(double orderTakenSalesPersonId) {
        this.orderTakenSalesPersonId = orderTakenSalesPersonId;
    }

    public String getOrderTakenSalesPerson() {
        return orderTakenSalesPerson;
    }

    public void setOrderTakenSalesPerson(String orderTakenSalesPerson) {
        this.orderTakenSalesPerson = orderTakenSalesPerson;
    }

    public String getTinNo() {
        return tinNo;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Object getAssignedOrders() {
        return assignedOrders;
    }

    public void setAssignedOrders(Object assignedOrders) {
        this.assignedOrders = assignedOrders;
    }

    public double getDeliveryIssuanceId() {
        return deliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(double deliveryIssuanceId) {
        this.deliveryIssuanceId = deliveryIssuanceId;
    }

    public double getUserid() {
        return userid;
    }

    public void setUserid(double userid) {
        this.userid = userid;
    }

    public Double getSavingamount() {
        return savingamount;
    }

    public void setSavingamount(Double savingamount) {
        this.savingamount = savingamount;
    }

    public Boolean getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(Boolean orderProcess) {
        this.orderProcess = orderProcess;
    }

    public double getDeliveryIssuanceIdOrderDeliveryMaster() {
        return deliveryIssuanceIdOrderDeliveryMaster;
    }

    public void setDeliveryIssuanceIdOrderDeliveryMaster(double deliveryIssuanceIdOrderDeliveryMaster) {
        this.deliveryIssuanceIdOrderDeliveryMaster = deliveryIssuanceIdOrderDeliveryMaster;
    }

}
