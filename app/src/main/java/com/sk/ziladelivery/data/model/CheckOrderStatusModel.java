package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOrderStatusModel {

    @Expose
    @SerializedName("checkRemaingOrderStatusDC")
    private CheckremaingorderstatusdcEntity Checkremaingorderstatusdc;
    @Expose
    @SerializedName("Message")
    private String Message;
    @Expose
    @SerializedName("Status")
    private boolean Status;

    public CheckremaingorderstatusdcEntity getCheckremaingorderstatusdc() {
        return Checkremaingorderstatusdc;
    }

    public void setCheckremaingorderstatusdc(CheckremaingorderstatusdcEntity Checkremaingorderstatusdc) {
        this.Checkremaingorderstatusdc = Checkremaingorderstatusdc;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public class CheckremaingorderstatusdcEntity {
        @Expose
        @SerializedName("IsProcess")
        private boolean Isprocess;
        @Expose
        @SerializedName("TotalAmount")
        private int Totalamount;
        @Expose
        @SerializedName("OrderCount")
        private int Ordercount;

        public boolean getIsprocess() {
            return Isprocess;
        }

        public void setIsprocess(boolean Isprocess) {
            this.Isprocess = Isprocess;
        }

        public int getTotalamount() {
            return Totalamount;
        }

        public void setTotalamount(int Totalamount) {
            this.Totalamount = Totalamount;
        }

        public int getOrdercount() {
            return Ordercount;
        }

        public void setOrdercount(int Ordercount) {
            this.Ordercount = Ordercount;
        }
    }
}
