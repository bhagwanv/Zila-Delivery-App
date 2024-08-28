package com.sk.ziladelivery.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankNameModel {

    @SerializedName("BankNameDc")
    private List<BankNameDc> BankNameDc;

    @SerializedName("status")
    private boolean status;

    @SerializedName("Message")
    private String Message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<BankNameDc> getBankNameDc() {
        return BankNameDc;
    }

    public void setBankNameDc(List<BankNameDc> BankNameDc) {
        this.BankNameDc = BankNameDc;
    }

    public class BankNameDc
    {
        @SerializedName("Id")
        private int Id;

        @SerializedName("BankName")
        private String BankName;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String bankName) {
            BankName = bankName;
        }
    }
}
