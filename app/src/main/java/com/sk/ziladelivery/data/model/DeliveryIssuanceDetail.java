package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public  class DeliveryIssuanceDetail implements Serializable {
    @Expose
    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;
    @Expose
    @SerializedName("OrderedDate")
    private String OrderedDate;
    @Expose
    @SerializedName("IsElectronicPayment")
    private boolean IsElectronicPayment;
    @Expose
    @SerializedName("WarehouseId")
    private int WarehouseId;
    @Expose
    @SerializedName("DboyMobileNo")
    private String DboyMobileNo;
    @Expose
    @SerializedName("DboyName")
    private String DboyName;
    @Expose
    @SerializedName("RecivedAmount")
    private double RecivedAmount;

    @SerializedName("ElectronicPaymentNo")
    private String ElectronicPaymentNo;

    @Expose
    @SerializedName("GrossAmount")
    private int GrossAmount;
    @Expose
    @SerializedName("TotalAmount")
    private double TotalAmount;
    @Expose
    @SerializedName("comments")
    private String comments;
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

    @SerializedName("ChequeImageUrl")
    private String ChequeImageUrl;

    @SerializedName("ChequeDate")
    private String ChequeDate;


    @Expose
    @SerializedName("CustomerId")
    private int CustomerId;
    @Expose
    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("ElectronicPaymentType")
    private int ElectronicPaymentType;

    @SerializedName("DeliveryDate")
    private String DeliveryDate;

    @SerializedName("ChequeBankName")
    private String ChequeBankName;

    @SerializedName("PaymentDetails")
    private ArrayList<PaymentDetails> PaymentDetails;





    public ArrayList<PaymentDetails> getPaymentDetails() {
        return PaymentDetails;
    }

    public void setPaymentDetails(ArrayList<PaymentDetails> paymentDetails) {
        PaymentDetails = paymentDetails;
    }

    public String getChequeBankName() {
        return ChequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        ChequeBankName = chequeBankName;
    }

    public String getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(String chequeDate) {
        ChequeDate = chequeDate;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public String getChequeImageUrl() {
        return ChequeImageUrl;
    }

    public void setChequeImageUrl(String chequeImageUrl) {
        ChequeImageUrl = chequeImageUrl;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }
    public int getElectronicPaymentType() {
        return ElectronicPaymentType;
    }

    public void setElectronicPaymentType(int electronicPaymentType) {
        ElectronicPaymentType = electronicPaymentType;
    }
    public String getOrderedDate() {
        return OrderedDate;
    }

    public void setOrderedDate(String OrderedDate) {
        this.OrderedDate = OrderedDate;
    }

    public boolean getIsElectronicPayment() {
        return IsElectronicPayment;
    }

    public void setIsElectronicPayment(boolean IsElectronicPayment) {
        this.IsElectronicPayment = IsElectronicPayment;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int WarehouseId) {
        this.WarehouseId = WarehouseId;
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



    public String getElectronicPaymentNo() {
        return ElectronicPaymentNo;
    }

    public void setElectronicPaymentNo(String ElectronicPaymentNo) {
        this.ElectronicPaymentNo = ElectronicPaymentNo;
    }


    public int getGrossAmount() {
        return GrossAmount;
    }

    public void setGrossAmount(int GrossAmount) {
        this.GrossAmount = GrossAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }
}
