package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class SearchOrderIDResponseModel {
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("orderdata")
    private Orderdata orderdata;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Orderdata getOrderdata() {
        return orderdata;
    }

    public void setOrderdata(Orderdata orderdata) {
        this.orderdata = orderdata;
    }

    public  class Orderdata {
        @Expose
        @SerializedName("ShippingAddress")
        private String ShippingAddress;
        @Expose
        @SerializedName("TotalAmount")
        private double TotalAmount;
        @Expose
        @SerializedName("Customerphonenum")
        private String Customerphonenum;
        @Expose
        @SerializedName("CreatedDate")
        private String CreatedDate;
        @Expose
        @SerializedName("orderCount")
        private int orderCount;
        @Expose
        @SerializedName("Status")
        private String Status;
        @Expose
        @SerializedName("ShopName")
        private String ShopName;
        @Expose
        @SerializedName("DeliveryIssuanceId")
        private int DeliveryIssuanceId;
        @Expose
        @SerializedName("CustomerName")
        private String CustomerName;
        @Expose
        @SerializedName("OddetailCount")
        private int OddetailCount;
        @Expose
        @SerializedName("OrderId")
        private int OrderId;

        public String getShippingAddress() {
            return ShippingAddress;
        }

        public void setShippingAddress(String ShippingAddress) {
            this.ShippingAddress = ShippingAddress;
        }

        public double getTotalAmount() {
            return TotalAmount;
        }

        public void setTotalAmount(double TotalAmount) {
            this.TotalAmount = TotalAmount;
        }

        public String getCustomerphonenum() {
            return Customerphonenum;
        }

        public void setCustomerphonenum(String Customerphonenum) {
            this.Customerphonenum = Customerphonenum;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public int getDeliveryIssuanceId() {
            return DeliveryIssuanceId;
        }

        public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
            this.DeliveryIssuanceId = DeliveryIssuanceId;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String CustomerName) {
            this.CustomerName = CustomerName;
        }

        public int getOddetailCount() {
            return OddetailCount;
        }

        public void setOddetailCount(int OddetailCount) {
            this.OddetailCount = OddetailCount;
        }

        public int getOrderId() {
            return OrderId;
        }

        public void setOrderId(int OrderId) {
            this.OrderId = OrderId;
        }
    }
}
