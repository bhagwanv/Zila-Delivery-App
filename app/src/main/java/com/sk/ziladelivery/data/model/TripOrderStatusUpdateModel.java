package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class TripOrderStatusUpdateModel {

    @Expose
    @SerializedName("customerOrderInfo")
    private ArrayList<CustomerorderinfoEntity> customerorderinfo;

    @Expose
    @SerializedName("customerInfo")
    private CustomerinfoEntity customerinfo;

    public ArrayList<CustomerorderinfoEntity> getCustomerorderinfo() {
        return customerorderinfo;
    }

    public void setCustomerorderinfo(ArrayList<CustomerorderinfoEntity> customerorderinfo) {
        this.customerorderinfo = customerorderinfo;
    }

    public CustomerinfoEntity getCustomerinfo() {
        return customerinfo;
    }

    public void setCustomerinfo(CustomerinfoEntity customerinfo) {
        this.customerinfo = customerinfo;
    }

    public  class CustomerorderinfoEntity {
        @Expose
        @SerializedName("Status")
        private String status;
        @Expose
        @SerializedName("NoOfItems")
        private int noofitems;
        @Expose
        @SerializedName("GrossAmount")
        private double grossamount;
        @Expose
        @SerializedName("RemaningAmount")
        private double RemaningAmount;

        @Expose
        @SerializedName("OrderId")
        private int orderid;

        @Expose
        @SerializedName("ReDispatchCount")
        private int ReDispatchCount;
        @Expose
        @SerializedName("DeliveryIssuanceId")
        private int DeliveryIssuanceId;


        @Expose
        @SerializedName("OnilnePayment")
        private boolean OnilnePayment;

        @Expose
        @SerializedName("PaymentFrom")
        private String PaymentFrom;

        @Expose
        @SerializedName("OnlineAmount")
        private double OnlineAmount;

        @Expose
        @SerializedName("RemaningOrderAmount")
        private double RemaningOrderAmount;

        @Expose
        @SerializedName("TripPlannerConfirmedOrderId")
        private int TripPlannerConfirmedOrderId;


        @Expose
        @SerializedName("TripPlannerConfirmedDetailId")
        private int TripPlannerConfirmedDetailId;

        @Expose
        @SerializedName("WorkingStatus")
        private int WorkingStatus;

        @Expose
        @SerializedName("IsApproved")
        private boolean IsApproved;

        @Expose
        @SerializedName("IsRejected")
        private boolean IsRejected;


        @Expose
        @SerializedName("RemaningTimeInMins")
        private long RemaningTimeInMins;

        @Expose
        @SerializedName("Reason")
        private String Reason;

        @Expose
        @SerializedName("IsOTPSent")
        private boolean IsOTPSent;

        @Expose
        @SerializedName("OTPSentRemaningTimeInSec")
        private int OTPSentRemaningTimeInSec;

        @Expose
        @SerializedName("MessageText")
        private String MessageText;

        @Expose
        @SerializedName("boolWorkingStatus")
        private boolean boolWorkingStatus;

        @Expose
        @SerializedName("ReAttemptCount")
        private int ReAttemptCount;

        @Expose
        @SerializedName("OrderReDispatchCount")
        private int OrderReDispatchCount;
        @Expose
        @SerializedName("SalePersonMobile")
        private String  SalePersonMobile;
        @Expose
        @SerializedName("SalePersonName")
        private String  SalePersonName;

        @Expose
        @SerializedName("IsDeliveryCancelledEnable")
        private boolean IsDeliveryCancelledEnable;


        @Expose
        @SerializedName("OrderType")
        private String  OrderType;

         @Expose
        @SerializedName("IsETAOrderHide")
        private boolean  IsETAOrderHide ;

        @Expose
        @SerializedName("IsScaleUpPaymentOverdue")
        private boolean IsScaleUpPaymentOverdue;

        public boolean isScaleUpPaymentOverdue() {
            return IsScaleUpPaymentOverdue;
        }

        public void setScaleUpPaymentOverdue(boolean scaleUpPaymentOverdue) {
            IsScaleUpPaymentOverdue = scaleUpPaymentOverdue;
        }

        public boolean isETAOrderHide() {
            return IsETAOrderHide;
        }

        public void setETAOrderHide(boolean ETAOrderHide) {
            IsETAOrderHide = ETAOrderHide;
        }

        public int getTripPlannerConfirmedDetailId() {
            return TripPlannerConfirmedDetailId;
        }

        public void setTripPlannerConfirmedDetailId(int tripPlannerConfirmedDetailId) {
            TripPlannerConfirmedDetailId = tripPlannerConfirmedDetailId;
        }

        public int getTripPlannerConfirmedOrderId() {
            return TripPlannerConfirmedOrderId;
        }

        public void setTripPlannerConfirmedOrderId(int tripPlannerConfirmedOrderId) {
            TripPlannerConfirmedOrderId = tripPlannerConfirmedOrderId;
        }

        public String getOrderType() {
            return OrderType;
        }

        public void setOrderType(String orderType) {
            OrderType = orderType;
        }

        public boolean isDeliveryCancelledEnable() {
            return IsDeliveryCancelledEnable;
        }

        public void setDeliveryCancelledEnable(boolean deliveryCancelledEnable) {
            IsDeliveryCancelledEnable = deliveryCancelledEnable;
        }

        public double getRemaningAmount() {
            return RemaningAmount;
        }

        public void setRemaningAmount(double remaningAmount) {
            RemaningAmount = remaningAmount;
        }

        public String getSalePersonMobile() {
            return SalePersonMobile;
        }

        public void setSalePersonMobile(String salePersonMobile) {
            SalePersonMobile = salePersonMobile;
        }

        public String getSalePersonName() {
            return SalePersonName;
        }

        public void setSalePersonName(String salePersonName) {
            SalePersonName = salePersonName;
        }

        public int getReAttemptCount() {
            return ReAttemptCount;
        }

        public void setReAttemptCount(int reAttemptCount) {
            ReAttemptCount = reAttemptCount;
        }

        public int getOrderReDispatchCount() {
            return OrderReDispatchCount;
        }

        public void setOrderReDispatchCount(int orderReDispatchCount) {
            OrderReDispatchCount = orderReDispatchCount;
        }

        public String getPaymentFrom() {
            return PaymentFrom;
        }

        public void setPaymentFrom(String paymentFrom) {
            PaymentFrom = paymentFrom;
        }

        public double getOnlineAmount() {
            return OnlineAmount;
        }

        public void setOnlineAmount(double onlineAmount) {
            OnlineAmount = onlineAmount;
        }

        public double getRemaningOrderAmount() {
            return RemaningOrderAmount;
        }

        public void setRemaningOrderAmount(double remaningOrderAmount) {
            RemaningOrderAmount = remaningOrderAmount;
        }

        public String getMessageText() {
            return MessageText;
        }

        public void setMessageText(String messageText) {
            MessageText = messageText;
        }

        public boolean isOTPSent() {
            return IsOTPSent;
        }

        public void setOTPSent(boolean OTPSent) {
            IsOTPSent = OTPSent;
        }

        public int getOTPSentRemaningTimeInSec() {
            return OTPSentRemaningTimeInSec;
        }

        public void setOTPSentRemaningTimeInSec(int OTPSentRemaningTimeInSec) {
            this.OTPSentRemaningTimeInSec = OTPSentRemaningTimeInSec;
        }

        public String getReason() {
            return Reason;
        }

        public void setReason(String reason) {
            Reason = reason;
        }

        public boolean isApproved() {
            return IsApproved;
        }

        public void setApproved(boolean approved) {
            IsApproved = approved;
        }

        public boolean isRejected() {
            return IsRejected;
        }

        public void setRejected(boolean rejected) {
            IsRejected = rejected;
        }

        public long getRemaningTimeInMins() {
            return RemaningTimeInMins;
        }

        public void setRemaningTimeInMins(long remaningTimeInMins) {
            RemaningTimeInMins = remaningTimeInMins;
        }

        public int getWorkingStatus() {
            return WorkingStatus;
        }

        public void setWorkingStatus(int workingStatus) {
            WorkingStatus = workingStatus;
        }

        public boolean isBoolWorkingStatus() {
            return boolWorkingStatus;
        }

        public void setBoolWorkingStatus(boolean boolWorkingStatus) {
            this.boolWorkingStatus = boolWorkingStatus;
        }


        @Expose
        @SerializedName("OrderDetails")
        private ArrayList<ItemOrderDetailModel> OrderDetailsModel;

        public ArrayList<ItemOrderDetailModel> getOrderDetailsModel() {
            return OrderDetailsModel;
        }

        public void setOrderDetailsModel(ArrayList<ItemOrderDetailModel> orderDetailsModel) {
            OrderDetailsModel = orderDetailsModel;
        }

        public boolean isOnilnePayment() {
            return OnilnePayment;
        }

        public void setOnilnePayment(boolean onilnePayment) {
            OnilnePayment = onilnePayment;
        }
        public int getReDispatchCount() {
            return ReDispatchCount;
        }

        public void setReDispatchCount(int reDispatchCount) {
            ReDispatchCount = reDispatchCount;
        }
        public int getDeliveryIssuanceId() {
            return DeliveryIssuanceId;
        }

        public void setDeliveryIssuanceId(int deliveryIssuanceId) {
            DeliveryIssuanceId = deliveryIssuanceId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getNoofitems() {
            return noofitems;
        }

        public void setNoofitems(int noofitems) {
            this.noofitems = noofitems;
        }

        public double getGrossamount() {
            return grossamount;
        }

        public void setGrossamount(double grossamount) {
            this.grossamount = grossamount;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        @Override
        public String toString() {
            return "CustomerorderinfoEntity{" +
                    "status='" + status + '\'' +
                    ", noofitems=" + noofitems +
                    ", grossamount=" + grossamount +
                    ", RemaningAmount=" + RemaningAmount +
                    ", orderid=" + orderid +
                    ", ReDispatchCount=" + ReDispatchCount +
                    ", DeliveryIssuanceId=" + DeliveryIssuanceId +
                    ", OnilnePayment=" + OnilnePayment +
                    ", PaymentFrom='" + PaymentFrom + '\'' +
                    ", OnlineAmount=" + OnlineAmount +
                    ", RemaningOrderAmount=" + RemaningOrderAmount +
                    ", TripPlannerConfirmedOrderId=" + TripPlannerConfirmedOrderId +
                    ", TripPlannerConfirmedDetailId=" + TripPlannerConfirmedDetailId +
                    ", WorkingStatus=" + WorkingStatus +
                    ", IsApproved=" + IsApproved +
                    ", IsRejected=" + IsRejected +
                    ", RemaningTimeInMins=" + RemaningTimeInMins +
                    ", Reason='" + Reason + '\'' +
                    ", IsOTPSent=" + IsOTPSent +
                    ", OTPSentRemaningTimeInSec=" + OTPSentRemaningTimeInSec +
                    ", MessageText='" + MessageText + '\'' +
                    ", boolWorkingStatus=" + boolWorkingStatus +
                    ", ReAttemptCount=" + ReAttemptCount +
                    ", OrderReDispatchCount=" + OrderReDispatchCount +
                    ", SalePersonMobile='" + SalePersonMobile + '\'' +
                    ", SalePersonName='" + SalePersonName + '\'' +
                    ", IsDeliveryCancelledEnable=" + IsDeliveryCancelledEnable +
                    ", OrderType='" + OrderType + '\'' +
                    ", IsETAOrderHide=" + IsETAOrderHide +
                    ", OrderDetailsModel=" + OrderDetailsModel +
                    '}';
        }
    }

    public static class CustomerinfoEntity {
        @Expose
        @SerializedName("ShopName")
        private String shopname;
        @Expose
        @SerializedName("Skcode")
        private String skcode;
        @Expose
        @SerializedName("BillingAddress")
        private String BillingAddress;
        @Expose
        @SerializedName("GrossAmount")
        private double GrossAmount;

        @Expose
        @SerializedName("TotalDeliverd")
        private double TotalDeliverd;

        @Expose
        @SerializedName("isReAttempt")
        private boolean isReAttempt;

        @Expose
        @SerializedName("CRMTags")
        private String CRMTags;

        public String getCRMTags() {
            return CRMTags;
        }

        public void setCRMTags(String CRMTags) {
            this.CRMTags = CRMTags;
        }

        public boolean isReAttempt() {
            return isReAttempt;
        }

        public void setReAttempt(boolean reAttempt) {
            isReAttempt = reAttempt;
        }

        public String getBillingAddress() {
            return BillingAddress;
        }

        public void setBillingAddress(String billingAddress) {
            BillingAddress = billingAddress;
        }

        public double getGrossAmount() {
            return GrossAmount;
        }

        public void setGrossAmount(double grossAmount) {
            GrossAmount = grossAmount;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getSkcode() {
            return skcode;
        }

        public void setSkcode(String skcode) {
            this.skcode = skcode;
        }


        public double getTotalDeliverd() {
            return TotalDeliverd;
        }

        public void setTotalDeliverd(double totalDeliverd) {
            TotalDeliverd = totalDeliverd;
        }
    }

    public class ItemOrderDetailModel {
        @Expose
        @SerializedName("OrderId")
        private int OrderId;

        @Expose
        @SerializedName("itemname")
        private String itemname;

        @Expose
        @SerializedName("TotalAmt")
        private double TotalAmt;

        @Expose
        @SerializedName("qty")
        private int qty;

        @Expose
        @SerializedName("Status")
        private String  Status;


        public int getOrderId() {
            return OrderId;
        }

        public void setOrderId(int orderId) {
            OrderId = orderId;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public double getTotalAmt() {
            return TotalAmt;
        }

        public void setTotalAmt(double totalAmt) {
            TotalAmt = totalAmt;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }

    @Override
    public String toString() {
        return "TripOrderStatusUpdateModel{" +
                "customerorderinfo=" + customerorderinfo +
                ", customerinfo=" + customerinfo +
                '}';
    }
}
